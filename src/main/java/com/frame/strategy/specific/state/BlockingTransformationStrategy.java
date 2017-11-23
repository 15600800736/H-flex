package com.frame.strategy.specific.state;

import com.frame.state.role.Monitor;

/**
 * Created by fdh on 2017/11/14.
 */
public class BlockingTransformationStrategy<C> extends ConcurrentTransformationStrategy<C> {


    public BlockingTransformationStrategy(Monitor monitor) {
        super(monitor);
    }

    @Override
    public void transFailStrategy(C from) {

    }

    @Override
    public void notificationStrategy(C expectState, Thread t) {

    }
}
