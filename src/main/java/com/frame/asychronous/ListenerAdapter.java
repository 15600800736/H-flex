package com.frame.asychronous;

import com.frame.enums.asynchronous.FutureEventType;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by fdh on 2017/11/23.
 */
public abstract class ListenerAdapter<K, T> implements Listener<K, T> {

    private final List<Filter> filters = new LinkedList<>();

    private volatile FutureEventType event;

    @Override
    public void registerFilter(Filter... filters) {

    }

    @Override
    public Listener<K, T> registerFilter(Filter filter) {
        return null;
    }

    @Override
    public void setKey(K key) {

    }

    @Override
    public K getKey() {
        return null;
    }


}
