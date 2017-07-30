package com.frame.execute.structure;

import com.frame.execute.Executor;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * Created by fdh on 2017/7/29.
 */
public class AppendableLine<P> extends DynamicAppendableFlow<P, P> {
    protected class ExecutorPair {
        Executor<P, ?> executor;
        P production;
        Integer position;
        public ExecutorPair(Executor<P, ?> executor, P production, Integer position) {
            this.executor = executor;
            this.production = production;
            this.position = position;
        }
    }
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
        this.lineThread = Thread.currentThread();
        if(isClosed()) {
            compareAndSetClosed(true,false);
        }
        if(isDone()) {
            compareAndSetDone(true,false);
        }
    }

    @Override
    protected Object exec() throws Exception {
        if(isClosed() && isDone()) {
            
        }
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
