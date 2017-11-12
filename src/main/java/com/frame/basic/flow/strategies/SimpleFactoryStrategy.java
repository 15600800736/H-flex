package com.frame.basic.flow.strategies;

import com.frame.execute.Executor;
import com.frame.basic.flow.flows.AppendableLine;
import com.frame.basic.flow.flows.ReusableTask;

/**
 * Created by fdh on 2017/8/24.
 */
public class SimpleFactoryStrategy<P> extends FactoryStrategy<P> {

    /**
     *
     */
    private AppendableLine<P> appendableLine;

    private int productionCache = 16;

    @Override
    public AppendableLine<P> getLine() {
        return appendableLine;
    }

    @Override
    protected ReusableTask<P> getTask() {
        return new ReusableTask<>();
    }

    @Override
    public void buildLine(Executor<P, ?>[][] executors, P production) {
        appendableLine = new AppendableLine<>(executors.length, productionCache);
        appendableLine.appendProduction(production);
        for (Executor<P, ?>[] executor : executors) {
            ReusableTask<P> task = getTask();
            for (Executor<P, ?> ex : executor) {
                task.appendWorker(ex);
            }
            appendableLine.appendWorker(task);
        }
    }
}
