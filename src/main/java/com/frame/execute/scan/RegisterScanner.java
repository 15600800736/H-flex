package com.frame.execute.scan;

import com.frame.exceptions.ParseException;
import com.frame.info.Configuration;
import com.frame.info.ConfigurationNode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fdh on 2017/7/14.
 */
public class RegisterScanner implements Scanner {

    private ConfigurationNode actionRegister;
    private ConfigurationNode actionGroups;
    private final String ACTION_CLASSES = "action-classes";
    private final String ANNOTATION_SCAN = "annotation-scan";
    private final String BASE_CONTENTS = "base-contents";
    private final String ACTION_GROUPS = "action-groups";

    private final Map<String, Class<? extends Scanner>> creatorMapper = new HashMap<>(8);

    public RegisterScanner(ConfigurationNode actionRegister, ConfigurationNode actionGroups) {
        this.actionRegister = actionRegister;
        this.actionGroups = actionGroups;
        // register scanner
        initCreatorMapper();
    }

    @Override
    public void scan(Configuration configuration) throws ParseException {
        if (actionRegister == null) {
            throw new ParseException("<action-register></action-register>", "未定义<action-register>");
        }
        // register actions
        // get all of the child node
        ConfigurationNode actionClasses = actionRegister.getChild(ACTION_CLASSES);
        ConfigurationNode annotationScan = actionRegister.getChild(ANNOTATION_SCAN);
        ConfigurationNode baseContents = actionRegister.getChild(BASE_CONTENTS);
        Boolean canRegisterAction = (actionClasses != null) || (annotationScan != null && baseContents != null);

        if (!canRegisterAction) {
            throw new ParseException("<action-classes></action-classes> 或 <annotation-scan/><base-contents></base-contents>"
                    , "没有可供注册的方法");
        }
        Scanner scanner = null;

        // register scan by xml
        if (actionClasses != null) {
            scanner = createScanner(ACTION_CLASSES);
            scanner.scan(configuration);
        }
        // register scan by annotations
        if (baseContents != null) {
            scanner = createScanner(BASE_CONTENTS);
            scanner.scan(configuration);
        }
        // register action groups
//        if(actionGroups != null) {
//            scanner = createScanner(ACTION_GROUPS);
//            scanner.scan(configuration);
//        }

    }

    private Scanner createScanner(String scannerType) {
        Class<? extends Scanner> scannerClass = creatorMapper.get(scannerType);
        Scanner scanner = null;
        if (scannerClass != null) {
            try {
                scanner = scannerClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return scanner;
    }

    private void initCreatorMapper() {
        creatorMapper.put(BASE_CONTENTS, BaseContentsScanner.class);
        creatorMapper.put(ACTION_CLASSES, RegisterActionClassesScanner.class);
        creatorMapper.put(ACTION_GROUPS, ActionGroupScanner.class);
    }

    @Override
    public void prepareForExecute() {

    }

    @Override
    public void postProcessForExccute() {

    }
}
