package com.frame.asychronous;

import com.frame.enums.asynchronous.FutureEvent;
import com.frame.exceptions.CastException;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by fdh on 2017/11/23.
 */
public abstract class ListenerAdapter<K, T> implements FutureListener<K, T> {

    protected ListenerFilter preFilter;

    protected ListenerFilter postFilter;

    protected volatile FutureEvent event;

    protected K key;

    @Override
    public void registerFilter(Filter filter) throws CastException {
        if (filter instanceof ListenerFilter) {
            this.preFilter = (ListenerFilter) filter;
        } else {
            throw new CastException(filter.getClass(), ListenerFilter.class);
        }
    }

    @Override
    public void setKey(K key) {
        this.key = key;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public final T callback() throws Exception {
        if (preFilter != null) {
            preFilter.execute();
        }

        T result = exec();

        if (postFilter != null) {
            postFilter.execute();
        }
        return result;
    }

    protected abstract T exec();

    @Override
    public FutureEvent getEvent() {
        return event;
    }

    @Override
    public void setEvent(FutureEvent event) {
        this.event = event;
    }
}
