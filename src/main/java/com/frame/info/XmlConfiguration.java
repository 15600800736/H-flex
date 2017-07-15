package com.frame.info;

import java.util.List;
import java.util.Map;

/**
 * Created by fdh on 2017/7/3.
 */
public class XmlConfiguration {
    // 根节点
    private Node root;
    // 是否开启了注解扫描
    private Boolean annotationScan;
    // 扫描的类列表
    private Map<String,String> classesPath;

    // 扫描得到的方法名称映射
    private Map<String,String> actions;
    // 方法名对应的类名映射
    private Map<String,String> actionClassMapper;

    public Boolean getAnnotationScan() {
        return annotationScan;
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

    public Map<String, String> getClassesPath() {
        return classesPath;
    }

    public void setClassesPath(Map<String, String> classesPath) {
        this.classesPath = classesPath;
    }

    public Map<String, String> getActions() {
        return actions;
    }

    public void setActions(Map<String, String> actions) {
        this.actions = actions;
    }

    public Map<String, String> getActionClassMapper() {
        return actionClassMapper;
    }

    public void setActionClassMapper(Map<String, String> actionClassMapper) {
        this.actionClassMapper = actionClassMapper;
    }

    public String getActionAbsolutePath(String alias) {
        return actions.get(alias);
    }

    public String getActionClassAbsolutePath(String alias) {
        return actionClassMapper.get(alias);
    }
}
