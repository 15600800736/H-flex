package com.frame.execute.proxy;

import com.frame.example.Use;
import com.frame.execute.Executor;
import net.sf.cglib.proxy.Enhancer;

/**
 * Created by fdh on 2017/9/1.
 */
public class ProxyCreator extends Executor<Class<?>, Object> {

    public ProxyCreator(Class<?> production) {
        super(production);
    }

    @Override
    protected Object exec() throws Exception {
        ExecutionProxy executionProxy = new ExecutionProxy();
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(this.production);
        enhancer.setCallback(executionProxy);
        return enhancer.create();
    }
}
