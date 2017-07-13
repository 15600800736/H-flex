package com.frame.scan;

import com.frame.exceptions.ParseException;
import com.frame.info.ConfigurationNode;
import com.frame.info.Node;
import com.frame.info.XmlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by fdh on 2017/7/2.
 */
public class BaseContentScanner implements Scanner {
    private ConfigurationNode base_contents;

    private final String ANNOTATION_SCAN = "annotation-scan";
    private final String PATH = "path";
    public BaseContentScanner(ConfigurationNode base_contents) {
        this.base_contents = base_contents;
    }
    @Override
    public void scan(XmlConfiguration configuration) throws ParseException {
        Node root = configuration.getRoot();
        if(root == null && configuration.isAnnotationScan() == null) {
            throw new ParseException(null,"无法获取<annotation-scan/>状态");
        }
        if(root == null) {
            // what to do?
        }
        if(configuration.isAnnotationScan() == null) {
            ConfigurationNode annotationScan = root.getChild(ANNOTATION_SCAN);
            if (annotationScan == null) {
                configuration.setAnnotationScan(false);
            } else {
                configuration.setAnnotationScan(true);
            }
        }
        if(configuration.isAnnotationScan()) {
            List<ConfigurationNode> pathNodeList = base_contents.getChildren(PATH);
            List<String> paths = new LinkedList<>();
            pathNodeList.forEach(p -> {
                String text = p.getText();
                paths.add(text);
            });

            List<String> actionClasses = new ArrayList<>(64);
            paths.forEach(path -> {
                String classpath = System.getProperty("user.dir");
                String pathName = classpath + "\\src\\main\\java\\" + path.replace(".","\\");
                getClassesFromClasspath(pathName,actionClasses);
            });
            configuration.setClassesPath(actionClasses);
        }
        configuration.getClassesPath().forEach(System.out::println);
        throw new ParseException("<annotation-scan/>", "未开启目录扫描");
    }

    public static List<String> getClassesFromClasspath(String pathName, List<String> classesPath) {
        File file = new File(pathName);
        if(file != null) {
            File[] children = file.listFiles();
            for(File child : children) {
                if(child.isDirectory()) {
                    getClassesFromClasspath(child.getPath(), classesPath);
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
}
