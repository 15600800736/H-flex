package com.frame.execute;

import com.frame.context.resource.Resource;

import java.util.concurrent.Callable;

/**
 * Created by fdh on 2017/7/17.
 */
public interface Executor<T> extends Callable<T> {
    /**
     * <p>The main method in Executor, it will be called after {@code prepareForExecutor} and before {@code postProcessForExecute}.
     * Sub-classes should overwritten this method to implements your own logic</p>
     * @return
     * @throws Exception
     */
    Object exec() throws Exception;

    /**
     * the entrance of executor, default implementation give you a chance to prepare and post-process
     * @return
     * @throws Exception
     */
    default T execute() throws Exception {
        prepareForExecute();
        Object result = exec();
        return postProcessForExecute(result);
    }
    /**
     * <p>Default call {@code execute}</p>
     * @return
     * @throws Exception
     */
    @Override
    default T call() throws Exception {
        return execute();
    }

    /**
     * <p>Prepare the environment for execute, default implementation is empty</p>
     */
    default void prepareForExecute() {

    }

    /**
     * process the result of {@code execute}, default implementation is type-transforming.
     * @param result the result of {@code execute()}
     * @return
     */
    default T postProcessForExecute(Object result) {
        return (T)result;
    }

    /**
     * Get the resources that the executor holds.
     * @return
     */
    default Resource[] getResources() {
        return null;
    }
}
