package com.frame.execute.structure;

import com.frame.execute.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by fdh on 2017/7/26.
 */

/**
 * <p>AppendableTask is unit of work, it's an special {@link Executor}, which can let a series of executor work concurrently.
 * Every task is for a specific production, so what it receives is what it gives, because a task is used for processing an
 * production like filling it, which means just make a production more useful, but won't change it to another type.
 * The state of task is determined by two field, {@code done} and {@code closed}, you could check them with {@code isDone(); isClosed();}.
 * <ol>
 * <li>done -> if isDone() returns true, it means that all of the executors in the task has finished their work, and the
 * task has been hang up, but it doesn't mean the task's work has finished, because you can add a new {@link Executor} into the task
 * with {@code appendExecutor(Executor executor)}, and the task will start itself and continue to do the new work.</li>
 * <li>closed -> if isClosed() returns true, it means you cannot add a new executor into the task, but it doesn't mean the task has been stop,
 * because it will continue to do the rest work until all of the executors executed.</li>
 * </ol>
 * Above all, if you want to check if the task is stop, you have to use {@code isDone() && isClosed()}. By the way, you can call {@code close()} to
 * forbid the other threads to add executor.
 * * If you want to get the executors' result, call {@code get()}.
 * * The task isn't suggested to run in the main thread, because it may be blocked.
 * </p>
 */
public class AppendableTask<P> extends DynamicAppendableFlow<P, P> {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * <p>The capacity of blocking queue,when it is full, the thread that tries to put executors in it will be blocked. </p>
     */
    private int maxExecutor = 10;
    /**
     * <p>The cache thread pool means the task can be acting concurrently</p>
     */
    private final ExecutorService pool = Executors.newCachedThreadPool();
    /**
     * <p>AppendableTask's executors, the production will be passed into the executors and let them process it</p>
     */
    private BlockingQueue<Executor<P, ?>> executors = new LinkedBlockingQueue<>(maxExecutor);


    /**
     * <p>The Queue is used for storing futures</p>
     */
    private Queue<Future<?>> futures = new ConcurrentLinkedQueue<>();

    /**
     * <p>The list is used for storing every executor's result</p>
     */
    private List<Object> results = new LinkedList<>();

    /**
     * <p>Hold the thread that runs the current task to interrupt this thread.</p>
     */
    private Thread taskThread;

    public AppendableTask() {
    }


    public AppendableTask(int maxExecutor) {
        this.maxExecutor = maxExecutor;
    }

    public AppendableTask(P production, int maxExecutor) {
        super(production);
        this.maxExecutor = maxExecutor;
    }

    public AppendableTask(CyclicBarrier barrier, P production, int maxExecutor) {
        super(barrier, production);
        this.maxExecutor = maxExecutor;
    }



    /**
     * <p>reset the state of the task, inject the thread of the task</p>
     */
    @Override
    public void prepareForExecute() {
        this.taskThread = Thread.currentThread();
        if (isClosed()) {
            compareAndSetClosed(true, false);
        }
        if (isDone()) {
            compareAndSetDone(true, false);
        }
    }

    /**
     * <p>The method will loop taking an executor from the blocking queue and execute,
     * when the task is closed and no more executors can be taken, it will return and let the post-processing
     * deal the result.</p>
     *
     * @return null
     * @throws Exception any exceptions that can be thrown when execute
     */
    @Override
    public Object exec() throws Exception {
        try {
            // loop to take mission and execute
            for (; ; ) {
                // if the task has already rejected the mission, and no more exist mission
                // start to wait the exist mission complete
                if (isClosed() && executors.isEmpty()) {
                    return null;
                }
                if (executors.isEmpty()) {
                    if (!isDone()) {
                        compareAndSetDone(false, true);
                    }
                }
                // else execute mission
                try {
                    Executor<?, ?> executor = executors.take();
                    Future<?> future = pool.submit(executor);
                    // put the future into future queue
                    futures.offer(future);
                } catch (InterruptedException ignored) {
                    if(logger.isDebugEnabled()) {
                        logger.debug("task has been closed.");
                    }
                }
            }
        } finally {
            pool.shutdown();
        }
    }


    /**
     * <p>Wait until all of the work finished and return the well-precessed prodution , storing every executor's result
     * at the same time.</p>
     *
     * @return the well-processed production
     */
    @Override
    public P postProcessForExecute(Object result) {
        // loop to check if all mission have completed
        for (; ; ) {
            Future<?> future = futures.poll();
            if (future == null) {
                compareAndSetDone(false, true);
                return production;
            }
            // get the result and put it into result list
            try {
                Object re = future.get();
                results.add(re);
            } catch (InterruptedException | ExecutionException e) {
                // todo
            }
        }
    }
    /**
     * <p>Dynamically append a executor in an task, if the queue is full, the thread will return false. and if the task has been closed,
     * return false, too</p>
     *
     * @param executor the executor to add
     * @return
     */
    @Override
    public Boolean appendExecutor(Executor<P, ?> executor) {
        if (!isClosed()) {
            if (isDone()) {
                compareAndSetDone(true, false);
            }
            return executors.offer(executor);
        }
        return false;
    }


    /**
     * <p>call this method to forbid any thread to add executors into this task</p>
     */
    @Override
    public void close() {
        if (!isClosed()) {
            compareAndSetClosed(false, true);
        }
        if(taskThread != null) {
            taskThread.interrupt();
        } else {
            // todo
        }
    }

    /**
     * <p>Get the results of each executor</p>
     *
     * @return
     */
    public List<Object> get() {
        if (!isClosed() || !isDone()) {
            return null;
        }
        return results;
    }



}
