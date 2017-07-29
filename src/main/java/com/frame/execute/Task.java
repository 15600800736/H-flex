package com.frame.execute;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by fdh on 2017/7/26.
 */

/**
 * <p>Task is a chain of executor, it has a {@link Executor} queue in it and will execute them by their order,
 * task will take executors util it meets the {@link }, so all of executors behind an {@link } will be ignore
 * because the task has been closed, you can call {@code isClosed()} to check if you can put a new {@link Executor} in it.
 * When all of the executor finish their work, the task will atomically change its {@code isClosed} to true, which means you
 * cannot put a new {@link Executor}. On the other hand, the field {@code isDone()} shows if the task has done what it should do,
 * in other words, if {@code isDone()} returns true, you can get the result as you expect, but if not, something may be wrong when task executes.
 * So there are three states in the task, they are
 * <ol>
 * <li>finished rightly  {@code isDone() == true && isClosed() == true}</li>
 * <li>finished wrongly {@code isDone() == false && isClosed() == true}</li>
 * <li>not finished yet {@code isClosed() == false}</li>
 * </ol>
 * You needn't worry about {@code isClosed() == false && isDone() == true} , that won't happen</p>
 */
public class Task extends Executor<List<Object>> {
    /**
     * <p>The capacity of blocking queue,when it is full, the thread that tries to put executors in it will be hang up. </p>
     */
    private int maxExecutor = 10;
    /**
     * <p>The cache thread pool means the task can be acting as multi thread</p>
     */
    private final ExecutorService pool = Executors.newCachedThreadPool();
    /**
     * <p>Task's executor, it should be well-initialization with resource, means you can call {@code executor.execute()
     * directly, the task doesn't take charge of initializing}</p>
     */
    private BlockingQueue<Executor<?>> executors = new LinkedBlockingQueue<>(maxExecutor);

    /**
     * <p>The field is to record if the task has been finished rightly, This 'finished' means all of the executors return with no wrong and exceptions.
     * Even if there is one executor throws exception when {@code execute()}, the field will stay false.</p>
     */
    private volatile AtomicBoolean done = new AtomicBoolean(false);

    /**
     * <p>The field represents if the task is alive, alive means you can dynamically add executors into it, and will it meets
     * {@link }, or a executor throws exception, it will be closed.</p>
     */
    private volatile AtomicBoolean closed = new AtomicBoolean(false);

    /**
     *
     */
    private Queue<Future<?>> futures = new ConcurrentLinkedQueue<>();

    /**
     *
     */
    private List<Object> results = new LinkedList<>();

    /**
     * <p>Return if the task has finished, if it's in main thread, it is always true,
     * if it is in another thread, it will return true when the thread is returned-safely,
     * and if the thread get exceptions and exit, or is handling work it will return false means
     * you can't trust this task's result and the task will recover itself.</p>
     *
     * @return
     */
    public Boolean isDone() {
        return done.get();
    }

    public Boolean isClosed() {
        return closed.get();
    }

    /**
     * @return
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
                    continue;
                }
                Executor<?> executor = executors.take();
                // else execute mission
                Future<?> future = pool.submit(executor);
                // put the future into future queue
                futures.offer(future);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();

        } finally {
            pool.shutdown();
        }
        return null;
    }

    @Override
    public List<Object> postProcessForExecute(Object result) {
        // loop to check if all mission have completed
        for (; ; ) {
            Future<?> future = futures.poll();
            if (future == null) {
                compareAndSetDone(false, true);
                return results;
            }
            // get the result and put it into result list
            try {
                Object re = future.get();
                results.add(re);
                return results;
            } catch (InterruptedException | ExecutionException e) {
                // todo
            }

        }
    }

    /**
     * <p>Dynamically append a executor in an task, if the queue is full, the thread will be hang up.</p>
     *
     * @param executor
     * @return
     */
    public Boolean appendExecutor(Executor<?> executor) {
        if (!isClosed()) {
            if (isDone()) {
                compareAndSetDone(true, false);
            }
            return executors.offer(executor);
        }
        return false;
    }

    public void close() {
        if (!isClosed()) {
            compareAndSetClosed(false, true);
        }
    }

    public List<Object> get() {
        if(!isClosed() || !isDone()) {
            return null;
        }
        return results;
    }

    /**
     * <p>Atomically update the isDone field</p>
     *
     * @param expect the expect value, if the current value equals it, the field will be update
     * @param update the new value.
     * @return if update is success
     */
    private void compareAndSetDone(Boolean expect, Boolean update) {
        done.compareAndSet(expect, update);
    }

    /**
     * <p>Atomically update the isDone field</p>
     *
     * @param expect the expect value, if the current value equals it, the field will be update
     * @param update the new value.
     * @return if update is success
     */
    private void compareAndSetClosed(Boolean expect, Boolean update) {
        closed.compareAndSet(expect, update);
    }
}
