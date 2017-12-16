package com.frame.asychronous;

/**
 * Created by fdh on 2017/11/23.
 */

/**
 * <p>The presentation of an error that happens in an asynchronous action</p>
 */
public interface Error {

    Future<?,? extends Comparable> getFuture();

    Throwable getCause();

    String getDescription();

}
