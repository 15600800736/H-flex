package com.frame.execute;

/**
 * Created by fdh on 2017/7/17.
 */
public interface Executor<T> {
    T execute() throws Exception;
    void prepareForExecute();
    void postProcessForExccute();
}
