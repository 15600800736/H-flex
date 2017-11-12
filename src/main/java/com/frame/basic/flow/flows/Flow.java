package com.frame.basic.flow.flows;


import com.frame.execute.Executor;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by fdh on 2017/7/29.
 */
public abstract class Flow<P,T> extends Executor<P,T> {

    public Flow() {
    }

    public Flow(P production) {
        super(production);
    }


    /**
     * <p>The field is to record if the flow has been finished rightly, This 'finished' means all of the executors return with no wrong and exceptions.
     * Even if there is one executor throws exception when {@code execute()}, the field will stay false.</p>
     */
    protected volatile AtomicBoolean done = new AtomicBoolean(false);

    /**
     * <p>The field represents if the flow is alive, alive means you can dynamically add executors into it,
     * when you call {@code close()}, or a executor throws exception, it will be closed.</p>
     */
    protected volatile AtomicBoolean closed = new AtomicBoolean(false);

    /**
     * <p>The filed represents if the flow has started, As for how to start a flow, it will be defined by sub-class.</p>
     */
    protected volatile AtomicBoolean started = new AtomicBoolean(false);

    /**
     * <p>Atomically update the isDone field</p>
     *
     * @param expect the expect value, if the current value equals it, the field will be update
     * @param update the new value.
     * @return if update is success
     */
    protected void compareAndSetDone(Boolean expect, Boolean update) {
        done.compareAndSet(expect, update);
    }

    /**
     * <p>Atomically update the isDone field</p>
     *
     * @param expect the expect value, if the current value equals it, the field will be update
     * @param update the new value.
     * @return if update is success
     */
    protected void compareAndSetClosed(Boolean expect, Boolean update) {
        closed.compareAndSet(expect, update);
    }

    protected void compareAndSetStarted(Boolean expect, Boolean update) {
        started.compareAndSet(expect, update);
    }
    public Boolean isClosed() {
        return closed.get();
    }

    public Boolean isDone() {
        return done.get();
    }

    public Boolean isStarted() {
        return started.get();
    }

    public abstract void close();
}
