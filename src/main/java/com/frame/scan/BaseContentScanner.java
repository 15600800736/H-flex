package com.frame.scan;

import com.frame.exceptions.ParseException;
import com.frame.info.ConfigurationNode;
import com.frame.info.XmlConfiguration;
import org.dom4j.Element;

import javax.security.auth.login.Configuration;
import java.io.File;
import java.net.URL;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by fdh on 2017/7/2.
 */
public class BaseContentScanner implements Scanner {
    private ConfigurationNode base_contents;
    private XmlConfiguration xmlConfiguration;

    public BaseContentScanner(ConfigurationNode base_contents, XmlConfiguration xmlConfiguration) {
        this.xmlConfiguration = xmlConfiguration;
        this.base_contents = base_contents;
    }
    @Override
    public XmlConfiguration scan() throws ParseException {
        ConfigurationNode root = xmlConfiguration.getRoot();
        if(root == null || xmlConfiguration.getAnnotationScan() == null) {
            throw new ParseException(null,"无法获取<annotation-scan/>状态");
        }
        if(xmlConfiguration.getAnnotationScan() == null) {
            ConfigurationNode annotationScan = root.getChild("annotation-scan");

        }
        if(xmlConfiguration.getAnnotationScan()) {
            List<ConfigurationNode> pathNodeList = base_contents.getChildren("path");
            List<String> paths = new LinkedList<>();
            for (ConfigurationNode pathNode : pathNodeList) {
                String text = pathNode.getText();
                paths.add(text);
            }
            List<String> actionClasses = getClassesPath(paths);
            xmlConfiguration.setClassesPath(actionClasses);
            return xmlConfiguration;
        }
        throw new ParseException("<annotation-scan/>", "未开启目录扫描");
    }

    private List<String> getClassesPath(List<String> paths) throws ParseException {
        List<String> classesPath = new LinkedList<>();
        for(String path : paths) {
            String pathName = path.replace(".","/");
            classesPath.addAll(getClassesFrom(pathName, paths));
        }
        return classesPath;
    }

    public static List<String> getClassesFrom(String pathName, List<String> classesPath) {
        File file = new File(pathName);
        if(file != null) {
            File[] children = file.listFiles();
            for(File child : children) {
                if(child.isDirectory()) {
                    getClassesFrom(child.getPath(), classesPath);
                } else {
                    String absolutePath = child.getAbsolutePath();
                    if(absolutePath.endsWith(".java") || absolutePath.endsWith(".class")) {
                        classesPath.add(absolutePath);
                    }
                }
            }
        }
        return classesPath;
    }

    public static void main(String...strings) {

    }
}
