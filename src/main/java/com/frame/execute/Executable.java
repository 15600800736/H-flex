package com.frame.execute;

/**
 * Created by fdh on 2017/7/29.
 */

/**
 * <p>The executable is a abstraction of everything which can do something and return something</p>
 * @param <T>
 */
@FunctionalInterface
public interface Executable<T> {
    T execute() throws Exception;
}
