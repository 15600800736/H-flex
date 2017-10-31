package com.frame.util.structure.pool;

import com.frame.util.structure.strategy.specific.PoolStrategy;

/**
 * Created by fdh on 2017/10/31.
 */
public abstract class MultiLevelPool<E> extends Pool<E> {

    private Pool<E> deeperPool;

    /**
     * @param capacity
     * @param poolStrategy
     */
    public MultiLevelPool(int capacity, PoolStrategy poolStrategy, Pool<E> deeperPool) {
        super(capacity, poolStrategy);
        this.deeperPool = deeperPool;
    }

    public Pool<E> getDeeperPool() {
        return this.deeperPool;
    }
}
