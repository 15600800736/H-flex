package com.frame.context;

import com.frame.execute.EndExecutor;
import com.frame.execute.Executor;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by fdh on 2017/7/26.
 */

/**
 * <p>Task is a chain of executor, it has a {@link Executor} queue in it and will execute them by their order,
 * task will take executors util it meets the EndExecutor, so all of executors behind an EndExecutor will be ignore
 * because the task has been closed, you can call {@code isDone()} to check if you can put a new {@link Executor} in it.
 * When all of the executor finish their work, the task will atomically change its {@code isDone} to true, which means you
 * cannot put a new {@link Executor}</p>
 */
public class Task {
    /**
     *
     */
    private int maxExecutor = 10;
    /**
     *
     */
    private final ExecutorService pool = Executors.newSingleThreadExecutor();
    /**
     * <p>Task's executor, it should be well-initialization with resource</p>
     */
    private BlockingQueue<Executor<?>> executors = new LinkedBlockingQueue<>(maxExecutor);

    /**
     *
     */
    private volatile AtomicBoolean isDone = new AtomicBoolean(false);

    /**
     * <p>Return if the task has finished, if it's in main thread, it is always true,
     * if it is in another thread, it will return true when the thread is returned-safely,
     * and if the thread get exceptions and exit, or is handling work it will return false means
     * you can't trust this task's result and the task will recover itself.</p>
     *
     * @return
     */
    public Boolean isDone() {
        return isDone.get();
    }

    /**
     * @return
     */
    public void execute() {
        try {
            for (; ; ) {
                Executor<?> executor = executors.take();
                if (executor instanceof EndExecutor) {
                    compareAndSetIsDone(false,true);
                    return;
                }
                executor.prepareForExecute();
                Future<?> future = pool.submit(executor);
                future.get();
                executor.postProcessForExceute();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            pool.shutdown();
        }
    }

    public Boolean appendExecutor(Executor<?> executor) {
        return executors.offer(executor);
    }

    private Boolean compareAndSetIsDone(Boolean expect, Boolean update) {
        return isDone.compareAndSet(expect, update);
    }

    public void setMaxExecutor(int maxExecutor) {
        this.maxExecutor = maxExecutor;
    }




}
