package com.frame.util.structure.strategy;

import com.frame.util.structure.pool.Pool;
import com.frame.util.structure.strategy.common.FirstLeastUseStrategy;
import com.frame.util.structure.strategy.specific.PoolStrategy;

/**
 * Created by fdh on 2017/10/5.
 */
public class PoolLeastRecentUseStrategy<V> extends PoolStrategy<V> {

    private FirstLeastUseStrategy<String> strategy = new FirstLeastUseStrategy<>();

    @Override
    public void add(String key, V val) {
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
    public void sizeControlStrategy() {

    }

    @Override
    public void rejectionStrategy() {

    }

    @Override
    public void weedOutStrategy(int level) {
        String key = strategy.findLeastUseElement();
        this.target.remove(key, level);
    }

    @Override
    public void addStrategy(String key, V val) {
        sizeControlStrategy();
        // if the pool is full,
        if (this.target.getSize() == this.target.getCapacity()) {

        }
    }

    @Override
    public void removeStrategy(String key) {

    }

    @Override
    public void getDataStrategy(String key) {

    }
}
