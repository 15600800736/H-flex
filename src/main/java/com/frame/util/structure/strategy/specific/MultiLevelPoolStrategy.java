package com.frame.util.structure.strategy.specific;

import com.frame.util.structure.pool.Pool;

/**
 * Created by fdh on 2017/10/31.
 */
public abstract class MultiLevelPoolStrategy<V> extends PoolStrategy<V> {
    public abstract void transmitStrategy();
}
