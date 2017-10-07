package com.frame.flow.flows;

import com.frame.execute.Executor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by fdh on 2017/10/6.
 */
public class SingleProductionLine<P> extends Flow<P,P> {


    /**
     * <p>Represent if the line should stop</p>
     */
    private boolean isClosed;
    /**
     * <p>Used for interrupting</p>
     */
    private Thread currentThread;
    /**
     * <p>The executor's queue, each of the executors appended will be executed only once</p>
     */
    private BlockingQueue<Process> blockingQueue = new LinkedBlockingQueue<>();

    public SingleProductionLine(P production) {
        super(production);
        this.currentThread = Thread.currentThread();
    }

    protected class Process {
        /**
         * <p>The main of the process</p>
         */
        WorkerInfo worker;

        /**
         * <p>net processor</p>
         */
        Process nextProcessor;

    }

    protected class WorkerInfo {
        /**
         * <p>The execute unit</p>
         */
        Executor<P,P> worker;

        /**
         * <p>The order of the worker</p>
         */
        int position;
    }
    @Override
    public void close() {

    }

    @Override
    protected Object exec() throws Exception {
        while(true) {
            Process process = blockingQueue.take();
        }


    }

    public void appendWorker(Executor<P,P> worker) {

    }
}
