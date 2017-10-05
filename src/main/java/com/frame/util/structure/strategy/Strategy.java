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

    protected O target;
    public abstract O getTarget();
    public abstract void setTarget(O target);

}
