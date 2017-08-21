package com.frame.execute.valid;

import com.frame.execute.Executor;

import java.util.concurrent.CyclicBarrier;

/**
 * Created by fdh on 2017/7/4.
 */
public abstract class Validor extends Executor<String, Boolean> {
    public Validor() {
    }

    public Validor(String production) {
        super(production);
    }

}

