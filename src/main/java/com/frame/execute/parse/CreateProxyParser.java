package com.frame.execute.parse;

import com.frame.context.ParserContext;
import com.frame.context.info.StringInformation.Configuration;
import com.frame.exceptions.ParseException;
import com.frame.execute.parse.action.ActionClassParser;
import com.frame.execute.proxy.ProxyCreator;

import java.util.Map;

/**
 * Created by fdh on 2017/8/29.
 */
public class CreateProxyParser extends Parser {


    public CreateProxyParser(Configuration configuration) {
        super(configuration);
    }

    public CreateProxyParser(ParserContext production, Configuration configuration) {
        super(production, configuration);
    }

    @Override
    protected Object exec() throws Exception {
        Map<String, String> executionClassesPath = configuration.getExecutionClassesPath();
        executionClassesPath.forEach(this::createProxy);
        return null;
    }

    private Boolean createProxy(String executionName, String executionPath) {
        if (executionName == null || executionPath == null) {
            return false;
        }

        Class<?> executionClazz;
        try {
            executionClazz = Class.forName(executionPath);
        } catch (ClassNotFoundException e) {
            throw new ParseException("execution  class", "there is no such a class");
        }
        if (executionClazz == null) {
            return false;
        }

        ProxyCreator proxyCreator = new ProxyCreator(executionClazz, configuration);
        Object proxy = null;
        try {
             proxy = proxyCreator.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (proxy == null) {
            return false;
        }

        this.production.appendProxy(executionName, proxy);
        return true;
    }
}
