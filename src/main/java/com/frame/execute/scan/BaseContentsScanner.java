package com.frame.execute.scan;

import com.frame.annotations.ActionClass;
import com.frame.annotations.ActionGroup;
import com.frame.exceptions.ParseException;
import com.frame.info.Configuration;
import com.frame.info.ConfigurationNode;
import com.frame.info.Node;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by fdh on 2017/7/2.
 */
public class BaseContentsScanner implements Scanner {
    private final String ACTION_REGISTER = "action-register";
    private final String BASE_CONTENTS = "base-contents";
    private final String ANNOTATION_SCAN = "annotation-scan";
    private final String PATH = "path";
    private final Integer CLASS_SUFFIX_LENGTH = 6;

    @Override
    public void scan(Configuration configuration) throws ParseException {
        Node root = configuration.getRoot();
        if (root == null && configuration.isAnnotationScan() == null) {
            throw new ParseException(null, "无法获取<annotation-scan/>状态");
        }
        if (root == null) {
            // what to do?
            return;
        }
        // check if annotation-scan is on
        if (configuration.isAnnotationScan() == null) {
            ConfigurationNode annotationScan = root.getChild(ANNOTATION_SCAN);
            if (annotationScan == null) {
                configuration.setAnnotationScan(false);
            } else {
                configuration.setAnnotationScan(true);
            }
        }

        if (configuration.isAnnotationScan()) {
            ConfigurationNode baseContents = root.getChild(ACTION_REGISTER).getChild(BASE_CONTENTS);
            if (baseContents == null) {
                throw new ParseException("<base-contents></base-contents>", "缺少<base-contents>元素");
            }
            // get all path to check
            List<ConfigurationNode> pathNodeList = baseContents.getChildren(PATH);
            List<String> paths = new LinkedList<>();
            pathNodeList.forEach(p -> {
                String text = p.getText();
                paths.add(text);
            });


            List<Class> actionClasses = new ArrayList<>(64);
            // get Class under the path
            paths.forEach(path -> {
                String classpath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
                String pathName = classpath + path.replace(".", "\\");
                String packageName = path;
                getClassesFromClasspath(pathName, packageName,actionClasses);
            });
            actionClasses.forEach(ac -> System.out.println(ac.getName()));
        }
    }

    private void getClassesFromClasspath(String pathName,String packageName, List<Class> actionClasses) {
        File file = new File(pathName);
        if (file != null) {
            // list all files under the path
            File[] children = file.listFiles();
            if (children == null) {
                return;
            }
            for (File child : children) {
                if (child == null) {
                    continue;
                }
                // recursion and update package name
                if (child.isDirectory()) {
                    String childPackageName = packageName + "." + child.getName();
                    getClassesFromClasspath(child.getPath(),childPackageName, actionClasses);
                } else {
                    // load the class and check if the class is a action class
                    String className = child.getName();
                    if (className.endsWith(".java") || className.endsWith(".class")) {
                        // use classpath loader to load the class
                        ClassLoader loader = ClassLoader.getSystemClassLoader();
                        try {
                            // get the class's full name
                            String classPath = packageName + "." + className.substring(0,className.length() - CLASS_SUFFIX_LENGTH);
                            Class<?> actionClass = loader.loadClass(classPath);
                            if(actionClass.isAnnotationPresent(ActionClass.class)) {
                                actionClasses.add(actionClass);
                            }
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void prepareForExecute() {

    }

    @Override
    public void postProcessForExccute() {

    }

    public static void main(String... strings) {
        try {
            Class clazz = ClassLoader.getSystemClassLoader().loadClass("com.frame.info.ActionInfo");
            System.out.println(clazz.getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

