package com.frame.execute.process;

/**
 * Created by fdh on 2017/7/14.
 */
public class DefaultProcessor implements Processor {
    @Override
    public Object process(Object o) {
        return o;
    }

    @Override
    public void prepareForExecute() {

    }

    @Override
    public void postProcessForExccute() {

    }
}
