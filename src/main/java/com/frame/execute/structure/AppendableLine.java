package com.frame.execute.structure;

import com.frame.execute.Executor;

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
    class Process implements Runnable {
        /**
         * <p>The unit of execute to do work</p>
         */
        WorkerInfo worker;
        /**
         * <p>Next step, if null, means there is no more</p>
         */
        Process nextProcessor;

        @Override
        public void run() {
            for (; ; ) {
                if (isClosed()) {
                    return;
                }
                try {
                    P production = worker.productionCache.take();
                    injectProduction(worker.worker, production);
                    P finishedProduction = worker.worker.execute();
                    nextProcessor.worker.addProdution(finishedProduction);
                } catch (Exception e) {
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

        public WorkerInfo(Executor<P, P> worker, Integer position) {

            if (productionCacheSize == null) {
                productionCache = new LinkedBlockingQueue<>();
            } else {
                productionCache = new LinkedBlockingQueue<>(productionCacheSize);
            }
            this.worker = worker;
            this.position = position;
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
                System.out.println("interrupt when add production");
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
    Process firstProcessor;

    /**
     *
     */
    Process lastProcessor = firstProcessor;

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
        if (!isStarted()) {
            compareAndSetStarted(false, true);
        }
        Process currentProcessor = firstProcessor;
        while (currentProcessor != tailProcessor) {
            pool.submit(currentProcessor);
            currentProcessor = currentProcessor.nextProcessor;
        }

        for (; ; ) {
            if (tailProcessor.worker.productionCache.size() == countOfProduction.get()) {
                if (!isDone()) {
                    compareAndSetDone(false, true);
                    if (isClosed()) {
                        return tailProcessor.worker.productionCache;
                    }
                }
            }
            LockSupport.park();
        }
    }

    @Override
    public List<P> postProcessForExecute(Object result) {
        BlockingQueue<P> finishedProductions = (BlockingQueue<P>) result;
        List<P> finished = new LinkedList<>();
        for (; ; ) {
            if(finishedProductions.poll() == null) {
                return finished;
            }
            P product = finishedProductions.poll();
            finished.add(product);
        }
    }


    public void appendProduction(P production) {

    }

    public Boolean appendWorker(Executor<P, P> worker) {
        // if the line has started, you can't add worker into the line.
        if(isStarted()) {
            return false;
        }
        // if this is the first time adding processor
        if(firstProcessor == null) {
            Process process = new Process();
            process.worker = new WorkerInfo(worker, 1);
            firstProcessor = process;
            tailProcessor = process;
            return true;
        }

        if(tailProcessor == null) {
            tailProcessor = firstProcessor;
        }

        Process process = new Process();
        process.worker = new WorkerInfo(worker, tailProcessor.worker.position + 1);
        tailProcessor.nextProcessor = process;
        tailProcessor = tailProcessor.nextProcessor;
        return true;
    }

    @Override
    public void close() {

    }

    public P get() {
        try {
            return tailProcessor.worker.productionCache.take();
        } catch (InterruptedException e) {
            // todo
            System.out.println("interrupt when get");
            return null;
        }
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
        tailProcessor.worker = new WorkerInfo(null, lastProcessor.worker.position + 1);
        lastProcessor.nextProcessor = tailProcessor;
    }

    protected P injectProduction(Executor<P, P> worker, P production) {
        P originalProduction = worker.getProduction();
        worker.setProduction(production);
        return originalProduction;
    }


}
