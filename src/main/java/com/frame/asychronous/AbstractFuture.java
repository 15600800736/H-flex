package com.frame.asychronous;


import com.frame.enums.asynchronous.FutureEvent;

import java.util.*;
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

    /**
     * The main task of this future represents.
     */
    private Callable<V> callable;

    /**
     * Represents if the task has been done successfully
     */
    private boolean success = false;

    /**
     * Represents if the task has failed, should be opposite of the {@code success}
     */
    private boolean failed = !success;

    /**
     * Represents if the task has done, no matter it's successful or failed
     */
    private boolean done = false;

    /**
     * Represents if the task has been cancelled, when the task has been cancelled,
     * the {@code done, success, failed} will all be false while the {@code cancelled} is true.
     */
    private boolean cancelled = false;

    /**
     * The error that the task throws when being executed.If the {@code success == true}, then
     * the {@code error} will be null
     */
    private Error error;

    /**
     * The listeners registered into the future, divided by event, and the key is for the ordered of listeners
     */
    private final Map<FutureEvent, Map<K,FutureListener<K,?>>> listeners = new HashMap<>(256);

    /**
     * The listeners of all events, it will be noticed after each event in {@link FutureEvent} complete, and the
     * all-event-listener will be noticed after all of the specific event listener invoked.
     */
    private final List<FutureListener<K,?>> allEventListener = new LinkedList<>();

    /**
     * The lock is used for synchronize the add operation.
     */
    private Lock addListenerLock = new ReentrantLock();


    @Override
    public Callable<V> callable() {
        return callable;
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
            if (key == null) {
                throw new NullPointerException("key is null");
            }
            Map<K,  FutureListener<K,?>> orderedListeners = listeners.get(event);
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
        allEventListener.add(listener);
        return null;
    }

    protected abstract K makeKey(FutureListener<K, ?> listener);
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
    public boolean isSuccess() {
        return success;
    }

    @Override
    public boolean isFailed() {
        return failed;
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
