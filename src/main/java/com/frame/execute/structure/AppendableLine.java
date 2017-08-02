package com.frame.execute.structure;

import com.frame.execute.Executor;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;

/**
 * Created by fdh on 2017/7/29.
 */
public class AppendableLine<P> extends DynamicAppendableFlow<P, P> {



    class Process {
        /**
         *
         */
        Executor<P,P> worker;
        /**
         *
         */
        Executor<P,P> nextWorker;
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
    private BlockingQueue<Process> processors = new LinkedBlockingQueue<>();

    /**
     * <p>Hold the line thread in order to interrupt this thread</p>
     */
    private Thread lineThread;


    @Override
    public void prepareForExecute() {
        this.lineThread = Thread.currentThread();
        if(isClosed()) {
            compareAndSetClosed(true,false);
        }
        if(isDone()) {
            compareAndSetDone(true,false);
        }
    }

    @Override
    protected Object exec() throws Exception {
        if(isClosed()) {
            return production;
        }

        if(isDone()) {
            LockSupport.park();
        }

        Process processor = processors.take();
        P originalProduction = processor.worker.setProduction(production);


    }

    @Override
    public P postProcessForExecute(Object result) {
        return null;
    }

    @Override
    public void appendExecutor(Executor<P, ?> worker) {
        return;
    }


    @Override
    public void close() {

    }
}
