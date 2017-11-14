package com.frame.basic.strategy.specific.state;

import com.frame.basic.state.State;
import com.frame.basic.strategy.Strategy;

/**
 * Created by fdh on 2017/11/14.
 */
public abstract class ConcurrentTransformationStrategy<C, T> extends Strategy<State<C, T>> {

    private State<C, T> state;

    public ConcurrentTransformationStrategy(State<C, T> state) {
        this.state = state;
    }

    public abstract void transFailStrategy(C from, C to);

    @Override
    public State<C, T> getTarget() {
        return null;
    }

    @Override
    public void setTarget(State<C, T> target) {

    }
}
