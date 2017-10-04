package com.frame.util.structure.pool;

import com.frame.util.structure.strategy.Strategy;

import java.lang.reflect.Method;

/**
 * Created by fdh on 2017/9/29.
 */
public class ActionPool extends Pool{
    public ActionPool() {
        super(2);
    }

    public ActionPool(int[] size, Strategy poolStrategy) {
        super(2, size, poolStrategy);
    }

    @Override
    public boolean addMethod(String key, Method method) {

        return false;
    }

    @Override
    public Method getMethod() {
        return null;
    }

    @Override
    public int getCacheSize() {
        return 0;
    }

    @Override
    public Object weedOutElement() {
        return null;
    }

    @Override
    public int weedOutElementIndex() {
        return 0;
    }
}
