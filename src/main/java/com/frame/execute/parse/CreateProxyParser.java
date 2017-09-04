package com.frame.execute.parse;

import com.frame.context.Context;
import com.frame.context.ExecutionContext;
import com.frame.context.ParserContext;
import com.frame.context.info.StringInfomation.Configuration;
import com.frame.example.Use;
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
        ActionClassParser actionClassParser = new ActionClassParser(this.production, configuration);
        executionClassesPath.forEach((k,v) -> {
            actionClassParser.setClazzName(v);
            try {
                actionClassParser.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return null;
    }
}
