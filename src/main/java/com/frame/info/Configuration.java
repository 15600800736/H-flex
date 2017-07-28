package com.frame.info;

import com.frame.annotations.Action;
import com.frame.context.resource.MapperResource;
import com.frame.context.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
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
     * <p>Map type to its alias: alias -> type</p>
     */
    private ConcurrentMap<String, String> typeAliasMapper = new ConcurrentHashMap<>(64);
    /**
     * <p>the count of the direct child node (<action-groups/><action-register/>)</p>
     */
    private Integer countOfResourcesType = 2;


    /**
     * <p>Has the configuration been well-registered, means if all the actions info has been injected</p>
     */
    private AtomicBoolean registered = new AtomicBoolean(false);

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
        return this.actions.putIfAbsent(id, actionInfo );
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
    /**/

    public ConcurrentMap<String, ActionInfo> getActions() {
        return actions;
    }

    public ConcurrentMap<String, String> getClassesPathMapper() {
        return classesPathMapper;
    }

    /**/

    public Boolean isRegisterd() {
        return registered.get();
    }

    public void setIsRegisterd(Boolean isRegisterd) {
        this.registered.set(isRegisterd);
    }
}


