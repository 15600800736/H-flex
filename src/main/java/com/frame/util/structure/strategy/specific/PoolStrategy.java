package com.frame.util.structure.strategy.specific;

import com.frame.traits.Addable;
import com.frame.util.structure.pool.Pool;
import com.frame.util.structure.strategy.Strategy;

/**
 * Created by fdh on 2017/9/30.
 */

public abstract class PoolStrategy<K,V> extends Strategy<Pool>
        implements Addable<K,V> {


    public abstract void sizeControlStrategy();

    public abstract void rejectionStrategy();

    public abstract void weedOutStrategy();

    public abstract void addStrategy(K key, V val);

    public abstract void getMethodStrategy(K key);
}
