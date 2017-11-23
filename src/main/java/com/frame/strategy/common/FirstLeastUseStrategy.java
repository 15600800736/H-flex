package com.frame.strategy.common;

import com.frame.strategy.Strategy;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by fdh on 2017/10/4.
 */
public class FirstLeastUseStrategy<K> extends Strategy<Collection<K>> {

    private Map<K, Integer> useMap = new ConcurrentHashMap<>(target.size(), 1);

    @Override
    public Collection<K> getTarget() {
        return target;
    }

    @Override
    public void setTarget(Collection<K> target) {
        this.target = target;
    }

    public K findLeastUseElement() {
        int least = Integer.MAX_VALUE;
        for (Integer u : useMap.values()) {
            least = u < least ? u : least;
        }
        for (Map.Entry<K, Integer> entry : useMap.entrySet()) {
            if (entry.getValue() == least) {
                return entry.getKey();
            }
        }
        return null;
    }

    public void askForData(K key) {
        Integer usage = useMap.get(key);
        if (usage == null) {
            // if the cache is full, then remove the least use
            if (useMap.size() == target.size()) {
                useMap.remove(findLeastUseElement());
            }
            useMap.put(key, 1);
        } else {
            useMap.put(key, usage + 1);
        }
    }



}
