package com.frame.context.info.StringInformation;

import com.frame.context.info.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by fdh on 2017/7/3.
 */

/**
 * <p>The configuration records all information you can configured, no matter from xml or java code.
 * It stores all information as string type.</p>
 */
public class Configuration {
    /**
     * <p>root node, the configuration keep a node as root, from which you can reach all of the information.</p>
     * <p>So, no matter what form the configuration is, it should seems like a tree.</p>
     */
    private Node root;
    /**
     * <p>whether the annotation-scanning is enabled</p>
     */
    private AtomicBoolean annotationScan = new AtomicBoolean();
    /**
     * <p>the class of action: name -> path</p>
     */
    private ConcurrentMap<String, String> classesPathMapper = new ConcurrentHashMap<>(64);
    /**
     * <p>the actions: id -> action's info</p>
     */
    private ConcurrentMap<String, ActionInfo> actions = new ConcurrentHashMap<>(256);

    /**
     * <p>The type aliases is a short representation of a type, for convenience: alias -> type</p>
     */
    private ConcurrentMap<String, String> typeAliases = new ConcurrentHashMap<>(256);

    /**
     * <p>Executions contains all of the executions, which is invoked by a field: action alias -> execution's info</p>
     */
    private ConcurrentMap<String, ExecutionInfo> executions = new ConcurrentHashMap<>(256);

    /**
     * <p></p>
     */
    private ConcurrentMap<String, String> executionClassesPath = new ConcurrentHashMap<>(64);
    /**
     * <p>Has the configuration been well-registered, means if all the actions' and executions' info has been injected</p>
     */
    private AtomicBoolean registered = new AtomicBoolean(false);

    /**
     * <p>action alias -> action id</p>
     */
    private ConcurrentMap<String, String> actionAlias = new ConcurrentHashMap<>(1024);

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Constructor
     */
    public Configuration() {
    }
    /**
     * Getters and setters
     */

    /**
     * <p>Set the configuration's node, other scanner will get the root by calling {@code getRoot()}</p>
     * @param root
     */
    public void setRoot(Node root) {
        this.root = root;
    }

    /**
     * <p>Get the root node of the configuration to get other nodes</p>
     * @return
     */
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

    public ConcurrentMap<String, ExecutionInfo> getExecutions() {
        return executions;
    }

    public ConcurrentMap<String, String> getExecutionClassesPath() {
        return executionClassesPath;
    }

    public ConcurrentMap<String, String> getActionAlias() {
        return actionAlias;
    }

    public Boolean isRegisterd() {
        return registered.get();
    }

    public void setIsRegisterd(Boolean isRegisterd) {

        this.registered.set(isRegisterd);
    }

    public void setActionAlias(ConcurrentMap<String, String> actionAlias) {
        this.actionAlias = actionAlias;
    }

/**
     * Append info into maps
     */

    /**
     * append action's classes
     * @param name action's class name, may be full name of class, or the name attribute.
     * @param path the path of action's class
     * @return
     */
    public String appendClazz(String name, String path) {
        return this.classesPathMapper.putIfAbsent(name, path);
    }

    /**
     * append actions
     * @param id action's id, the only identify of an action
     * @param actionInfo information of an action
     * @return
     */
    public ActionInfo appendAction(String id, ActionInfo actionInfo) {
        return this.actions.putIfAbsent(id, actionInfo);
    }

    /**
     * append type alias
     * @param alias the type alias
     * @param name the full name of the an class
     * @return
     */
    public String appendTypeAlias(String alias, String name) {
        return this.typeAliases.putIfAbsent(alias, name);
    }

    /**
     * append execution's class
     * @param name execution's class name, may be full name of class, or the name attribute.
     * @param path the full path of the class
     * @return
     */
    public String appendExecutionClazz(String name, String path) {
        return this.executionClassesPath.putIfAbsent(name, path);
    }

    /**
     * append executions
     * @param name the field name(include field's class) that the execution bonding with.
     * @param execution the info of an execution
     * @return
     */
    public Boolean appendExecution(String name, ExecutionInfo execution) {
        ExecutionInfo ei = this.executions.get(name);
        if (ei == null) {
            this.executions.putIfAbsent(name, execution);
            return true;
        } else {
            if (ei.executions == null) {
                ei.executions = new HashSet<>(128);
            }
            ei.fieldName = execution.fieldName;
            ei.actionClass = execution.actionClass;
            return ei.executions.addAll(execution.executions);
        }
    }

    public Boolean appendAlias(List<String> alias, String methodId) {
        for (String a : alias) {
            if (this.actionAlias.putIfAbsent(a, methodId) != null) {
                return false;
            }
        }
        return true;
    }

}


