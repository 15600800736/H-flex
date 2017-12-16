package com.frame.asychronous;

import com.frame.enums.asynchronous.FutureEvent;

/**
 * Created by fdh on 2017/12/15.
 */
public interface FutureListener<K, T> extends Listener<K, T> {
    FutureEvent getEvent();
    void setEvent(FutureEvent event);
}
