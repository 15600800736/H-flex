package com.frame.context.resource;

import com.frame.exceptions.CastException;
import com.frame.info.Information;

/**
 * Created by fdh on 2017/7/17.
 */

/**
 * Represents all of the structure that can give another class information
 * Resource can be merged into another resource, or split into multi resources
 *
 */
public interface Resource {
    /**
     * Split information in the resource into different resources, after split,
     * the original resource stays as the same.
     * @param resources the resources that receive the info from original resource.
     * @return if the Resource can't be split, it will return false, if it can, true.
     */
    <T extends Resource> Boolean split(T...resources);

    /**
     * Inject the information in this resource to another resource
     * There is no guarantee that all of information of this resource can be injected
     * @param resource the resource that this one is about to join in.
     * @return the count of information injected
     */
    <T extends Resource> Integer join(T resource);

    /**
     * @param name the name of information
     * @return whether the resource has the specify information
     */
    Boolean hasInformation(String name);

    /**
     * get the specify information
     * @param name
     * @return
     */
    Information getInformation(String name) throws Exception;

    /**
     * get the specify information with specify class
     * @param name
     * @param infoClass
     * @return
     */
    <T> T getInformation(String name, Class<T> infoClass) throws Exception;

    /**
     * add an information with specify name
     * @param name
     * @param value
     */
    void setInformation(String name, Object value);

    /**
     * add an information with specify name and class
     * @param name
     * @param value
     * @param infoClass
     */
    <T> void setInformation(String name, Object value, Class<T> infoClass);

    /**
     * get the resource class's name
     * @return
     */
    String getType();

    /**
     *  This method is used to transform the given resources to specify order that can be
     *  execute
     * @param resources
     */
    Resource[] transformResourcesToSpecificOrder(Resource...resources);
}
