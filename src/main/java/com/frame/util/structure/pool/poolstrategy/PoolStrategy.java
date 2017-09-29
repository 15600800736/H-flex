package com.frame.util.structure.pool.poolstrategy;

/**
 * Created by fdh on 2017/9/29.
 */
public abstract class PoolStrategy<K,V> {

    public abstract void add(K key, V val);
    public abstract V get(K key);
    public abstract void expand();
    public abstract boolean needExpand();
    protected abstract void weedOut();
}
