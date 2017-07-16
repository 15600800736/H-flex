package com.frame.info;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by fdh on 2017/7/3.
 */
public class Configuration {
    // 根节点
    private Node root;
    // 是否开启了注解扫描
    private AtomicBoolean annotationScan;

    // 扫描的类列表 name -> path
    private Map<String,String> classesPath = new HashMap<>(64);
    // 扫描得到的方法名称映射 name -> path
    private Map<String,String> actions = new HashMap<>(256);
    // 方法名对应的类名映射 name -> classname
    private Map<String,String> actionClassMapper = new HashMap<>(256);
    // 别名映射 alias -> name
    private Map<String,String> aliasMapper = new HashMap<>(512);

    // 增加映射的锁
    private Lock appendClassesMapLock = new ReentrantLock();
    private Lock appendActionsMapLock = new ReentrantLock();
    private Lock appendActionClassMapLock = new ReentrantLock();
    private Lock appendAliasMap = new ReentrantLock();
    public void setRoot(Node root) {
        this.root = root;
    }

    public Node getRoot() {
        return root;
    }

    public void setAnnotationScan(Boolean annotationScan) {
        this.annotationScan.set(annotationScan);
    }

    public Boolean isAnnotationScan() {
        return annotationScan.get();
    }

    public void appendClassesPath(Map<String, String> classesPath) {
        if(appendClassesMapLock.tryLock())
        this.classesPath.putAll(classesPath);
    }

    public void appendActions(Map<String, String> actions) {
        this.actions.putAll(actions);
    }

    public void appendActionClassMapper(Map<String, String> actionClassMapper) {
        this.actionClassMapper.putAll(actionClassMapper);
    }

    public void appendAliasMapper(Map<String,String> aliasMapper) {
        this.aliasMapper.putAll(aliasMapper);
    }

    public void setAnnotationScan(AtomicBoolean annotationScan) {
        this.annotationScan = annotationScan;
    }
    public void setAliasMapper(Map<String, String> aliasMapper) {
        this.aliasMapper = aliasMapper;
    }

    public String getActionClassAbsolutePath(String name) {
        return actionClassMapper.get(name);
    }

    public String getActionAbsolutePath(String name) {
        return actions.get(name);
    }

    public String getClassPath(String name) {
        return actions.get(name);
    }

    public String getActionName(String alias) {
        return aliasMapper.get(alias);
    }
}
