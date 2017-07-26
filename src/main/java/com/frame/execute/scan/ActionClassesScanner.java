package com.frame.execute.scan;

import com.frame.annotations.ActionClass;
import com.frame.enums.ConfigurationStringPool;
import com.frame.exceptions.ScanException;
import com.frame.info.Configuration;
import com.frame.info.ConfigurationNode;
import com.frame.info.Node;
import com.frame.util.ExceptionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    public ActionClassesScanner(Configuration configuration) {
        super(configuration);
    }

    @Override
    public Boolean execute() throws ScanException {
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
        registerAction(actionClassList,classMapper,scanner,configuration);
        configuration.appendClass(classMapper);
        return true;
    }

    private void registerAction(List<ConfigurationNode> actionClassList, Map<String, String> classMapper, Scanner actionScanner, Configuration configuration) {
        actionClassList.forEach(n -> {
            String className = n.getAttributeText(ConfigurationStringPool.NAME_ATTRIBUTE);
            String classPath = n.getAttributeText(ConfigurationStringPool.PATH_ATTRIBUTE);
            classMapper.put(className, classPath);
            if(actionScanner instanceof ActionRegisterScanner) {
                ((ActionRegisterScanner)actionScanner).setActionClass(n);
            }
            try {
                actionScanner.execute();
            } catch (Exception e) {
                ExceptionUtil.doThrow(e);
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
