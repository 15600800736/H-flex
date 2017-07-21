package com.frame.context;

import java.util.Map;

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
     * split information in the resource into different resources
     * @param resources
     * @return if the Resource can't be split, it will return false, if it can, true.
     */
    <T extends Resource> Boolean split(T...resources);

    /**
     * Inject the information in this resource to another resource
     * There is no guarantee that all of information of this resource can be injected
     * @param resource
     * @return the count of information injected
     */
    <T extends Resource> Integer join(T resource);

    /**
     * @param name
     * @return whether the resource has the specify information
     */
    Boolean hasInformation(String name);

    /**
     * get the specify information
     * @param name
     * @return
     */
    Object getInformation(String name);

    /**
     * get the specify information with specify class
     * @param name
     * @param infoClass
     * @param <T>
     * @return
     */
    <T> T getInformation(String name, Class<T> infoClass);

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
}
