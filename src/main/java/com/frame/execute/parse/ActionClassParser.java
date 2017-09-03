package com.frame.execute.parse;

import com.frame.context.ParserContext;
import com.frame.context.info.StringInfomation.Configuration;

/**
 * Created by fdh on 2017/9/3.
 */
public class ActionClassParser extends Parser {

    private String clazzName;

    public ActionClassParser(Configuration configuration, String clazzName) {
        super(configuration);
        this.clazzName = clazzName;
    }

    public ActionClassParser(ParserContext production, Configuration configuration, String clazzName) {
        super(production, configuration);
        this.clazzName = clazzName;
    }

    @Override
    protected Object exec() throws Exception {
        if (clazzName == null) {
            return null;
        }
        Class<?> clazz = this.production.getActionClazz(clazzName);
        // if there is no class in cache.
        if (clazz == null) {

        }
        return null;
    }
}
