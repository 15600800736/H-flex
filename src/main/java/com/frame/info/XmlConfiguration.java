package com.frame.info;

import java.util.List;

/**
 * Created by fdh on 2017/7/3.
 */
public class XmlConfiguration {
    // 根节点
    private Node root;
    // 是否开启了注解扫描
    private Boolean annotationScan;
    // 扫描的类列表
    private List<String> classesPath;

    public Boolean getAnnotationScan() {
        return annotationScan;
    }

    public List<String> getActionName() {
        return actionName;
    }

    public void setActionName(List<String> actionName) {
        this.actionName = actionName;
    }

    // 扫描得到的方法名称列表
    private List<String> actionName;

    public void setClassesPath(List<String> classesPath) {
        this.classesPath = classesPath;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public Node getRoot() {

        return root;
    }

    public void setAnnotationScan(Boolean annotationScan) {
        this.annotationScan = annotationScan;
    }

    public Boolean isAnnotationScan() {
        return annotationScan;
    }

    public List<String> getClassesPath() {
        return classesPath;
    }

    public void addClassPath(String classPath) {
        if(classPath != null) {
            classesPath.add(classPath);
        }
    }
}
