package com.frame.asychronous;

/**
 * Created by fdh on 2017/11/23.
 */
public interface Listener<K,T> {

    void registerFilter(Filter...filters);

    Listener<K,T> registerFilter(Filter filter);

    void setKey(K key);

    K getKey();

    T callback();

}
