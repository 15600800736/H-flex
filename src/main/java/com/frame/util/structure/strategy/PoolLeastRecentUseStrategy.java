package com.frame.util.structure.strategy;

import com.frame.util.structure.pool.Pool;
import com.frame.util.structure.strategy.common.FirstLeastUseStrategy;
import com.frame.util.structure.strategy.specific.PoolStrategy;

/**
 * Created by fdh on 2017/10/5.
 */
public class PoolLeastRecentUseStrategy<K,V> extends PoolStrategy<K,V> {

    private FirstLeastUseStrategy<K> strategy = new FirstLeastUseStrategy<>();

    @Override
    public void add(K key, V val) {
        // pre-check
        addStrategy(key,val);
        // after-process
    }

    @Override
    public Pool getTarget() {
        return target;
    }

    @Override
    public void setTarget(Pool target) {
        this.target = target;
    }

    @Override
    protected void sizeControlStrategy() {

    }

    @Override
    protected void rejectionStrategy() {

    }

    @Override
    protected void weedOutStrategy() {
        K key = strategy.findLeastUseElement();

    }

    @Override
    protected void addStrategy(K key, V val) {

    }

    @Override
    protected void removeStrategy(K key) {

    }

    @Override
    protected void getDataStrategy(K key) {

    }
}
