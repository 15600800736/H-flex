package com.frame.execute.structure;

import com.frame.execute.Executor;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by fdh on 2017/7/29.
 */
public class AppendableLine<P> extends DynamicAppendableFlow<P, P> {
    /**
     *
     */
    private int maxExecutor = 10;
    /**
     *
     */
    private BlockingQueue<Executor<P, ?>> executors = new LinkedBlockingQueue<>(maxExecutor);
    /**
     *
     */
    private ExecutorService pool = Executors.newSingleThreadExecutor();

    /**
     * <p>The Queue is used for storing futures</p>
     */
    private Queue<Future<?>> futures = new ConcurrentLinkedQueue<>();

    /**
     * <p>The list is used for storing every executor's result</p>
     */
    private List<Object> results = new LinkedList<>();

    /**
     * <p>Hold the line thread in order to interrupt this thread</p>
     */
    private Thread lineThread;

    @Override
    public void prepareForExecute() {
        lineThread = Thread.currentThread();
    }

    @Override
    protected Object exec() throws Exception {

        return null;
    }

    @Override
    public P postProcessForExecute(Object result) {
        return null;
    }

    @Override
    public Boolean appendExecutor(Executor<P, ?> executor) {
        return null;
    }


    @Override
    public void close() {

    }
}
