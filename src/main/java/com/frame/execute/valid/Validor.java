package com.frame.execute.valid;

import com.frame.execute.Executor;

/**
 * Created by fdh on 2017/7/4.
 */
public abstract class Validor implements Executor<Boolean> {
    protected String validable;

    public Validor(String validable) {
        this.validable = validable;
    }
}
