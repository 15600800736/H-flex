package com.frame.util.structure.strategy;

import com.frame.util.structure.State;
import com.frame.util.structure.pool.Pool;
import com.frame.util.structure.strategy.common.FirstLeastUseStrategy;
import com.frame.util.structure.strategy.specific.MultiLevelPoolStrategy;
import com.frame.util.structure.strategy.specific.PoolStrategy;

/**
 * Created by fdh on 2017/10/5.
 */
public class PoolLeastRecentUseStrategy<V> extends MultiLevelPoolStrategy<V> {

    private FirstLeastUseStrategy<String> strategy = new FirstLeastUseStrategy<>();
    private int countOfLack = 0;
    private int countToResize;
    private int countToTransmit;
    private State state;

    public PoolLeastRecentUseStrategy(int countToResize, int countToTransmit) {
        this.countToResize = countToResize;
        this.countToTransmit = countToTransmit;
    }

    @Override
    public void add(String key, V val) {
        // pre-check
        sizeControlStrategy();
        weedOutStrategy();
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
        if (this.countOfLack > countToResize) {

        }
    }

    @Override
    public void rejectionStrategy() {

    }

    @Override
    public void weedOutStrategy() {
        String key = strategy.findLeastUseElement();
        removeStrategy(key);
        transmitStrategy();
    }

    @Override
    public void addStrategy(String key, V val) {

    }

    @Override
    public void removeStrategy(String key) {

    }

    @Override
    public void getDataStrategy(String key) {
        this.target.remove(key);
    }
    @Override
    public void transmitStrategy() {

    }
}
