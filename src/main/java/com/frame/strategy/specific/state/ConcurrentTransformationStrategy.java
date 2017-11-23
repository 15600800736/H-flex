package com.frame.strategy.specific.state;

import com.frame.state.role.Monitor;
import com.frame.strategy.Strategy;

/**
 * Created by fdh on 2017/11/14.
 */
public abstract class ConcurrentTransformationStrategy<C> extends Strategy<Monitor> {

    private Monitor monitor;

    public abstract void transFailStrategy(C expectState);

    public abstract void notificationStrategy(C expectState, Thread t);

    public ConcurrentTransformationStrategy(Monitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public Monitor getTarget() {
        return monitor;
    }

    @Override
    public void setTarget(Monitor target) {
        this.monitor = target;
    }


}
