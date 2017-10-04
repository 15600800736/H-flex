package com.frame.util.structure.pool;

import com.frame.util.structure.LimitableCache;
import com.frame.util.structure.strategy.Strategy;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fdh on 2017/9/29.
 */
public abstract class Pool<E>
        implements LimitableCache<E> {

    /**
     * The numbers of level of pool
     */
    protected int level = 2;

    /**
     * size of each level
     */
    protected int[] size = new int[level];

    /**
     * strategy mode
     */
    protected Strategy poolStrategy;

    /**
     * store methods
     */
    protected Map<String, E> pool;
    /**
     *
     * @param level
     */
    protected Pool(int level) {
        this.level = level;
    }

    /**
     *
     * @param level
     * @param size
     * @param poolStrategy
     */
    public Pool(int level, int[] size, Strategy poolStrategy) {
        this.level = level;
        if (this.level == size.length) {
            this.size = size;
        } else {
            this.size = Arrays.copyOf(size, this.level);
        }
        this.poolStrategy = poolStrategy;
        if (this.level > 1 && this.size[0] > 1) {
            pool = new HashMap<>(this.size[0]);
        } else {
            pool = new HashMap<>(64);
        }
    }

    public void setLevelSize(int level, int size) {
        if (level > -1 && size > -1) {
            this.size[level] = size;
        }
    }

    public void setPoolStrategy(Strategy poolStrategy) {
        this.poolStrategy = poolStrategy;
    }

    public abstract boolean addMethod(String key, Method method);

    public abstract Method getMethod();

}
