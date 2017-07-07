package com.frame.info;

import java.util.List;

/**
 * Created by fdh on 2017/7/3.
 */
public class XmlConfiguration {
    // 根节点
    private ConfigurationNode root;
    // 是否开启了注解扫描
    private Boolean isAnnotationScan;
    // 扫描的类列表
    private List<String> classesPath;


    public void setClassesPath(List<String> classesPath) {
        this.classesPath = classesPath;
    }

    public void setRoot(ConfigurationNode root) {

        this.root = root;
    }

    public ConfigurationNode getRoot() {

        return root;
    }

    public void setAnnotationScan(Boolean annotationScan) {
        isAnnotationScan = annotationScan;
    }

    public Boolean getAnnotationScan() {
        return isAnnotationScan;
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
