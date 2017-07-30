package com.frame.execute.structure;

import com.frame.execute.Executor;

import java.util.concurrent.CyclicBarrier;

/**
 * Created by fdh on 2017/7/29.
 */
public abstract class DynamicAppendableFlow<P,T> extends Flow<P,T> {


    public DynamicAppendableFlow() {
    }

    public DynamicAppendableFlow(P production) {
        super(production);
    }

    public DynamicAppendableFlow(CyclicBarrier barrier, P production) {
        super(barrier, production);
    }
    /**
     * <p>Dynamically append a executor in an task, if the queue is full, the thread will return false. and if the task has been closed,
     * return false, too</p>
     *
     * @param executor the executor to add
     * @return
     */
    public abstract Boolean appendExecutor(Executor<P, ?> executor);

    /**
     * <p>Inject the task's production into the executor if the executor's production isn't the task's and return the executor's production</p>
     * @param production
     * @param executor
     * @return
     */
    protected P injectProduction(P production, Executor<P,?> executor) {
        P exProduction = executor.getProduction();
        if(exProduction != production) {
            executor.setProduction(production);
        }
        return exProduction;
    }

}
