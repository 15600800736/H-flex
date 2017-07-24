package com.frame.execute.scan;

import com.frame.annotations.ActionClass;
import com.frame.enums.ConfigurationStringPool;
import com.frame.exceptions.ScanException;
import com.frame.info.Configuration;
import com.frame.info.ConfigurationNode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fdh on 2017/7/14.
 */

/**
 * The RegisterScanner is used for register all of the configuration like actions or action groups
 * It will be called to collect the information from xml or annotations if it is enabled when the frame starts
 * You can configure both <action-classes></action-classes> to specify the path of a method
 * and <base-contents></base-contents> to let the frame scan the class with annotation @ActionClasses
 * under the path. You also can configure <action-groups></action-groups> to register your action groups to call
 * in your program.
 */
@ActionClass(className = "e")
public class RegisterScanner implements Scanner {
    private ConfigurationNode actionRegister;
    private ConfigurationNode actionGroups;

    private final Map<String, Class<? extends Scanner>> creatorMapper = new HashMap<>(8);

    public RegisterScanner(ConfigurationNode actionRegister, ConfigurationNode actionGroups) {
        this.actionRegister = actionRegister;
        this.actionGroups = actionGroups;
        // register scanner
        initCreatorMapper();
    }

    @Override
    public void scan(Configuration configuration) throws ScanException {
        if (actionRegister == null) {
            throw new ScanException("<action-register></action-register>", "未定义<action-register>");
        }
        // register actions
        // get all of the child node
        ConfigurationNode actionClasses = actionRegister.getChild(ConfigurationStringPool.ACTION_CLASSES);
        ConfigurationNode annotationScan = actionRegister.getChild(ConfigurationStringPool.ANNOTATION_SCAN);
        ConfigurationNode baseContents = actionRegister.getChild(ConfigurationStringPool.BASE_CONTENTS);
        Boolean canRegisterAction = (actionClasses != null) || (annotationScan != null && baseContents != null);

        if (!canRegisterAction) {
            throw new ScanException("<action-classes></action-classes> 或 <annotation-scan/><base-contents></base-contents>"
                    , "没有可供注册的方法");
        }
        Scanner scanner = null;

        // register scan by xml
        if (actionClasses != null) {
            scanner = createScanner(ConfigurationStringPool.ACTION_CLASSES);
            scanner.scan(configuration);
        }
        // register scan by annotations
        if (baseContents != null) {
            scanner = createScanner(ConfigurationStringPool.BASE_CONTENTS);
            scanner.scan(configuration);
        }
        // register action groups
//        if(actionGroups != null) {
//            scanner = createScanner(ACTION_GROUPS);
//            scanner.scan(configuration);
//        }

    }

    /**
     * return the scanner according to the tag's name
     * @param scannerType
     * @return
     */
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

    /**
     * map the scanner to the tag's name
     */
    private void initCreatorMapper() {
        creatorMapper.put(ConfigurationStringPool.BASE_CONTENTS, BaseContentsScanner.class);
        creatorMapper.put(ConfigurationStringPool.ACTION_CLASSES, RegisterActionClassesScanner.class);
        creatorMapper.put(ConfigurationStringPool.ACTION_GROUPS, ActionGroupScanner.class);
    }

    @Override
    public void prepareForExecute() {

    }

    @Override
    public void postProcessForExccute() {

    }
}
