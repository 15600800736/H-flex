package com.frame.info;

import com.frame.annotations.ActionGroup;
import com.frame.context.MapperResource;
import com.frame.context.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by fdh on 2017/7/3.
 */
public class Configuration extends MapperResource {
    // 根节点
    private Node root;
    // 是否开启了注解扫描
    private AtomicBoolean annotationScan = new AtomicBoolean();
    // 扫描的类列表 name -> path
    private Map<String, String> classesPath = new HashMap<>(64);
    // 扫描得到的方法名称映射 id -> name
    private Map<String, String> actions = new HashMap<>(256);
    // 方法名对应的类名映射 id -> classname
    private Map<String, String> actionClassMapper = new HashMap<>(256);
    // 别名映射 alias -> id
    private Map<String, String> aliasMapper = new HashMap<>(512);

    private Logger logger = LoggerFactory.getLogger(this.getClass());

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
        if (annotationScan == null) {
            return null;
        }
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

    public void appendAliasMapper(Map<String, String> aliasMapper) {
        appendAliasMapLock.lock();
        this.aliasMapper.putAll(aliasMapper);
        appendAliasMapLock.unlock();
    }

    public void appendClass(String name, String path) {
        appendClassesMapLock.lock();
        this.actionClassMapper.put(name, path);
        appendClassesMapLock.unlock();
    }

    public void appendAction(String id, String name) {
        appendActionsMapLock.lock();
        this.actions.put(id, name);
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


    public AtomicBoolean getAnnotationScan() {
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

    public Map<String, String> getAliasMapper() {
        return aliasMapper;
    }

    public Lock getAppendClassesMapLock() {
        return appendClassesMapLock;
    }

    public void setAppendClassesMapLock(Lock appendClassesMapLock) {
        this.appendClassesMapLock = appendClassesMapLock;
    }

    public Lock getAppendActionsMapLock() {
        return appendActionsMapLock;
    }

    public void setAppendActionsMapLock(Lock appendActionsMapLock) {
        this.appendActionsMapLock = appendActionsMapLock;
    }

    public Lock getAppendActionClassMapLock() {
        return appendActionClassMapLock;
    }

    public void setAppendActionClassMapLock(Lock appendActionClassMapLock) {
        this.appendActionClassMapLock = appendActionClassMapLock;
    }

    public Lock getAppendAliasMapLock() {
        return appendAliasMapLock;
    }

    public void setAppendAliasMapLock(Lock appendAliasMapLock) {
        this.appendAliasMapLock = appendAliasMapLock;
    }

    @Override
    public <T extends Resource> Boolean split(T[] resources) {
        Boolean canSplit = resources.length > 1 &&
                ((resources[0] instanceof ActionInfo && resources[1] instanceof ActionGroupInfo) ||
                        (resources[0] instanceof ActionGroup && resources[1] instanceof ActionInfo));
        if(canSplit) {

        } else {
            if(logger.isWarnEnabled()) {
                logger.warn("configuration can't be split by ");
            }
        }
        return canSplit;
    }

    @Override
    public <T extends Resource> Integer join(T resource) {
        return null;
    }
}


