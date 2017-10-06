package com.frame.util.structure.strategy.common;

import com.frame.util.structure.strategy.Strategy;

import java.security.Key;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by fdh on 2017/10/4.
 */
public class LeastUseStrategy<K> extends Strategy<Collection<K>> {

    private Map<K, Integer> useMap = new ConcurrentHashMap<>(target.size(),1);

    public LeastUseStrategy() {
        target.forEach(key -> useMap.put(key,0));
    }

    @Override
    public Collection<K> getTarget() {
        return null;
    }

    @Override
    public void setTarget(Collection<K> target) {

    }

    private int findLeastUseElement() {
        return 0;
    }

}
