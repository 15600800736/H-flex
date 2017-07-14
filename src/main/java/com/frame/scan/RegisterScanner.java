package com.frame.scan;

import com.frame.exceptions.ParseException;
import com.frame.info.ConfigurationNode;
import com.frame.info.XmlConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fdh on 2017/7/14.
 */
public class RegisterScanner implements Scanner {

    private ConfigurationNode action_register;

    private final String ACTION_CLASSES = "action-classes";
    private final String ANNOTATION_SCAN = "annotation-scan";
    private final String BASE_CONTENTS = "base-contents";

    private final Map<String,Class<? extends Scanner>> creatorMapper = new HashMap<>(8);
    public RegisterScanner(ConfigurationNode action_register) {
        this.action_register = action_register;
    }

    @Override
    public void scan(XmlConfiguration configuration) throws ParseException {
        if(action_register == null) {
            throw new ParseException("<action-register></action-register>","未定义<action-register>");
        }
        ConfigurationNode action_classes = action_register.getChild(ACTION_CLASSES);
        ConfigurationNode annotation_scan = action_register.getChild(ANNOTATION_SCAN);
        ConfigurationNode base_contents = action_register.getChild(BASE_CONTENTS);
        Boolean canRegisterAction = (action_classes != null) || (annotation_scan != null && base_contents != null);
        if(!canRegisterAction) {
            throw new ParseException("<action-classes></action-classes> 或 <annotation-scan/><base-contents></base-contents>"
                    ,"没有可供注册的方法");
        }
        Scanner scanner = null;

        if(action_classes != null) {
            scanner = createScanner(ACTION_CLASSES);
            scanner.scan(configuration);
        }
        if(base_contents != null) {
            scanner = createScanner(BASE_CONTENTS);
            scanner.scan(configuration);
        }
    }

    private Scanner createScanner(String scannerType) {
        Class<? extends Scanner> scannerClass = creatorMapper.get(scannerType);
        Scanner scanner = null;
        if(scannerClass != null) {
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
    }
}
