package com.frame.context.info.StringInfomation;

import com.frame.annotations.Execution;
import com.frame.context.info.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by fdh on 2017/7/3.
 */

/**
 * <p>The configuration records all information you can configured, no matter in xml or java code.
 * So, it extends the abstract class MapperSource, So it can offer information as resource, use
 * {@code setInformation} or {@code getInformation}, It also can be split by {@link ActionInfo} and
 * <p>The information of Configuration also can be extract from other resource because it implements Extractor</p>
 */
public class Configuration {
    /**
     * <p>root node</p>
     */
    private Node root;
    /**
     * <p>whether the annotation-scanning is enabled</p>
     */
    private AtomicBoolean annotationScan = new AtomicBoolean();
    /**
     * <p>the class of action mapper: name -> path</p>
     */
    private ConcurrentMap<String, String> classesPathMapper = new ConcurrentHashMap<>(64);
    /**
     * <p>the action's name mapper: id -> name</p>
     */
    private ConcurrentMap<String, ActionInfo> actions = new ConcurrentHashMap<>(256);
    /**
     * <p>the count of the direct child node (<action-groups/><action-register/>)</p>
     */
    private Integer countOfResourcesType = 2;

    /**
     * <p>The type aliases is a short representation of a type, for convenience: alias -> type</p>
     */
    private ConcurrentMap<String, String> typeAliases = new ConcurrentHashMap<>(256);

    /**
     *
     */
    private ConcurrentMap<String, ExecutionInfo> executions = new ConcurrentHashMap<>(256);

    /**
     *
     */
    private ConcurrentMap<String, String> executionClassesPath = new ConcurrentHashMap<>(64);
    /**
     * <p>Has the configuration been well-registered, means if all the actions info has been injected</p>
     */
    private AtomicBoolean registered = new AtomicBoolean(false);

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public Configuration() {
    }


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

    public String appendClass(String name, String path) {
        return this.classesPathMapper.putIfAbsent(name, path);
    }

    public ActionInfo appendAction(String id, ActionInfo actionInfo) {
        return this.actions.putIfAbsent(id, actionInfo);
    }

    public String appendTypeAlias(String alias, String name) {
        return this.typeAliases.putIfAbsent(alias, name);
    }

    public String appendExecutionClass(String name, String path) {
        return this.executionClassesPath.putIfAbsent(name, path);
    }

    public Boolean appendExecution(String name, ExecutionInfo execution) {

        return false;
    }

    public String getType(String alias) {
        return this.typeAliases.get(alias);
    }


    public ConcurrentMap<String, ActionInfo> getActions() {
        return actions;
    }

    public ConcurrentMap<String, String> getClassesPathMapper() {
        return classesPathMapper;
    }

    public ConcurrentMap<String, String> getTypeAliases() {
        return typeAliases;
    }


    public Boolean isRegisterd() {
        return registered.get();
    }

    public void setIsRegisterd(Boolean isRegisterd) {
        this.registered.set(isRegisterd);
    }
}


