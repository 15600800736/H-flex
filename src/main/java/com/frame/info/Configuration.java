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
    private Lock appendAliasMapLock = new ReentrantLock();
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
        appendClassesMapLock.lock();
        this.classesPath.putAll(classesPath);
        appendClassesMapLock.unlock();
    }

    public void appendActions(Map<String, String> actions) {
        appendActionsMapLock.lock();
        this.actions.putAll(actions);
        appendActionsMapLock.unlock();
    }

    public void appendActionClassMapper(Map<String, String> actionClassMapper) {
        appendActionClassMapLock.lock();
        this.actionClassMapper.putAll(actionClassMapper);
        appendActionClassMapLock.unlock();
    }

    public void appendAliasMapper(Map<String,String> aliasMapper) {
        appendAliasMapLock.lock();
        this.aliasMapper.putAll(aliasMapper);
        appendAliasMapLock.unlock();
    }

    public void appendClass(String name, String path) {
        appendClassesMapLock.lock();
        this.actionClassMapper.put(name,path);
        appendClassesMapLock.unlock();
    }

    public void appendAction(String id, String path) {
        appendActionsMapLock.lock();
        this.actions.put(id,path);
        appendActionsMapLock.unlock();
    }

    public void appendActionClass(String id, String classPath) {
        appendActionClassMapLock.lock();
        this.actionClassMapper.put(id, classPath);
        appendActionClassMapLock.unlock();
    }

    public void appendAlias(String alias, String id) {
        appendAliasMapLock.lock();
        this.aliasMapper.put(alias, id);
        appendAliasMapLock.unlock();
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
