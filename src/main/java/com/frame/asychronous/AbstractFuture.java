package com.frame.asychronous;


import com.frame.enums.asynchronous.FutureEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by fdh on 2017/12/15.
 */
public abstract class AbstractFuture<V, K extends Comparable<K>> implements Future<V, K> {

    protected Callable<V> callable;

    protected Boolean success;

    protected Boolean failed = !success;

    protected Error error;

    protected final Map<FutureEvent, Map<K,FutureListener<?,?>>> listeners = new HashMap<>(256);

    private Lock addListenerLock = new ReentrantLock();


    @Override
    public Callable<V> callable() {
        return callable;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public boolean isFailed() {
        return failed;
    }

    @Override
    public Error error() {
        return error;
    }

    @Override
    public K addListener(FutureListener<K, ?> listener) {
        K key = listener.getKey();
        FutureEvent event = listener.getEvent();
        if (event == null) {
            return registerListenerToAllEvents(listener);
        }
        addListenerLock.lock();
        try {
            if (key == null) {
                key = makeKey(listener);
            }
            Map<K,  FutureListener<?,?>> orderedListeners = listeners.get(event);
            if (orderedListeners == null) {
                orderedListeners = new TreeMap<>();
            }
            orderedListeners.put(key, listener);
        } finally {
            addListenerLock.unlock();
        }
        return key;
    }

    private K registerListenerToAllEvents(FutureListener<K, ?> listener) {

    }

    private K makeKey(FutureListener<K, ?> listener) {
        FutureEvent event = listener.getEvent();
        if (event == null) {
            throw new NullPointerException("event is null");
        }
        Map<K, FutureListener<?,?>> orderedListeners = this.listeners.get(event);
        
        // todo make key
        return null;
    }
    @Override
    public List<FutureListener<?, ?>> listeners() {
        return null;
    }

    @Override
    public void waitUntilDone() {

    }

    @Override
    public void waitUntil(long sec) {

    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        return null;
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }
}
