package com.frame.util.structure;

/**
 * Created by fdh on 2017/10/4.
 */
public interface LimitableCache<E> {
    int getCacheSize();

    void weedOutElement();

    int weedOutElementIndex();
}
