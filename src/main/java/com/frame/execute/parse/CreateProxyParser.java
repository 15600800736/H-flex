package com.frame.execute.parse;

import com.frame.context.Context;
import com.frame.context.ExecutionContext;
import com.frame.context.info.StringInfomation.Configuration;

/**
 * Created by fdh on 2017/8/29.
 */
public class CreateProxyParser extends Parser {
    /**
     *
     */
    Configuration configuration;

    /**
     *
     */
    ExecutionContext context = new ExecutionContext();

    public CreateProxyParser(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    protected Object exec() throws Exception {
        return null;
    }
}
