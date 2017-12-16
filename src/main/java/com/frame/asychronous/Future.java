package com.frame.asychronous;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * Created by fdh on 2017/11/23.
 */
public interface Future<V, K extends Comparable<K>> extends java.util.concurrent.Future<V> {

    Callable<V> callable();

    boolean isSuccess();

    boolean isFailed();

    Error error();

    K addListener(FutureListener<K,?> listener);

    List<FutureListener<?,?>> listeners();

    void waitUntilDone();

    void waitUntil(long sec);

}
