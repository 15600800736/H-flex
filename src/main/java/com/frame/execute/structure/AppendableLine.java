package com.frame.execute.structure;

import com.frame.execute.Executor;

import java.util.Deque;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by fdh on 2017/7/29.
 */
public class AppendableLine<P> extends Flow<BlockingQueue<P>, List<P>> {



    private class PassWorker extends Executor<P,P> {

        @Override
        protected Object exec() throws Exception {
            Process header = processors.peek();
            if(header == null) {
                return null;
            }
            for ( ; ;) {
                if(isClosed()) {
                    return null;
                }
                P production = AppendableLine.this.production.take();
                header.worker.addProdution(production);
            }
        }
    }
    /**
     * <p>A process is a abstraction of a point in a line of factory, like a Node, it has worker and the next one.
     * When the next process is null, means there are no more process, and the line will be hanged up until some thread close
     * the line.</p>
     */
    class Process {
        /**
         * <p>The unit of execute to do work</p>
         */
        WorkerInfo worker;
        /**
         * <p>Next step, if null, means there is no more</p>
         */
        Process nextProcessor;
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

            if(productionCacheSize == null) {
                productionCache = new LinkedBlockingQueue<>();
            } else {
                productionCache = new LinkedBlockingQueue<>(productionCacheSize);
            }
            this.worker = worker;
            this.position = position;
        }

        /**
         * <p>add a production to the production queue.</p>
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
    private BlockingDeque<Process> processors = new LinkedBlockingDeque<>();

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
    private Lock processorLock = new ReentrantLock();

    /**
     *
     */
    private Integer productionCacheSize = 16;

    private PassWorker passWorker = new PassWorker();
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
            compareAndSetStarted(true,false);
        }

        // start to take production from production queue.
        pool.submit(passWorker);
    }

    @Override
    protected Object exec() throws Exception {
        if(!isStarted()) {
            compareAndSetStarted(false,true);
        }
        for (; ; ) {
            // if the line has closed, return immediately, no matter if the line has finished its work
            if (isClosed()) {
                return production;
            }

            // if all production has been processed.
            if (isDone()) {
                LockSupport.park();
            }
            Executor<P, P> worker = currentProcessor.worker;
            P originalProduction = injectProduction(production, worker);
            if (worker != null) {
                Future<P> future = pool.submit(worker);
                try {
                    this.production = future.get();
                } catch (InterruptedException e) {

                }
                worker.setProduction(originalProduction);
                Process next = currentProcessor.nextProcessor;
                if (next != null) {
                    proccessorLock.lock();
                    this.currentProcessor = next;
                    proccessorLock.unlock();
                } else {
                    if (!isDone()) {
                        this.currentProcessor.set(this, null);
                        compareAndSetDone(false, true);
                    }
                }
            }
        }
    }

    @Override
    public List<P> postProcessForExecute(Object result) {
        return (List<P>) result;
    }


    public void appendProduction(P production) {
        Process processor = new Process();
        Process tail = processors.pollLast();

        // add the processor
        processors.add(processor);

        // update the relationship
        // means the queue is empty
        if (tail != null) {
            if (tail.nextProcessor == null) {
                tail.nextProcessor = processor;
                // means the thread has been hanged on
                if (isDone()) {

                    compareAndSetDone(true, false);
                    LockSupport.unpark(lineThread);
                }
            }
        }
    }

    public void appendWorker(Executor<P,P> worker) {

    }
    @Override
    public void close() {

    }
}
