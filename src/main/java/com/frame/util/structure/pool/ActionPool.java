package com.frame.util.structure.pool;

import com.frame.util.structure.strategy.Strategy;
import com.frame.util.structure.strategy.specific.PoolStrategy;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * Created by fdh on 2017/9/29.
 */
public class ActionPool extends Pool<Method> {

    public ActionPool() {
        super(2);
    }

    public ActionPool(int[] size, PoolStrategy poolStrategy) {
        super(2, size, poolStrategy);
    }

    @Override
    public Method getMethod(String key) {
        return null;
    }

    @Override
    public int getCacheSize() {
        return size[currentLevel];
    }

    @Override
    public Method weedOutElement() {
        return null;
    }


    @Override
    public int weedOutElementIndex() {
        return 0;
    }


    @Override
    public void add(String key, Method val) {

    }
}
