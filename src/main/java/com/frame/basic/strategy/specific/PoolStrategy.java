package com.frame.basic.strategy.specific;

import com.frame.traits.Addable;
import com.frame.basic.pool.Pool;
import com.frame.basic.strategy.Strategy;

/**
 * Created by fdh on 2017/9/30.
 */

public abstract class PoolStrategy<V> extends Strategy<Pool>
        implements Addable<String,V> {

    public abstract void sizeControlStrategy();

    public abstract void rejectionStrategy();

    public abstract void weedOutStrategy();

    public abstract void addStrategy(String key, V val);

    public abstract void removeStrategy(String key);

    public abstract void getDataStrategy(String key);

}
