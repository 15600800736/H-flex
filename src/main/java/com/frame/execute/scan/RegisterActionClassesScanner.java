package com.frame.execute.scan;

import com.frame.exceptions.ParseException;
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
@SuppressWarnings("FieldCanBeLocal")
public class RegisterActionClassesScanner implements Scanner {

    private final String ACTION_CLASSES = "action-classes";
    private final String ACTION_CLASS = "action-class";
    private final String NAME_ATTRIBUTE = "name";
    private final String PATH_ATTRIBUTE = "path";
    private final String ACTION_REGISTER = "action-register";

    @Override
    public void scan(Configuration configuration) throws ParseException {
        Node root = configuration.getRoot();
        if (root == null) {
            throw new ParseException("<frame-haug></frame-haug>", "缺少根节点");
        }
        ConfigurationNode action_classes = root.getChild(ACTION_REGISTER).getChild(ACTION_CLASSES);
        if (action_classes == null) {
            throw new ParseException("<action-classes></action-classes>", "缺少<action-classes>元素");
        }
        List<ConfigurationNode> actionClassList = action_classes.getChildren(ACTION_CLASS);
        Map<String, String> classMapper = new HashMap<>(64);
        ActionRegisterScanner scanner = new ActionRegisterScanner();

        configuration.appendClassesPath(classMapper);
    }

    private void registerAction(List<ConfigurationNode> actionClassList, Map<String, String> classMapper, Scanner actionScanner, Configuration configuration) {
        actionClassList.forEach(n -> {
            String className = n.getAttributeText(NAME_ATTRIBUTE);
            String classPath = n.getAttributeText(PATH_ATTRIBUTE);
            classMapper.put(className, classPath);
            if(actionScanner instanceof ActionRegisterScanner) {
                ((ActionRegisterScanner)actionScanner).setActionClass(n);
            }
            try {
                actionScanner.scan(configuration);
            } catch (ParseException e) {
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
