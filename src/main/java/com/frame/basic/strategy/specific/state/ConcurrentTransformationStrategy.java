package com.frame.basic.strategy.specific.state;

import com.frame.basic.state.State;
import com.frame.basic.state.role.Monitor;
import com.frame.basic.strategy.Strategy;

/**
 * Created by fdh on 2017/11/14.
 */
public abstract class ConcurrentTransformationStrategy<C> extends Strategy<Monitor<C>> {

    private Monitor<C> monitor;

    public abstract void transFailStrategy(C expectState);

    public abstract void notificationStrategy(C expectState);

    public ConcurrentTransformationStrategy(Monitor<C> monitor) {
        this.monitor = monitor;
    }

    @Override
    public Monitor<C> getTarget() {
        return monitor;
    }

    @Override
    public void setTarget(Monitor<C> target) {
        this.monitor = target;
    }


}
