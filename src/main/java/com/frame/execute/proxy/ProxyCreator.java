package com.frame.execute.proxy;

import com.frame.context.ParserContext;
import com.frame.context.info.StringInformation.Configuration;
import com.frame.example.UseB;
import com.frame.execute.Executor;
import net.sf.cglib.proxy.Enhancer;

/**
 * Created by fdh on 2017/9/1.
 */
public class ProxyCreator extends Executor<Class<?>, Object> {

    private Configuration configuration;

    private ParserContext context;
    public ProxyCreator(Class<?> production, Configuration configuration, ParserContext context) {
        super(production);
        this.configuration = configuration;
        this.context = context;
    }


    @Override
    protected Object exec() throws Exception {
        ExecutionProxy executionProxy = new ExecutionProxy(
                this.configuration.getActions(),
                this.configuration.getActionAlias(),
                this.context.getActions(),
                this.context.getActionsClazz(),
                this.configuration.getClassesPathMapper());
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(this.production);
        enhancer.setCallback(executionProxy);
        return enhancer.create();
    }

    public static void main(String[] args) throws Exception {
        ProxyCreator proxyCreator = new ProxyCreator(UseB.class, new Configuration(), new ParserContext());
        UseB b = (UseB) proxyCreator.execute();
        b.getTest();
    }
}