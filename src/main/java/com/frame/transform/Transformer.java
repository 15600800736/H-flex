package com.frame.transform;

/**
 * Created by fdh on 2017/7/4.
 */
@FunctionalInterface
public interface Transformer<T> {
    T transform();
}
