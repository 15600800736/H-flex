package com.frame.execute;

import com.frame.context.resource.Resource;

import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by fdh on 2017/7/17.
 */
public abstract class Executor<T>
        implements Callable<T>,Executable<T> {

    protected CyclicBarrier barrier;
    public Executor() {

    }

    public Executor(CyclicBarrier barrier) {
        this.barrier = barrier;
    }

    public void setBarrier(CyclicBarrier barrier) {
        this.barrier = barrier;
    }

    /**
     * <p>The main method in Executor, it will be called after {@code prepareForExecutor} and before {@code postProcessForExecute}.
     * Sub-classes should overwritten this method to implements your own logic</p>
     * @return
     * @throws Exception
     */
    protected abstract Object exec() throws Exception;

    /**
     * the entrance of executor, default implementation give you a chance to prepare and post-process
     * @return
     * @throws Exception
     */
    public final T execute() throws Exception {
        try {
            prepareForExecute();
            Object rawResult = exec();
            return postProcessForExecute(rawResult);
        } finally {
            if(this.barrier != null) {
                this.barrier.await();
            }
        }

    }
    /**
     * <p>Default call {@code execute}</p>
     * @return
     * @throws Exception
     */
    @Override
    public T call() throws Exception {
        return execute();
    }

    /**
     * <p>Prepare the environment for execute, default implementation is empty</p>
     */
    public void prepareForExecute() {

    }

    /**
     * process the result of {@code execute}, default implementation is type-transforming.
     * @param result the result of {@code execute()}
     * @return
     */
    public T postProcessForExecute(Object result) {
        return (T)result;
    }

    /**
     * Get the resources that the executor holds.
     * @return
     */
    public Resource[] getResources() {
        return null;
    }
}
