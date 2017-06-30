
package com.frame.mapper.method;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MethodAliasMapper {
    private static MethodAliasMapper instance = null;
    Map<String,Method> methodAliasMapper = new ConcurrentHashMap<>();
    private MethodAliasMapper() {

    }
    public static MethodAliasMapper getInstance() {
        if(instance == null) {
            instance = new MethodAliasMapper();
        }
        return instance;
    }
    public  void setMethod(String alias, Method method) {
        methodAliasMapper.put(alias,method);
    }

    public  Method getMethod(String alias) {
        return methodAliasMapper.get(alias);
    }
}