package com.frame.execute.process;

import com.frame.context.resource.Resource;

/**
 * Created by fdh on 2017/7/14.
 */
public class DefaultProcessor<T> extends Processor<T> {
    @Override
    public T execute() {
        return null;
    }
    @Override
    public void prepareForExecute() {

    }

    @Override
    public void postProcessForExceute() {

    }

    @Override
    public Resource[] getResources() {
        return null;
    }
}
