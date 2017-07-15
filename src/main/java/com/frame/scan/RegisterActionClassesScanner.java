package com.frame.scan;

import com.frame.exceptions.ParseException;
import com.frame.info.ConfigurationNode;
import com.frame.info.Node;
import com.frame.info.XmlConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fdh on 2017/7/15.
 */
public class RegisterActionClassesScanner implements Scanner{

    private final String ACTION_CLASSES = "action-classes";
    private final String ACTION_CLASS = "action-class";
    @Override
    public void scan(XmlConfiguration configuration) throws ParseException {
        Node root = configuration.getRoot();
        if(root == null) {
            throw new ParseException("<frame-haug></frame-haug>","缺少根节点");
        }
        ConfigurationNode action_classes = root.getChild(ACTION_CLASSES);
        if(action_classes == null) {
            throw new ParseException("<action-classes></action-classes>", "缺少<action-classes>元素");
        }
        List<ConfigurationNode> actionClassList = action_classes.getChildren(ACTION_CLASS);
        List<String> classesPath = new ArrayList<>(64);
        actionClassList.forEach(n -> classesPath.add(n));
        configuration.setClassesPath(classesPath);
        actionClassList.forEach(n -> {

        });

    }
}
