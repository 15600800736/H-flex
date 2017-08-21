package com.frame.execute.structure;

import com.frame.execute.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

/**
 * Created by fdh on 2017/8/16.
 */
public class ReusableTask<P> extends Flow<P, P> {
    protected class NamedThreadFactory implements ThreadFactory {
        ThreadFactory tf = Executors.defaultThreadFactory();
        public NamedThreadFactory() {

        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = tf.newThread(r);
            t.setName("task's worker thread");
            return t;
        }
    }
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
    private ExecutorService pool = Executors.newCachedThreadPool(new NamedThreadFactory());
    /**
     * <p>AppendableTask's workers, the production will be passed into the workers and let them process it</p>
     */
    private List<Executor<P, ?>> workers = new ArrayList<>(maxWorker);


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
        if (pool.isShutdown()) {
            pool = Executors.newCachedThreadPool();
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
            compareAndSetStarted(false, true);
        }
        // loop to take mission and execute
        for (; ; ) {
            if (isClosed()) {
                return System.currentTimeMillis();
            }
            // else execute mission
            for (Executor<P, ?> worker : workers) {
                P originalProduction = injectProduction(production, worker);
                Future<?> future = pool.submit(worker);
                // cache the worker's own production
                productionCache.putIfAbsent(future, new WorkerInfo(worker, originalProduction));
                // put the future into future queue
                futures.offer(future);
            }
            processResult();
            close();
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
        if (!isStarted()) {
            workers.add(worker);
        }
    }


    public ReusableTask() {
    }


    public ReusableTask(int maxWorker) {
        this.maxWorker = maxWorker;
    }

    public ReusableTask(P production, int maxWorker) {
        super(production);
        this.maxWorker = maxWorker;
    }

    public ReusableTask(CyclicBarrier barrier, P production, int maxWorker) {
        super(production);
        this.maxWorker = maxWorker;
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

    public static void main(String[] args) {
        ReusableTask<Map<String, Integer>> reusableTask = new ReusableTask<>(10);
        reusableTask.setProduction(new ConcurrentHashMap<>(10));
        for (int i = 0; i < 10; i++) {
            int j = i;
            reusableTask.appendWorker(new Executor<Map<String, Integer>, Object>() {
                @Override
                protected Object exec() throws Exception {
                    this.production.putIfAbsent(String.valueOf(j), j);
                    return j;
                }
            });
        }

        Thread exe = new Thread(() -> {
            try {
                reusableTask.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        exe.start();
        for (; ; ) {
            if (reusableTask.isDone()) {
                reusableTask.getProduction().entrySet().forEach(System.out::println);
                break;
            }
        }

        System.out.println(reusableTask.isClosed());

    }
}


