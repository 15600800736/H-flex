package com.frame.execute;

import com.frame.context.resource.Resource;

import java.util.concurrent.Callable;

/**
 * Created by fdh on 2017/7/17.
 */
public interface Executor<T> extends Callable<T> {
    /**
     * <p>The main method in Executor, it will be called after {@code prepareForExecutor} and before {@code postProcessForExecute}</p>
     * @return
     * @throws Exception
     */
    T execute() throws Exception;

    /**
     * 
     * @return
     * @throws Exception
     */
    @Override
    default T call() throws Exception {
        return execute();
    }

    void prepareForExecute();
    void postProcessForExceute();
    Resource[] getResources();
}
