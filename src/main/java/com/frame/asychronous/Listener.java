package com.frame.asychronous;

import com.frame.exceptions.CastException;

/**
 * Created by fdh on 2017/11/23.
 */
public interface Listener<K,T> {

    void registerFilter(Filter filters) throws CastException;

    void setKey(K key);

    K getKey();

    T callback() throws Exception;
}
