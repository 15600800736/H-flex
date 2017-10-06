package com.frame.util.structure.pool;

import com.frame.traits.Addable;
import com.frame.util.structure.LimitableCache;
import com.frame.util.structure.strategy.specific.PoolStrategy;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by fdh on 2017/9/29.
 */
public abstract class Pool<E>
        implements LimitableCache<E>, Addable<String, E> {

    /**
     * The numbers of level of pool
     */
    protected int level = 2;

    protected int currentLevel = 0;
    /**
     * size of each level
     */
    protected int[] size = new int[level];

    /**
     * strategy mode
     */
    protected PoolStrategy poolStrategy;

    /**
     * store methods
     */
    protected Map<String, E>[] pool;
    /**
     * @param level
     */
    protected Pool(int level) {
        this.level = level;
    }

    /**
     * @param level
     * @param size
     * @param poolStrategy
     */
    public Pool(int level, int[] size, PoolStrategy poolStrategy) {
        this.level = level;
        if (this.level == size.length) {
            this.size = size;
        } else {
            this.size = Arrays.copyOf(size, this.level);
        }
        this.poolStrategy = poolStrategy;
        if (this.level > 1) {
            // only the first cache will be used by multi thread
            pool[0] = new ConcurrentHashMap<>(this.size[0]);
            for (int i = 1; i < this.size.length; i++) {
                pool[i] = new HashMap<>(this.size[i]);
            }
        }
    }
    public void setLevelSize(int level, int size) {
        if (level > -1 && size > -1 && level < this.level) {
            this.size[level] = size;
        }
    }

    public void setPoolStrategy(PoolStrategy poolStrategy) {
        this.poolStrategy = poolStrategy;
    }

    public abstract Method getMethod(String key);

}
