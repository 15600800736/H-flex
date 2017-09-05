package com.frame.execute.parse.action;

import com.frame.context.ParserContext;
import com.frame.context.info.StringInformation.ActionInfo;
import com.frame.context.info.StringInformation.Configuration;
import com.frame.execute.parse.Parser;

/**
 * Created by fdh on 2017/9/2.
 */
public class ActionParser extends Parser {

    /**
     *
     */
    private String rawMethodId;

    public ActionParser(Configuration configuration, String rawMethodId) {
        super(configuration);
        this.rawMethodId = rawMethodId;
    }

    public ActionParser(ParserContext production, Configuration configuration, String rawMethodId) {
        super(production, configuration);
        this.rawMethodId = rawMethodId;
    }

    @Override
    protected Object exec() throws Exception {
        if (configuration == null) {
            return false;
        }

        String methodId = configuration.getActionAlias() == null ?
                null
                : configuration.getActionAlias().get(rawMethodId);
        if (methodId == null) {
            methodId = rawMethodId;
        }

        ActionInfo actionInfo = configuration.getActions() == null ?
                null : configuration.getActions().get(methodId);

        if (actionInfo == null) {
            return false;
        }

        String clazzName = actionInfo.getActionClass();

        Class<?> actionClazz = this.production.getActionClazz(clazzName);
        if (actionClazz == null) {
            Parser clazzParser = new ActionClassParser(production, configuration, clazzName);

            clazzParser.execute();
        }
        actionClazz = this.production.getActionClazz(clazzName);
        if (actionClazz == null) {
            return false;
        }







        return null;



    }
}
