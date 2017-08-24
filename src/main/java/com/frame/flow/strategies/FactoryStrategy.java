package com.frame.flow.strategies;

import com.frame.execute.Executor;
import com.frame.flow.flows.AppendableLine;
import com.frame.flow.flows.ReusableTask;

/**
 * Created by fdh on 2017/8/24.
 */
public abstract class FactoryStrategy<P> {

    public abstract AppendableLine<P> getLine();

    protected abstract ReusableTask<P> getTask();

    public abstract void buildLine(Executor<P,P>[] executors);
}
