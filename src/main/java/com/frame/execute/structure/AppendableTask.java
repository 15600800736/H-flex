package com.frame.execute.structure;

import com.frame.execute.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * Created by fdh on 2017/7/26.
 */

/**
 * <p>AppendableTask is unit of work, it's an special {@link Executor}, which can let a series of worker work concurrently.
 * Every task is for a specific production, so what it receives is what it gives, because a task is used for processing an
 * production like filling it, which means just make a production more useful, but won't change it to another type.
 * The state of task is determined by two field, {@code done} and {@code closed}, you could check them with {@code isDone(); isClosed();}.
 * <ol>
 * <li>done -> if isDone() returns true, it means that all of the workers in the task has finished their work, and the
 * task has been hang up, but it doesn't mean the task's work has finished, because you can add a new {@link Executor} into the task
 * with {@code appendExecutor(Executor worker)}, and the task will start itself and continue to do the new work.</li>
 * <li>closed -> if isClosed() returns true, it means you cannot add a new worker into the task, but it doesn't mean the task has been stop,
 * because it will continue to do the rest work until all of the workers executed.</li>
 * </ol></p>
 * <p>Above all, if you want to check if the task is stop, you have to use {@code isDone() && isClosed()}. By the way, you can call {@code close()} to
 * forbid the other threads to add worker.
 * * If you want to get the workers' result, call {@code get()}.
 * * The task isn't suggested to run in the main thread, because it may be blocked.
 * You cannot run the task twice, because all of the workers has been removed ,like all workers are gone, so if you want it to repeat, you need add
 * them again.
 * </p>
 * <p>Note that the task's workers <em><b>cannot</b></em> change the production and it's basic type filed.  It can just process it, sSo as follow</p>
 * <pre>
 *
 * public static void main(String...strings) {
 *   Integer i = 0;
 *   AppendableTask<Integer> task = new AppendableTask(i,1);
 *   Executor<Test,Object> exe = new Executor(...do something with i like i++);
 *   task.appendExecutor(exe);
 *   Integer result = task.execute();
 *   System.out.println(result.i);
 * }}</pre>
 * And you will get a 0, because the task...
 */
public class AppendableTask<P> extends Flow<P, P> {

    protected class WorkerInfo {
        Executor<P, ?> worker;
        P production;

        public WorkerInfo(Executor<P, ?> worker, P production) {
            this.worker = worker;
            this.production = production;
        }
    }

    private Long startMillis = 0L;

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * <p>The capacity of blocking queue,when it is full, the thread that tries to put workers in it will be blocked. </p>
     */
    private int maxWorker = 16;
    /**
     * <p>The cache thread pool means the task can be acting concurrently</p>
     */
    private final ExecutorService pool = Executors.newCachedThreadPool();
    /**
     * <p>AppendableTask's workers, the production will be passed into the workers and let them process it</p>
     */
    private BlockingQueue<Executor<P, ?>> workers = new LinkedBlockingQueue<>(maxWorker);


    /**
     * <p>The Queue is used for storing futures</p>
     */
    private Queue<Future<?>> futures = new ConcurrentLinkedQueue<>();

    /**
     * <p>The list is used for storing every worker's result</p>
     */
    private Map<Executor<P, ?>, Object> results = new ConcurrentHashMap<>();


    /**
     * <p>Hold the thread that runs the current task to interrupt this thread.</p>
     */
    private Thread taskThread;

    /**
     * <p>The production cache is used for caching the workers' own productions, in order to return it when the worker complete its task</p>
     */
    private ConcurrentMap<Future<?>, WorkerInfo> productionCache = new ConcurrentHashMap<>();

    public AppendableTask() {
    }


    public AppendableTask(int maxWorker) {
        this.maxWorker = maxWorker;
    }

    public AppendableTask(P production, int maxWorker) {
        super(production);
        this.maxWorker = maxWorker;
    }

    public AppendableTask(CyclicBarrier barrier, P production, int maxWorker) {
        super(barrier, production);
        this.maxWorker = maxWorker;
    }


    /**
     * <p>reset the state of the task, inject the thread of the task</p>
     */
    @Override
    public void prepareForExecute() {
        this.taskThread = Thread.currentThread();
        this.startMillis = System.currentTimeMillis();
        if (isClosed()) {
            compareAndSetClosed(true, false);
        }
        if (isDone()) {
            compareAndSetDone(true, false);
        }
        if (isStarted()) {
            compareAndSetStarted(true, false);
        }
    }

    /**
     * -
     * <p>The method will loop taking an worker from the blocking queue and execute,
     * when the task is closed and no more workers can be taken, it will return and let the post-processing
     * deal the result.</p>
     *
     * @return The time of ending execute
     * @throws Exception any exceptions that can be thrown when execute
     */
    @Override
    public Object exec() throws Exception {
        if (!isStarted()) {
            compareAndSetStarted(false,true);
        }
        // loop to take mission and execute
        for (; ; ) {
            // if the task has already rejected the mission, and no more exist mission to take
            // stop take workers from queue and go to post-processing
            if (isClosed() && workers.isEmpty()) {
                return System.currentTimeMillis();
            }
            // if the queue is empty means there is no more worker to take for now, so the task will process the current result
            // the task will pause for now, so if you append a worker now, it won't execute immediately.
            if (workers.isEmpty() && results.isEmpty() && !futures.isEmpty()) {
                processResult();
            }
            // else execute mission
            try {
                Executor<P, ?> worker = workers.take();
                P originalProduction = injectProduction(production, worker);
                Future<?> future = pool.submit(worker);
                // cache the worker's own production
                productionCache.putIfAbsent(future, new WorkerInfo(worker, originalProduction));
                // put the future into future queue
                futures.offer(future);
            } catch (InterruptedException ignored) {
                if (isClosed()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("task has been closed.");
                    }
                }
            }
        }
    }


    /**
     * <p>Wait until all of the work finished and return the well-precessed prodution , storing every worker's result
     * at the same time.</p>
     *
     * @return the well-processed production
     */
    @Override
    public P postProcessForExecute(Object result) {
        try {
            processResult();
            return production;
        } finally {
            pool.shutdown();
        }
    }

    private void processResult() {
        // loop to check if all mission have completed
        for (int i = 0; ; i++) {
            Future<?> future = futures.poll();
            if (future == null) {
                if (!isDone()) {
                    compareAndSetDone(false, true);
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("The task takes" + String.valueOf(System.currentTimeMillis() - startMillis) + "ms");
                }
                return;
            }
            // get the result and put it into result list
            try {
                Object re = future.get();
                // complete the mission and give back the worker's production
                WorkerInfo workerInfo = productionCache.get(future);
                Executor<P, ?> worker = workerInfo.worker;
                P production = workerInfo.production;
                worker.setProduction(production);
                results.put(worker, re);
            } catch (InterruptedException | ExecutionException e) {
                System.out.println("got a interruptedException when process result, but we store it");
                while (!futures.add(future)) ;
            }
        }
    }


    /**
     * <p>Dynamically append a worker in an task, if the queue is full, the thread will return false. and if the task has been closed,
     * return false, too</p>
     *
     * @param worker the worker to add
     * @return
     */
    public void appendWorker(Executor<P, ?> worker) {
        if (!isClosed()) {
            if (isDone()) {
                compareAndSetDone(true, false);
            }
            try {
                workers.put(worker);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * <p>Inject the task's production into the executor if the executor's production isn't the task's and return the executor's production</p>
     *
     * @param production
     * @param executor
     * @return
     */
    protected P injectProduction(P production, Executor<P, ?> executor) {
        P exProduction = executor.getProduction();
        if (exProduction != production) {
            executor.setProduction(production);
        }
        return exProduction;
    }

    /**
     * <p>call this method to forbid any thread to add workers into this task</p>
     */
    @Override
    public void close() {
        if (!isClosed()) {
            compareAndSetClosed(false, true);
        }
        if (taskThread != null) {
            taskThread.interrupt();
        } else {
            // todo
        }
    }

    /**
     * <p>Get the results of each worker</p>
     *
     * @return
     */
    public Map<Executor<P, ?>, Object> getResults() {
        Map<Executor<P, ?>, Object> returnedResults = new ConcurrentHashMap<>(results);
        if (!isDone()) {
            return null;
        }
        results.clear();
        futures.clear();
        return returnedResults;
    }
}
