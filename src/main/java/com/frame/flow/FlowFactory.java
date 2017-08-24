package com.frame.flow;

import com.frame.execute.Executor;
import com.frame.flow.flows.AppendableLine;
import com.frame.flow.strategies.FactoryStrategy;

/**
 * Created by fdh on 2017/8/24.
 */
public abstract class FlowFactory<P> {

    private FlowFactory() {

    }

    private FactoryStrategy<P> strategy;

    public AppendableLine<P> getLine() {
        return strategy.getLine();
    }


    public void setExecutors(Executor<P,P>[][] executors, P production) {
        strategy.buildLine(executors, production);
    }
}
