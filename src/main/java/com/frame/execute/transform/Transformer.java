package com.frame.execute.transform;

import com.frame.execute.Executor;

import java.util.concurrent.CyclicBarrier;

/**
 * Created by fdh on 2017/7/4.
 */
public abstract class Transformer<T> extends Executor<T> {

    public Transformer() {
    }

    public Transformer(CyclicBarrier barrier) {
        super(barrier);
    }
}
