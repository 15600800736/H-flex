package com.frame.flow;

import com.frame.flow.strategies.SimpleFactoryStrategy;

/**
 * Created by fdh on 2017/8/25.
 */
public class SimpleFactory<P> extends FlowFactory<P> {
    protected SimpleFactory() {
        super();
        this.strategy = new SimpleFactoryStrategy<>();
    }
}
