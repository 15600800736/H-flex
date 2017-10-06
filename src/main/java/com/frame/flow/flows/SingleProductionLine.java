package com.frame.flow.flows;

import com.frame.execute.Executor;

/**
 * Created by fdh on 2017/10/6.
 */
public class SingleProductionLine<P> extends Flow<P,P> {

    /**
     *
     */
    private Process headerProcessor = new Process();

    /**
     *
     */
    private Process tailProcessor = new Process();

    public SingleProductionLine(P production) {
        super(production);
        headerProcessor.nextProcessor = tailProcessor;
        headerProcessor.worker = new WorkerInfo();
        headerProcessor.worker.position = 0;
        tailProcessor.worker = new WorkerInfo();
        tailProcessor.worker.position = 1;
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
        Process processor = headerProcessor.nextProcessor;
        P production = this.production;

        while (processor != tailProcessor) {
            processor.worker.worker.setProduction(production);
            production = processor.worker.worker.execute();
            processor = processor.nextProcessor;
        }
        return production;
    }

    public void appendWorker(Executor<P,P> worker) {
        int position = this.tailProcessor.worker.position;
        Process newTail = new Process();
        newTail.worker = new WorkerInfo();
        newTail.worker.position = position + 1;
        this.tailProcessor.worker.worker = worker;
        this.tailProcessor.nextProcessor = newTail;
        this.tailProcessor = this.tailProcessor.nextProcessor;
    }
}
