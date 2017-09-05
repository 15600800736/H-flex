package com.frame.execute.proxy;

import com.frame.context.info.StringInformation.Configuration;
import com.frame.execute.Executor;
import net.sf.cglib.proxy.Enhancer;

/**
 * Created by fdh on 2017/9/1.
 */
public class ProxyCreator extends Executor<Class<?>, Object> {

    private Configuration configuration;

    public ProxyCreator(Class<?> production, Configuration configuration) {
        super(production);
        this.configuration = configuration;
    }


    @Override
    protected Object exec() throws Exception {
        ExecutionProxy executionProxy = new ExecutionProxy(configuration.getActions());
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(this.production);
        enhancer.setCallback(executionProxy);
        return enhancer.create();
    }
}