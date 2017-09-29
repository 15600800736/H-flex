package com.frame.util.structure.pool;

import com.frame.util.structure.pool.poolstrategy.PoolStrategy;

import java.lang.reflect.Method;

/**
 * Created by fdh on 2017/9/29.
 */
public class ActionPool extends Pool{
    public ActionPool() {
        super(2);
    }

    public ActionPool(int[] size, PoolStrategy poolStrategy) {
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
}
