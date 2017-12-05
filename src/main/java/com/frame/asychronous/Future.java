package com.frame.asychronous;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * Created by fdh on 2017/11/23.
 */
public interface Future<V> extends java.util.concurrent.Future<V> {

    Callable<V> callable();

    boolean isSuccess();

    boolean isFailed();

    Error error();

    <K, T> void addListener(Listener<K, T> listener);

    List<Listener<?,?>> listeners();


}
