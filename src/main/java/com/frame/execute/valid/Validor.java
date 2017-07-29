package com.frame.execute.valid;

import com.frame.execute.Executor;

import java.util.concurrent.CyclicBarrier;

/**
 * Created by fdh on 2017/7/4.
 */
public abstract class Validor extends Executor<Boolean> {
    protected String validable;

    public Validor(String validable) {
        this.validable = validable;
    }

    public Validor(CyclicBarrier barrier, String validable) {
        super(barrier);
        this.validable = validable;
    }
}

