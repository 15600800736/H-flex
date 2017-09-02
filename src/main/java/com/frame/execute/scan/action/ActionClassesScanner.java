package com.frame.execute.scan.action;

import com.frame.annotations.Action;
import com.frame.annotations.ActionClass;
import com.frame.context.resource.Resource;
import com.frame.enums.ConfigurationStringPool;
import com.frame.exceptions.ScanException;
import com.frame.execute.scan.*;
import com.frame.execute.valid.Validor;
import com.frame.context.info.StringInfomation.ActionInfo;
import com.frame.context.info.StringInfomation.Configuration;
import com.frame.context.info.StringInfomation.ConfigurationNode;
import com.frame.context.info.Node;
import com.frame.util.ExceptionUtil;
import com.frame.util.ScanUtil;

import java.util.*;
import java.util.Scanner;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by fdh on 2017/7/15.
 */

/**
 * The ActionClassesScanner is used for resolving the tag &lt;action-classes&gt;&lt;/action-classes&gt;
 * It will extract the action classes you configured in xml file and mapped the class name to its path
 * Then create a action register to scan the actions you configured under the class.
 */
public class ActionClassesScanner extends com.frame.execute.scan.Scanner {
    /**
     * <p>The class ActionRegisterScanner is used for register a action, it deal with the tag&lt;action&gt;&lt;/action&gt;</p>
     */
    class ActionRegisterScanner extends com.frame.execute.scan.Scanner {

        /**
         * <p>&lt;action-class&gt;&lt;/action-class&gt; node</p>
         */
        private ConfigurationNode actionClass;

        /**
         * Configuration
         * @param production
         */
        public ActionRegisterScanner(Configuration production) {
            super(production);
        }

        /**
         *
         * @param actionClass
         */
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
                boolean overload = false;
                String returnType = null;

                // extract the name attribute, it specify a method in a action
                if (al.hasAttribute(ConfigurationStringPool.NAME_ATTRIBUTE)) {
                    name = al.getAttributeText(ConfigurationStringPool.NAME_ATTRIBUTE);
                }
                // extract the id attribute, if it's not configured, use full method path as default
                if (al.hasAttribute(ConfigurationStringPool.ID_ATTRIBUTE)) {
                    id = al.getAttributeText(ConfigurationStringPool.ID_ATTRIBUTE);
                }
                String classPath = actionClass.getAttributeText(ConfigurationStringPool.PATH_ATTRIBUTE);
                String methodPath = classPath + "." + name;
                if (id == null) {
                    id = methodPath;
                }

                // extract the path of class
                if(!actionClass.hasAttribute(ConfigurationStringPool.PATH_ATTRIBUTE)) {
                    ExceptionUtil.doThrow(new ScanException("<action-class path='com.path.A'", "<action-class>缺少path属性"));
                }
                // extract the overload attribute, true or false
                if (al.hasAttribute(ConfigurationStringPool.OVERLOAD)
                        && "true".equals(al.getAttributeText(ConfigurationStringPool.OVERLOAD))) {
                    overload = true;
                }


                // create a new action
                ActionInfo newAction = ActionInfo.createActionInfo(id)
                    .setName(name)
                    .setActionClass(classPath)
                    .setAlias(getAliases(al))
                    .setParam(getParamType(al))
                    .setOverload(overload)
                    .setReturnType(returnType);
                // add it into production
                production.appendAction(id, newAction);
            });
            return true;
        }

        /**
         * <p>extract &lt;param&gt;&lt;/param&gt; , transfer type alias to the real path of type</p>
         * @param action &lt;action&gt;&lt;/action&gt; node
         * @return
         */
        private String[] getParamType(ConfigurationNode action) {
            if (action == null) {
                return null;
            }
            List<ConfigurationNode> parameterTypes = action.getChildren(ConfigurationStringPool.PARAM);
            if (parameterTypes == null) {
                return null;
            }
            String[] params = new String[parameterTypes.size()];
            Iterator<ConfigurationNode> iter = parameterTypes.iterator();
            for (int i = 0; i < params.length && iter.hasNext(); i++) {
                ConfigurationNode param = iter.next();
                String rawParam = param.getText();
                params[i] = ScanUtil.getRealType(rawParam, this.production.getTypeAliases());
            }
            return params;
        }

        /**
         * <p>extract &lt;alias&gt;&lt;/alias&gt; </p>
         * @param action
         * @return
         */
        private List<String> getAliases(ConfigurationNode action) {
            if (!action.hasAttribute(ConfigurationStringPool.ALIAS_ATTRIBUTE)) {
                return null;
            }
            String alias = action.getAttributeText(ConfigurationStringPool.ALIAS_ATTRIBUTE);
            String[] aliases = alias.split(",");
            return Arrays.asList(aliases);
        }
    }

    /**
     * Constructor
     * @param production
     */
    public ActionClassesScanner(Configuration production) {
        super(production);
    }

    @Override
    public Object exec() throws ScanException {
        Node root = production.getRoot();
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
        ActionRegisterScanner scanner = new ActionRegisterScanner(production);
        registerAction(actionClassList, classMapper, scanner);
        appendClasses(classMapper, production);

        return true;
    }

    /**
     * register an action
     * @param actionClassList action classes
     * @param classMapper the class map
     * @param actionScanner a scanner that can deal with the tag &lt;action&gt;&lt;/action&gt;
     */
    private void registerAction(List<ConfigurationNode> actionClassList, Map<String, String> classMapper, com.frame.execute.scan.Scanner actionScanner) {
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

    /**
     * inject an classes to the configuration
     * @param classMapper
     * @param production
     */
    private void appendClasses(Map<String, String> classMapper, Configuration production) {
        classMapper.entrySet().forEach(es->{
            production.appendClass(es.getKey(), es.getValue());
        });
    }
}
