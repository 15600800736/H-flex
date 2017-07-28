package com.frame.execute.scan;

import com.frame.annotations.Action;
import com.frame.annotations.ActionClass;
import com.frame.context.resource.Resource;
import com.frame.enums.ConfigurationStringPool;
import com.frame.exceptions.ScanException;
import com.frame.execute.valid.MethodExactlyNameValidor;
import com.frame.execute.valid.Validor;
import com.frame.info.ActionInfo;
import com.frame.info.Configuration;
import com.frame.info.ConfigurationNode;
import com.frame.info.Node;
import com.frame.util.ExceptionUtil;

import java.util.*;

/**
 * Created by fdh on 2017/7/15.
 */

/**
 * The RegisterActionClassesScanner is used for resolve the tag <action-classes></action-classes>
 * It will extract the action classes you configured in xml file and mapped the class name to its path
 * Then create a action register to scan the actions you configured under the class.
 */
@ActionClass(className = "d")
public class ActionClassesScanner extends Scanner {

    /**
     * The class ActionRegisterScanner is used for register a action
     * include alias,name and id attributes
     */
    @ActionClass(className = "b")
    class ActionRegisterScanner extends Scanner {

        private ConfigurationNode actionClass;

        public ActionRegisterScanner(Configuration configuration) {
            super(configuration);
        }

        @Action(id = "aaaaa", alias = "lalala")
        public void setActionClass(ConfigurationNode actionClass) {
            this.actionClass = actionClass;
        }

        @Override
        public Object exec() throws Exception {
            if (actionClass == null) {
                throw new ScanException("<action-class></action-class>", "缺少<action-class>标签");
            }
            List<ConfigurationNode> actionList = actionClass.getChildren(ConfigurationStringPool.ACTION);
            actionList.forEach((ConfigurationNode al) -> {
                if (!al.hasAttribute(ConfigurationStringPool.NAME_ATTRIBUTE)) {
                    ExceptionUtil.doThrow(new ScanException("<action path='xxx'></action>", "标签" + al.getName() + "缺少属性" + ConfigurationStringPool.NAME_ATTRIBUTE));
                }
                String name = null;
                String id = null;
                if (al.hasAttribute(ConfigurationStringPool.ID_ATTRIBUTE)) {
                    id = al.getAttributeText(ConfigurationStringPool.ID_ATTRIBUTE);
                }
                if (al.hasAttribute(ConfigurationStringPool.NAME_ATTRIBUTE)) {
                    name = al.getAttributeText(ConfigurationStringPool.NAME_ATTRIBUTE);
                }
                if(!actionClass.hasAttribute(ConfigurationStringPool.PATH_ATTRIBUTE)) {
                    ExceptionUtil.doThrow(new ScanException("<action-class path='com.path.A'", "<action-class>缺少path属性"));
                }

                String classPath = actionClass.getAttributeText(ConfigurationStringPool.PATH_ATTRIBUTE);
                String methodPath = classPath + "." + name;

                Validor validor = new MethodExactlyNameValidor(methodPath);
                try {
                    if (!validor.execute()) {
                        ExceptionUtil.doThrow(new ScanException("com.frame.test.Test.testMethod", "方法名格式错误"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (id == null) {
                    id = methodPath;
                }
                // create a new action
                ActionInfo newAction = ActionInfo.createActionInfo(id)
                    .setName(name)
                    .setActionClass(classPath)
                    .setAlias(getAliases(al))
                    .setParam(getParamType(al));
                configuration.appendAction(id, newAction);
            });
            return true;
        }

        @Override
        public Resource[] getResources() {
            return new Resource[0];
        }

        private List<String> getParamType(ConfigurationNode action) {
            List<String> paramList = new LinkedList<>();
            List<ConfigurationNode> param = action.getChildren(ConfigurationStringPool.PARAM);
            param.forEach(p -> {
                paramList.add(p.getText());
            });
            return paramList;
        }

        private List<String> getAliases(ConfigurationNode action) {
            if (!action.hasAttribute(ConfigurationStringPool.ALIAS_ATTRIBUTE)) {
                return null;
            }
            String alias = action.getAttributeText(ConfigurationStringPool.ALIAS_ATTRIBUTE);
            String[] aliases = alias.split(",");
            return Arrays.asList(aliases);
        }
    }



    public ActionClassesScanner(Configuration configuration) {
        super(configuration);
    }

    @Override
    public Object exec() throws ScanException {
        Node root = configuration.getRoot();
        if (root == null) {
            throw new ScanException("<frame-haug></frame-haug>", "缺少根节点");
        }
        // get the action-classes node
        ConfigurationNode actionClasses = root.getChild(ConfigurationStringPool.ACTION_REGISTER).getChild(ConfigurationStringPool.ACTION_CLASSES);
        if (actionClasses == null) {
            throw new ScanException("<action-classes></action-classes>", "缺少<action-classes>元素");
        }
        // get all of the action-class
        List<ConfigurationNode> actionClassList = actionClasses.getChildren(ConfigurationStringPool.ACTION_CLASS);
        Map<String, String> classMapper = new HashMap<>(64);
        ActionRegisterScanner scanner = new ActionRegisterScanner(configuration);
        registerAction(actionClassList, classMapper, scanner, configuration);
        appendClasses(classMapper, configuration);
        return true;
    }

    @Action(id = "bbbb", alias = "woaini")
    private void registerAction(List<ConfigurationNode> actionClassList, Map<String, String> classMapper, Scanner actionScanner, Configuration configuration) {
        actionClassList.forEach(n -> {
            String className = n.getAttributeText(ConfigurationStringPool.NAME_ATTRIBUTE);
            String classPath = n.getAttributeText(ConfigurationStringPool.PATH_ATTRIBUTE);
            if (className == null) {
                className = classPath;
            }
            classMapper.put(className, classPath);
            if (actionScanner instanceof ActionRegisterScanner) {
                ((ActionRegisterScanner) actionScanner).setActionClass(n);
            }
            try {
                actionScanner.execute();
            } catch (Exception e) {
                ExceptionUtil.doThrow(e);
            }
        });
    }

    @Override
    public Resource[] getResources() {
        return new Resource[0];
    }

    private void appendClasses(Map<String, String> classMapper, Configuration configuration) {
        classMapper.entrySet().forEach(es->{
            configuration.appendClass(es.getKey(), es.getValue());
        });
    }
}
