
package com.frame.proxy;


import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class Proxy implements MethodInterceptor {
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        if(method.getName().equals("test")) {
            Object params[] = new Object[2];
            params[0] = objects[1];
            params[1] = objects[0];
            Method method1 = this.getClass().getMethod("a",Integer.class,String.class);
            method1.invoke(this,params);
        }
        return null;
    }

    public Object a(Integer a,String b) {
        System.out.print(b);
        return null;
    }
}