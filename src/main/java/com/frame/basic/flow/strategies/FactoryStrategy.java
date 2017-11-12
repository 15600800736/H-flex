package com.frame.basic.flow.strategies;

import com.frame.execute.Executor;
import com.frame.basic.flow.flows.AppendableLine;
import com.frame.basic.flow.flows.ReusableTask;

/**
 * Created by fdh on 2017/8/24.
 */
public abstract class FactoryStrategy<P> {

    public abstract AppendableLine<P> getLine();

    protected abstract ReusableTask<P> getTask();

    public abstract void buildLine(Executor<P,?>[][] executors, P production);
}
