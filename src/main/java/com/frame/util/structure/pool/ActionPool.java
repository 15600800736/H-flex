package com.frame.util.structure.pool;

import com.frame.util.structure.strategy.Strategy;
import com.frame.util.structure.strategy.specific.PoolStrategy;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * Created by fdh on 2017/9/29.
 */
public class ActionPool extends Pool<Method> {


    public ActionPool(int size, PoolStrategy poolStrategy) {
        super(size, poolStrategy);
    }

    @Override
    public int getCacheSize() {
        return size;
    }

    @Override
    public void weedOutElement() {

    }


    @Override
    public int weedOutElementIndex() {
        return 0;
    }


    @Override
    public void add(String key, Method val) {

    }
}
