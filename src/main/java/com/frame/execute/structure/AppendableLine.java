package com.frame.execute.structure;

import com.frame.execute.Executor;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

/**
 * Created by fdh on 2017/7/29.
 */
public class AppendableLine<P> extends Flow<BlockingQueue<P>, List<P>> {

    /**
     * <p>A process is a abstraction of a point in a line of factory, like a Node, it has worker and the next one.
     * When the next process is null, means there are no more process, and the line will be hanged up until some thread close
     * the line.</p>
     */
    protected class Process implements Runnable {
        /**
         * <p>The unit of execute to do work</p>
         */
        WorkerInfo worker;
        /**
         * <p>Next step, if null, means there is no more</p>
         */
        Process nextProcessor;

        /**
         *
         */
        Thread currentThread;

        @Override
        public void run() {
            this.currentThread = Thread.currentThread();
            for (; ; ) {
                try {
                    P production = worker.productionCache.take();
                    injectProduction(worker.worker, production);
                    P finishedProduction = worker.worker.execute();
                    // if this is last processor pushing production into tail processor, check if done(There are some mistakes here)
                    nextProcessor.worker.addProdution(finishedProduction);
                    if (this == lastProcessor) {
                        LockSupport.unpark(lineThread);
                    }

                    // pushing production into next worker.
                } catch (Exception e) {
                    if (e instanceof InterruptedException) {
                        return;
                    }
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * <p>The worker pair holds some information about the worker, like where he is and what he deals with</p>
     */
    protected class WorkerInfo {
        /**
         * <p>The main part of the worker information</p>
         */
        Executor<P, P> worker;
        /**
         * <p>The index of the worker in this line</p>
         */
        Integer position;
        /**
         * <p>The productions that is waiting to be processed</p>
         */
        BlockingQueue<P> productionCache;

        Boolean needMax = false;

        /**
         * @param worker
         * @param position
         */
        public WorkerInfo(Executor<P, P> worker, Integer position) {

            if (productionCacheSize == null) {
                productionCache = new LinkedBlockingQueue<>();
            } else {
                productionCache = new LinkedBlockingQueue<>(productionCacheSize);
            }
            this.worker = worker;
            this.position = position;
        }


        private WorkerInfo(Executor<P, P> worker, Integer position, Boolean needMax) {
            if (needMax || productionCacheSize == null) {
                productionCache = new LinkedBlockingQueue<>();
            } else {
                productionCache = new LinkedBlockingQueue<>(productionCacheSize);
            }
            this.worker = worker;
            this.position = position;
            this.needMax = needMax;
        }

        /**
         * <p>add a production to the production queue.</p>
         *
         * @param production
         */
        public void addProdution(P production) {
            try {
                this.productionCache.put(production);
            } catch (InterruptedException e) {
                System.out.println("here!here!");
                // todo
            }
        }
    }

    /**
     * <p>how many workers in this line</p>
     */
    private int workerNum = 16;

    /**
     *
     */
    public Process firstProcessor;

    /**
     *
     */
    public Process lastProcessor = firstProcessor;

    /**
     * <p>Hold the line thread in order to interrupt this thread</p>
     */
    private Thread lineThread;

    /**
     *
     */
    private ExecutorService pool = Executors.newFixedThreadPool(workerNum);

    /**
     *
     */
    private Integer productionCacheSize = 16;

    /**
     *
     */
    private Process headerProcessor;

    /**
     *
     */
    private Process tailProcessor;

    /**
     *
     */
    private AtomicInteger countOfProduction = new AtomicInteger(0);

    /**
     * Constructors
     */

    public AppendableLine() {
        this.production = new LinkedBlockingQueue<>(productionCacheSize);
    }

    public AppendableLine(int productionCacheSize) {
        this.productionCacheSize = productionCacheSize;
        this.production = new LinkedBlockingQueue<>(productionCacheSize);
    }

    public AppendableLine(int workerNum, int productionCacheSize) {
        this.workerNum = workerNum;
        this.productionCacheSize = productionCacheSize;
        this.production = new LinkedBlockingQueue<>(productionCacheSize);
    }





    @Override
    public void prepareForExecute() {
        this.lineThread = Thread.currentThread();
        createHeaderProcessor();
        createTailProcessor();
        // start to inject production into first processor;
        if(headerProcessor != null) {
            pool.submit(headerProcessor.worker.worker);
        }

    }

    @Override
    protected Object exec() throws Exception {
        try {
            if (!isStarted()) {
                compareAndSetStarted(false, true);
            }
            Process currentProcessor = firstProcessor;

            while (currentProcessor != tailProcessor) {
                if (!pool.isShutdown()) {
                    pool.submit(currentProcessor);
                }
                currentProcessor = currentProcessor.nextProcessor;
            }
            for (; ; ) {
                if (isDone()) {
                    if (isClosed()) {
                        return tailProcessor.worker.productionCache;
                    } else {
                        LockSupport.park();
                    }
                }
                if (countOfProduction.compareAndSet(tailProcessor.worker.productionCache.size(), 0)) {
                    if (!isDone()) {
                        compareAndSetDone(false, true);
                    }
                } else {
                    LockSupport.parkNanos(1000000000L);
                }
            }
        } finally {
            pool.shutdownNow();
        }
    }

    @Override
    public List<P> postProcessForExecute(Object result) {
        BlockingQueue<P> finishedProductions = (BlockingQueue<P>) result;
        List<P> finished = new LinkedList<>();
        for (; ; ) {
            P product = finishedProductions.poll();
            if (product == null) {
                return finished;
            }
            finished.add(product);
        }
    }


    public void appendProduction(P production) {
        if (isClosed()) {
            return;
        }
        try {
            this.production.put(production);
            countOfProduction.incrementAndGet();
            if (isDone()) {
                compareAndSetDone(true, false);
            }
        } catch (InterruptedException e) {
            // todo
            e.printStackTrace();
        }
    }

    public AppendableLine appendWorker(Executor<P, P> worker) {
        // if the line has started, you can't add worker into the line.
        if (isStarted()) {
            return this;
        }
        // if this is the first time adding processor
        if (firstProcessor == null) {
            Process process = new Process();
            process.worker = new WorkerInfo(worker, 1);
            firstProcessor = process;
            lastProcessor = process;
            return this;
        }

        if (lastProcessor == null) {
            lastProcessor = firstProcessor;
        }

        Process process = new Process();
        process.worker = new WorkerInfo(worker, lastProcessor.worker.position + 1);
        // append processor
        lastProcessor.nextProcessor = process;
        // update tail processor
        lastProcessor = lastProcessor.nextProcessor;
        return this;
    }

    @Override
    public void close() {
        if (!isClosed()) {
            compareAndSetClosed(false, true);
            LockSupport.unpark(this.lineThread);
            for (Process p = headerProcessor; p != tailProcessor; p = p.nextProcessor) {
                if (p.currentThread != null) {
                    // double check
                    if (!isClosed()) {
                        compareAndSetClosed(false, true);
                    }
                    p.currentThread.interrupt();
                }
            }
        }
    }

    public P get() {
        return tailProcessor.worker.productionCache.poll();
    }

    /**
     *
     */
    private void createHeaderProcessor() {
        if (firstProcessor == null || headerProcessor != null) {
            return;
        }

        headerProcessor = new Process();
        headerProcessor.nextProcessor = firstProcessor;
        headerProcessor.worker = new WorkerInfo(new Executor<P, P>() {
            @Override
            protected Object exec() throws Exception {
                headerProcessor.currentThread = Thread.currentThread();
                for (; ; ) {
                    P production = AppendableLine.this.production.take();
                    if (headerProcessor.nextProcessor != null && headerProcessor.nextProcessor.worker != null) {
                        headerProcessor.nextProcessor.worker.addProdution(production);
                    }
                }
            }
        },
                0);
    }

    private void createTailProcessor() {
        if (lastProcessor == null || tailProcessor != null) {
            return;
        }

        tailProcessor = new Process();
        // the tail processor should have infinity production cache
        tailProcessor.worker = new WorkerInfo(null, lastProcessor.worker.position + 1, true);
        lastProcessor.nextProcessor = tailProcessor;
    }

    protected P injectProduction(Executor<P, P> worker, P production) {
        P originalProduction = worker.getProduction();
        worker.setProduction(production);
        return originalProduction;
    }


//    static class Test {
//
//        Map<Integer, String> map1 = new ConcurrentHashMap<>(2);
//
//        Map<Integer, String> map2 = new ConcurrentHashMap<>(2);
//    }

//    public static void main(String... strings) throws Exception {
//        AppendableLine<Test> line = new AppendableLine<>(100, 100);
//        ReusableTask<Test> task1 = new ReusableTask<>(2);
//        task1.appendWorker(new Executor<Test, Object>() {
//            @Override
//            protected Object exec() throws Exception {
//                this.production.map1.putIfAbsent(1,"Processed by task 1 worker 1");
//                return "Processed by task 1 worker 1";
//            }
//        });
//        task1.appendWorker(new Executor<Test, Object>() {
//
//            @Override
//            protected Object exec() throws Exception {
//                this.production.map2.putIfAbsent(2,"Processed by task 1 worker 2");
//                return "Processed by task 1 worker 2";
//            }
//        });
//
//        ReusableTask<Test> task2 = new ReusableTask<>(2);
//        task2.appendWorker(new Executor<Test, Object>() {
//            @Override
//            protected Object exec() throws Exception {
//                this.production.map1.putIfAbsent(3,"Processed by task 2 worker 1");
//                return "Processed by task 2 worker 1";
//            }
//        });
//        task2.appendWorker(new Executor<Test, Object>() {
//            @Override
//            protected Object exec() throws Exception {
//                this.production.map2.putIfAbsent(4,"Processed by task 2 worker 2");
//                return "Processed by task 2 worker 2";
//            }
//        });
//        line.appendWorker(task1).appendWorker(task2);
//        Thread exe = new Thread(() -> {
//            try {
//                line.execute();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//        exe.start();
//        for (int i = 0; i < 3; i++) {
//            Test test = new Test();
//            line.appendProduction(test);
//        }
//
//        for (; ; ) {
//            if(line.isDone()) {
//                Test test = line.get();
//                if(test == null) {
//                    break;
//                }
//                test.map1.entrySet().forEach(System.out::println);
//                test.map2.entrySet().forEach(System.out::println);
//                System.out.println("_____________________");
//            }
//        }
//        line.close();
//    }
}
