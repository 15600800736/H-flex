package com.frame.util.structure.strategy;

import com.frame.util.structure.pool.Pool;

/**
 * Created by fdh on 2017/9/29.
 */

/**
 *
 * @param <O>
 */
public abstract class Strategy<O> {
    /**
     *
     */
    O target;

    public Strategy(O target) {
        this.target = target;
    }

    public O getTarget() {
        return target;
    }

    public void setTarget(O target) {
        this.target = target;
    }

}
