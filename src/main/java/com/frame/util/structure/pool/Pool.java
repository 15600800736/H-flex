package com.frame.util.structure.pool;

import com.frame.traits.Addable;
import com.frame.util.structure.LimitableCache;
import com.frame.util.structure.strategy.specific.PoolStrategy;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by fdh on 2017/9/29.
 */
public abstract class Pool<E>
        implements LimitableCache<E>, Addable<String, E> {

    /**
     * capacity of pool
     */
    protected int capacity;

    /**
     * strategy mode
     */
    protected PoolStrategy<E> poolStrategy;

    /**
     * store elements
     */
    protected Map<String, E> pool;

    /**
     * @param capacity
     * @param poolStrategy
     */
    public Pool(int capacity, PoolStrategy poolStrategy) {
        this.poolStrategy = poolStrategy;
        this.pool = new ConcurrentHashMap<>(capacity * 2);
        this.poolStrategy = poolStrategy;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getSize() {
        return this.pool.size();
    }

    public void add(String key, E elements) {
        this.poolStrategy.add(key, elements);
    }

    public void setPoolStrategy(PoolStrategy poolStrategy) {
        this.poolStrategy = poolStrategy;
    }

    public E remove(String key,int level) {
        return this.pool.remove(key);
    }
}
