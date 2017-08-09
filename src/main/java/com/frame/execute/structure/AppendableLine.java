package com.frame.execute.structure;

import com.frame.execute.Executor;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

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
                    // if there is no more production to process,which means two situation
                    if (worker.productionCache.isEmpty()) {
                        // if the line has closed, which means there won't be any production, return,
                        // or the thread will wait on the method take().
                        if (isClosed()) {
                            return;
                        }
                    }
                    P production = worker.productionCache.take();
                    injectProduction(worker.worker, production);
                    P finishedProduction = worker.worker.execute();
                    // if this is last processor pushing production into tail processor, check if done(There are some mistakes here)
                    if (this == lastProcessor) {
                        LockSupport.unpark(lineThread);
                    }

                    // pushing production into next worker.
                    nextProcessor.worker.addProdution(finishedProduction);
                } catch (Exception e) {
                    if (e instanceof InterruptedException) {
                        return;
                    }
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

    public AppendableLine(CyclicBarrier barrier, BlockingQueue<P> production) {
        super(barrier, production);
        this.production = new LinkedBlockingQueue<>(productionCacheSize);
    }

    public AppendableLine(Integer productionCacheSize) {
        this.productionCacheSize = productionCacheSize;
        this.production = new LinkedBlockingQueue<>(productionCacheSize);
    }

    public AppendableLine(BlockingQueue<P> production, Integer productionCacheSize) {
        super(production);
        this.productionCacheSize = productionCacheSize;
        this.production = new LinkedBlockingQueue<>(productionCacheSize);
    }

    public AppendableLine(CyclicBarrier barrier, BlockingQueue<P> production, Integer productionCacheSize) {
        super(barrier, production);
        this.productionCacheSize = productionCacheSize;
        this.production = new LinkedBlockingQueue<>(productionCacheSize);
    }

    public AppendableLine(int workerNum) {
        this.workerNum = workerNum;
        this.production = new LinkedBlockingQueue<>(productionCacheSize);
    }

    public AppendableLine(BlockingQueue<P> production, int workerNum) {
        super(production);
        this.workerNum = workerNum;
        this.production = new LinkedBlockingQueue<>(productionCacheSize);
    }

    public AppendableLine(CyclicBarrier barrier, BlockingQueue<P> production, int workerNum) {
        super(barrier, production);
        this.workerNum = workerNum;
        this.production = new LinkedBlockingQueue<>(productionCacheSize);
    }

    public AppendableLine(int workerNum, Integer productionCacheSize) {
        this.workerNum = workerNum;
        this.productionCacheSize = productionCacheSize;
        this.production = new LinkedBlockingQueue<>(productionCacheSize);
    }

    public AppendableLine(BlockingQueue<P> production, int workerNum, Integer productionCacheSize) {
        super(production);
        this.workerNum = workerNum;
        this.productionCacheSize = productionCacheSize;
        this.production = new LinkedBlockingQueue<>(productionCacheSize);
    }

    public AppendableLine(CyclicBarrier barrier, BlockingQueue<P> production, int workerNum, Integer productionCacheSize) {
        super(barrier, production);
        this.workerNum = workerNum;
        this.productionCacheSize = productionCacheSize;
        this.production = new LinkedBlockingQueue<>(productionCacheSize);
    }

    @Override
    public void prepareForExecute() {
        this.lineThread = Thread.currentThread();
        if (isClosed()) {
            compareAndSetClosed(true, false);
        }
        if (isDone()) {
            compareAndSetDone(true, false);
        }
        if (isStarted()) {
            compareAndSetStarted(true, false);
        }
        createHeaderProcessor();
        createTailProcessor();
        // start to inject production into first processor;
        pool.submit(headerProcessor.worker.worker);

    }

    @Override
    protected Object exec() throws Exception {
        try {
            if (!isStarted()) {
                compareAndSetStarted(false, true);
            }
            Process currentProcessor = firstProcessor;

            while (currentProcessor != tailProcessor) {
                if(!pool.isShutdown()) {
                    pool.submit(currentProcessor);
                }
                currentProcessor = currentProcessor.nextProcessor;

            }
            for (; ; ) {
                if (tailProcessor.worker.productionCache.size() == countOfProduction.get()) {
                    if (!isDone()) {
                        compareAndSetDone(false, true);
                    }
                    if (isClosed()) {
                        System.out.println("size " + tailProcessor.worker.productionCache.size());
                        return tailProcessor.worker.productionCache;
                    }
                }
                LockSupport.park();
            }
        } finally {
            pool.shutdown();
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

    public Boolean appendWorker(Executor<P, P> worker) {
        // if the line has started, you can't add worker into the line.
        if (isStarted()) {
            return false;
        }
        // if this is the first time adding processor
        if (firstProcessor == null) {
            Process process = new Process();
            process.worker = new WorkerInfo(worker, 1);
            firstProcessor = process;
            lastProcessor = process;
            return true;
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
        return true;
    }

    @Override
    public void close() {
        try {
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
        } finally {
            pool.shutdown();
        }
    }

    public P get() {
        System.out.println(tailProcessor.worker.productionCache.size());
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
                    if (isClosed()) {
                        return this.production;
                    }
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


    public static void main(String... strings) throws InterruptedException {
        AppendableLine<Integer> line = new AppendableLine<>(100, 100);
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            Executor<Integer, Integer> executor = new Executor<Integer, Integer>() {
                @Override
                protected Object exec() throws Exception {
                    this.production += finalI;
                    Thread.sleep(10L);
                    return this.production;
                }
            };

            line.appendWorker(executor);
        }
        List<Integer> result = new LinkedList<>();
        Thread t = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    line.appendProduction(i);
                }
                List<Integer> re = line.execute();
                result.addAll(re);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.start();
        Thread.sleep(1000);
        line.close();
        //            line.close();
//            Thread.sleep(100);
//            Assert.assertTrue(line.isStarted());
//            Assert.assertTrue(line.isDone());
//            Assert.assertTrue(line.isClosed());
    }
//        AppendableLine<Integer> line = new AppendableLine<>(100, 500);
//        for (int i = 0; i < 10; i++) {
//            int finalI = i;
//            Executor<Integer, Integer> executor = new Executor<Integer, Integer>() {
//                @Override
//                protected Object exec() throws Exception {
//                    this.production += finalI;
//                    Thread.sleep(1L);
//                    return this.production;
//                }
//            };
//
//            line.appendWorker(executor);
//        }
//        System.out.println("isStart" + line.isStarted());
//
//        Thread t = new Thread(() -> {
//            try {
//                for(int i = 1; i < 500; i++) {
//                    line.appendProduction(i);
//                }
//                line.execute();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//        t.start();
//        Thread.sleep(1L);
//        System.out.println("isStart" + line.isStarted());
//        System.out.println("isClosed" + line.isClosed());
//        System.out.println("isDone" + line.isDone());
//
//        Thread.sleep(1000L);
//        System.out.println("isDone" + line.isDone());
//        line.close();
//        Thread.sleep(1L);
//        System.out.println("isClose" + line.isClosed());

}
