package com.frame.pool;

import com.frame.strategy.specific.pool.PoolStrategy;

import java.lang.reflect.Method;

/**
 * Created by fdh on 2017/9/29.
 */
public class ActionPool extends MultiLevelPool<Method> {


    public ActionPool(int size, PoolStrategy poolStrategy, Pool<Method> deeperPool) {
        super(size, poolStrategy, deeperPool);
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
