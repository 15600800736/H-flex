package com.frame.strategy;

/**
 * Created by fdh on 2017/9/29.
 */

/**
 * @param <T> type of target of the strategy
 */
public abstract class Strategy<T> {

    protected T target;
    public abstract T getTarget();
    public abstract void setTarget(T target);

}
