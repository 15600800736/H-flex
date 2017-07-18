package com.frame.execute.transform;

import com.frame.execute.Executor;

/**
 * Created by fdh on 2017/7/4.
 */
public interface Transformer<T> extends Executor{
    T transform();
}
