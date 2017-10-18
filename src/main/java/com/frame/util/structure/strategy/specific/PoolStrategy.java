package com.frame.util.structure.strategy.specific;

import com.frame.traits.Addable;
import com.frame.util.structure.pool.Pool;
import com.frame.util.structure.strategy.Strategy;

/**
 * Created by fdh on 2017/9/30.
 */

public abstract class PoolStrategy<K,V> extends Strategy<Pool>
        implements Addable<K,V> {

    protected abstract void sizeControlStrategy();

    protected abstract void rejectionStrategy();

    protected abstract void weedOutStrategy();

    protected abstract void addStrategy(K key, V val);

    protected abstract void removeStrategy(K key);

    protected abstract void getDataStrategy(K key);

}
