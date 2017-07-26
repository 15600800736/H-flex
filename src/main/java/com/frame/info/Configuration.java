package com.frame.info;

import com.frame.context.resource.MapperResource;
import com.frame.context.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by fdh on 2017/7/3.
 */

/**
 * <p>The configuration records all information you can configured, no matter in xml or java code.
 * So, it extends the abstract class MapperSource, So it can offer information as resource, use
 * {@code setInformation} or {@code getInformation}, It also can be split by {@link ActionInfo} and
 * {@link ActionGroupInfo}</p>
 * <p>The information of Configuration also can be extract from other resource because it implements Extractor</p>
 */
public class Configuration extends MapperResource {
    /**
     * root node
     */
    private Node root;
    /**
     * whether the annotation-scanning is enabled
     */
    private AtomicBoolean annotationScan = new AtomicBoolean();
    /**
     * the class of action mapper: name -> path
     */
    private Map<String, String> classesPathMapper = new HashMap<>(64);
    /**
     * the action's name mapper: id -> name
     */
    private Map<String, String> actions = new HashMap<>(256);
    /**
     * the name of action's class mapper: id -> classname
     */
    private Map<String, String> actionClassMapper = new HashMap<>(256);
    /**
     * the action's alias mapper: alias -> id
     */
    private Map<String, String> aliasMapper = new HashMap<>(512);
    /**
     * Map type to its alias: alias -> type
     */
    private Map<String, String> typeAliasMapper = new HashMap<>(64);
    /**
     * the count of the direct child node (<action-groups/><action-register/>)
     */
    private Integer countOfResourcesType = 2;


    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public Configuration() {
        initResourceOrder();
        initInformationRequired();
    }

    protected void initResourceOrder() {
        resourcesOrder.put(ActionInfoHolder.class, 0);
        resourcesOrder.put(ActionGroupInfo.class, 1);
    }

    @Override
    protected void initInformationRequired() {
        // wait to fix

    }

    // setter lock
    private Lock appendClassesMapLock = new ReentrantLock();
    private Lock appendActionsMapLock = new ReentrantLock();
    private Lock appendActionClassMapLock = new ReentrantLock();
    private Lock appendAliasMapLock = new ReentrantLock();
    private Lock appendTypeAliasMapLock = new ReentrantLock();

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

    public void appendClass(Map<String, String> classesPath) {
        appendClassesMapLock.lock();
        this.classesPathMapper.putAll(classesPath);
        appendClassesMapLock.unlock();
    }

    public void appendAction(Map<String, String> actions) {
        appendActionsMapLock.lock();
        this.actions.putAll(actions);
        appendActionsMapLock.unlock();
    }

    public void appendActionClass(Map<String, String> actionClassMapper) {
        appendActionClassMapLock.lock();
        this.actionClassMapper.putAll(actionClassMapper);
        appendActionClassMapLock.unlock();
    }

    public void appendAlias(Map<String, String> aliasMapper) {
        appendAliasMapLock.lock();
        this.aliasMapper.putAll(aliasMapper);
        appendAliasMapLock.unlock();
    }
    public void appendTypeAlias(Map<String, String> typeAliasMapper) {
        appendTypeAliasMapLock.lock();
        this.aliasMapper.putAll(typeAliasMapper);
        appendTypeAliasMapLock.unlock();
    }

    public void appendClass(String name, String path) {
        appendClassesMapLock.lock();
        this.classesPathMapper.put(name, path);
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

    public void appendTypeAlias(String alias, String name) {
        appendTypeAliasMapLock.lock();
        this.aliasMapper.put(alias, name);
        appendTypeAliasMapLock.unlock();
    }
    @Override
    public <T extends Resource> Boolean split(T[] resources) {
        Boolean canSplit = resources != null && resources.length == 2 &&
                ((resources[0] instanceof ActionInfoHolder && resources[1] instanceof ActionGroupInfo) ||
                        (resources[0] instanceof ActionGroupInfo && resources[1] instanceof ActionInfoHolder));
        if (canSplit) {
            // get the split type and instance
            Resource[] orderedResources = transformResourcesToSpecificOrder(resources);
            ActionInfoHolder actionInfoHolder = (ActionInfoHolder) orderedResources[resourcesOrder.get(ActionInfoHolder.class)];
            ActionGroupInfo actionGroupInfo = (ActionGroupInfo) orderedResources[resourcesOrder.get(ActionGroupInfo.class)];

        } else {
            if (logger.isWarnEnabled()) {
                StringBuilder errorMessage = new StringBuilder("configuration can't be split by ");
                for (T resource : resources) {
                    errorMessage.append(resource.getType() + " ");
                }
            }
        }
        return canSplit;
    }

    @Override
    public Resource[] transformResourcesToSpecificOrder(Resource... resources) {
        Resource[] orderedResource = new Resource[countOfResourcesType];
        for (Resource resource : resources) {
            Integer index = resourcesOrder.get(resource.getClass());
            if (index == null) {
                return null;
            }
            orderedResource[index] = resource;
        }
        return orderedResource;
    }

    public Map<String, String> getClassesPath() {
        return classesPathMapper;
    }
}


