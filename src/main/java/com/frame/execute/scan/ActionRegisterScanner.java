package com.frame.execute.scan;

import com.frame.annotations.ActionClass;
import com.frame.enums.ConfigurationStringPool;
import com.frame.exceptions.ScanException;
import com.frame.info.Configuration;
import com.frame.info.ConfigurationNode;
import com.frame.util.ExceptionUtil;
import com.frame.execute.valid.MethodExactlyNameValidor;
import com.frame.execute.valid.Validor;

import java.util.List;

/**
 * Created by fdh on 2017/7/13.
 */

/**
 * The class ActionRegisterScanner is used for register a action
 * include alias,name and id attributes
 */
@ActionClass(className = "b")
public class ActionRegisterScanner implements Scanner {

    private ConfigurationNode actionClass;


    public void setActionClass(ConfigurationNode actionClass) {
        this.actionClass = actionClass;
    }

    @Override
    public void scan(Configuration configuration) throws ScanException {
        if(actionClass == null) {
            throw new ScanException("<action-class></action-class>","缺少<action-class>标签");
        }
        List<ConfigurationNode> actionList = actionClass.getChildren(ConfigurationStringPool.ACTION);
        actionList.forEach((ConfigurationNode al) -> {
            if(!al.hasAttribute(ConfigurationStringPool.NAME_ATTRIBUTE)) {
                ExceptionUtil.doThrow(new ScanException("<action path='xxx'></action>","标签" + al.getName() + "缺少属性" + ConfigurationStringPool.NAME_ATTRIBUTE));
            }
            String name = null;
            String id = null;
            String alias = null;
            if(al.hasAttribute(ConfigurationStringPool.ID_ATTRIBUTE)) {
                id = al.getAttributeText(ConfigurationStringPool.ID_ATTRIBUTE);
            }
            if(al.hasAttribute(ConfigurationStringPool.NAME_ATTRIBUTE)) {
                name = al.getAttributeText(ConfigurationStringPool.NAME_ATTRIBUTE);
            }
            if(al.hasAttribute(ConfigurationStringPool.ALIAS_ATTRIBUTE)) {
                alias = al.getAttributeText(ConfigurationStringPool.ALIAS_ATTRIBUTE);
            }
            String classPath = actionClass.getAttributeText(ConfigurationStringPool.PATH_ATTRIBUTE);
            String methodPath = classPath + "." + name;
            Validor validor = new MethodExactlyNameValidor(methodPath);
            if(!validor.valid()) {
                ExceptionUtil.doThrow(new ScanException("com.frame.test.Test.testMethod","方法名格式错误"));
            }
            if(id == null) {
                id = methodPath;
            }
            configuration.appendAction(id,name);
            configuration.appendActionClass(id, classPath);
            String[] aliases = alias.split(",");
            for(String a : aliases) {
                configuration.appendAlias(alias, id);
            }
        });
    }

    @Override
    public void prepareForExecute() {

    }

    @Override
    public void postProcessForExccute() {

    }
}
