package com.frame.scan;

import com.frame.exceptions.ParseException;
import com.frame.info.ConfigurationNode;
import com.frame.info.XmlConfiguration;
import com.frame.util.ConfigurationReader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fdh on 2017/7/13.
 */
public class ClassPathScanner implements Scanner {

    private ConfigurationNode action_classes;

    private final String ACTION_CLASS = "action-class";
    private final String PATH = "path";

    public ClassPathScanner(ConfigurationNode action_classes) {
        this.action_classes = action_classes;
    }

    @Override
    public void scan(XmlConfiguration configuration) throws ParseException {
        List<ConfigurationNode> actionClassNodeList = action_classes.getChildren(ACTION_CLASS);
        List<String> actionClasses = new ArrayList<>(64);
        actionClassNodeList.forEach(ac -> {
            String path = ac.getChild(PATH).getText();
            actionClasses.add(path);
        });
        actionClasses.forEach(System.out::println);
    }

    public static void main(String...strings) {
        try {
            ConfigurationReader configurationReader = new ConfigurationReader("F:\\SourceTreeGit\\H-flex\\src\\main\\resources\\test.xml");
            XmlConfiguration configuration = new XmlConfiguration();
            configuration.setRoot(configurationReader.getRoot());
            new BaseContentScanner(configuration.getRoot().getChild("action-classes").getChild("base-contents")).scan(configuration);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
