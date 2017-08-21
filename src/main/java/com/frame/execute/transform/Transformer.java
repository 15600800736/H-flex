package com.frame.execute.transform;

import com.frame.execute.Executor;

import java.util.concurrent.CyclicBarrier;

/**
 * Created by fdh on 2017/7/4.
 */
public abstract class Transformer<P,T> extends Executor<P,T> {
    public Transformer() {
    }

    public Transformer(P production) {
        super(production);
    }
}
