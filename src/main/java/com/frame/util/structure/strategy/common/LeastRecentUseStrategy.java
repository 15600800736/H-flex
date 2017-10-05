package com.frame.util.structure.strategy.common;

import com.frame.util.structure.strategy.Strategy;

import java.security.Key;
import java.util.Collection;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by fdh on 2017/10/4.
 */
public class LeastRecentUseStrategy<K> extends Strategy<Collection<K>> {

    @Override
    public Collection<K> getTarget() {
        return null;
    }

    @Override
    public void setTarget(Collection<K> target) {

    }
}
