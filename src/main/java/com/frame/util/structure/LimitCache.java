package com.frame.util.structure;

/**
 * Created by fdh on 2017/10/4.
 */
public interface LimitCache<E> {
    int getCapacity();

    void weedOutElement();

    int weedOutElementIndex();
}
