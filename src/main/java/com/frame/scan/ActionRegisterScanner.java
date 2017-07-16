package com.frame.scan;

import com.frame.exceptions.ParseException;
import com.frame.info.Configuration;
import com.frame.info.ConfigurationNode;
import com.frame.util.ExceptionUtil;

import java.util.List;

/**
 * Created by fdh on 2017/7/13.
 */
public class ActionRegisterScanner implements Scanner {


    private final String ACTION = "action";
    private final String ALIAS_ATTRIBUTE = "alias";
    private final String NAME_ATTRIBUTE = "name";

    private ConfigurationNode actionClass;
    @Override
    public void scan(Configuration configuration) throws ParseException {
        if(actionClass == null) {
            throw new ParseException("<action-class></action-class>","缺少<action-class>标签");
        }
        List<ConfigurationNode> actionList = actionClass.getChildren(ACTION);
        actionList.forEach((ConfigurationNode al) -> {
            if(!al.hasAttribute(NAME_ATTRIBUTE)) {
                ExceptionUtil.doThrow(new ParseException("<action name='xxx'></action>","标签" + al.getName() + "缺少属性" + NAME_ATTRIBUTE));
            }
            String name = null;
            String alias = null;
            if(al.hasAttribute(NAME_ATTRIBUTE)) {
                name = al.getAttributeText(NAME_ATTRIBUTE);
            }
            if(al.hasAttribute(ALIAS_ATTRIBUTE)) {
                alias = al.getAttributeText(ALIAS_ATTRIBUTE);
            }


        });
    }
}
