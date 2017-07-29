package com.frame.execute;

/**
 * Created by fdh on 2017/7/29.
 */
@FunctionalInterface
public interface Executable<T> {
    T execute() throws Exception;
}
