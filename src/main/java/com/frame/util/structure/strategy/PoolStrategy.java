package com.frame.util.structure.strategy;

import com.frame.traits.Addable;
import com.frame.util.structure.pool.Pool;

/**
 * Created by fdh on 2017/9/30.
 */

public abstract class PoolStrategy<K,V> extends Strategy<Pool>
                implements Addable<K,V> {

    public PoolStrategy(Pool target) {
        super(target);
    }

    protected abstract void sizeControlStrategy();

    protected abstract void rejectionStrategy();

    protected abstract void addStrategy();


}
