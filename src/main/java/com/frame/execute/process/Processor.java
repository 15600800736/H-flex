package com.frame.execute.process;

import com.frame.execute.Executor;

/**
 * Created by fdh on 2017/7/14.
 */
public interface Processor extends Executor{
    Object process(Object o);
}
