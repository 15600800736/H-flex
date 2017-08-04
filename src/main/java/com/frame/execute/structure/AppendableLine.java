package com.frame.execute.structure;

import com.frame.execute.Executor;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.locks.LockSupport;

/**
 * Created by fdh on 2017/7/29.
 */
public class AppendableLine<P> extends DynamicAppendableFlow<P, P> {


    class Process {
        /**
         *
         */
        Executor<P, P> worker;
        /**
         *
         */
        Process nextProcessor;
    }

    /**
     *
     */
    protected class WorkerPair {

        /**
         *
         */
        Executor<P, P> worker;
        /**
         *
         */
        P production;
        /**
         *
         */
        Integer position;

        public WorkerPair(Executor<P, P> worker, P production, Integer position) {
            this.worker = worker;
            this.production = production;
            this.position = position;
        }
    }

    /**
     *
     */
    private int maxExecutor = 10;
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
    private ExecutorService pool = Executors.newSingleThreadExecutor();

    private AtomicReferenceFieldUpdater<AppendableLine<P>, Process> currentProcessor;

    @Override
    public void prepareForExecute() {
        this.lineThread = Thread.currentThread();
        try {
            this.currentProcessor.set(this,processors.take());
        } catch (InterruptedException e) {
            // todo
        }
        if (isClosed()) {
            compareAndSetClosed(true, false);
        }
        if (isDone()) {
            compareAndSetDone(true, false);
        }
    }

    @Override
    protected Object exec() throws Exception {
        for (; ; ) {
            // if the line has closed, return immediately, no matter if the line has finished its work
            if (isClosed()) {
                return production;
            }

            // if all
            if (isDone()) {
                LockSupport.park();
            }
            Executor<P, P> worker = currentProcessor.get(this).worker;
            P originalProduction = injectProduction(production, worker);
            if (worker != null) {
                Future<P> future = pool.submit(worker);
                try {
                    this.production = future.get();
                } catch (InterruptedException e) {

                }
                worker.setProduction(originalProduction);
                Process next = currentProcessor.get(this).nextProcessor;
                if (next != null) {
                    this.currentProcessor.set(this, next);
                } else {
                    if (!isDone()) {
                        this.currentProcessor.set(this,null);
                        compareAndSetDone(false, true);
                    }
                }
            }
        }
    }

    @Override
    public P postProcessForExecute(Object result) {
        return (P) result;
    }

    public void appendExecutor(Executor<P, P> worker) {
        Process processor = new Process();
        Process tail = processors.pollLast();

        // add the processor
        processors.add(processor);

        // update the relationship
        // means the queue is empty
        if(tail != null) {
            if(tail.nextProcessor == null) {
                tail.nextProcessor = processor;
                // means the thread has been hanged on
                if(isDone()) {

                    compareAndSetDone(true,false);
                    LockSupport.unpark(lineThread);
                }
            }
        }
    }

    @Override
    public void close() {

    }
}
