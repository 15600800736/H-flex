package com.frame.context;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by fdh on 2017/8/29.
 */
public class ExecutionContext extends Context {

    /**
     *
     */
    private Map<String, Object> proxies = new HashMap<>(64);

    @Override
    public Object get(String alias) {
        return null;
    }

    @Override
    public <T> T get(String alias, Class<T> clazz) {
        return null;
    }
}
