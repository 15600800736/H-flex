package com.frame.execute.proxy;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by fdh on 2017/9/1.
 */
public class ExecutionProxy implements MethodInterceptor {

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("pre-handle");
        Object result = methodProxy.invokeSuper(o, objects);
        System.out.println("post-handle");
        return result;
    }
}
