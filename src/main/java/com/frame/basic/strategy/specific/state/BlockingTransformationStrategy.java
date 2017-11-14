package com.frame.basic.strategy.specific.state;

import com.frame.basic.state.State;

/**
 * Created by fdh on 2017/11/14.
 */
public class BlockingTransformationStrategy<C, T> extends ConcurrentTransformationStrategy<C, T> {


    public BlockingTransformationStrategy(State<C, T> state) {
        super(state);
    }

    @Override
    public void transFailStrategy(C from, C to) {
        if (!this.target.getRawCurrentState().compareAndSet(from, to)) {

        }
    }
}
