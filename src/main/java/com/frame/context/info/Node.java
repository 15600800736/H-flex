package com.frame.context.info;

import com.frame.context.info.StringInfomation.ConfigurationNode;

import java.util.List;
import java.util.Map;

/**
 * Created by fdh on 2017/7/5.
 */
public abstract class Node {
    public abstract String getName();

    public abstract String getText();

    public abstract <T extends Node> T getChild(String name);

    public abstract <T extends Node> List<T> getChildren(String name) throws Exception;

    public abstract <T extends Node> List<T> getAllChildren() throws Exception;

    public abstract String getAttributeText(String name);

    public abstract Map<String, String> getAllAttributes();

    public abstract ConfigurationNode getParent();

    public abstract Boolean isRoot();

    public abstract Boolean hasChild();

    public abstract Boolean hasAttribute();

    public abstract Boolean hasChild(String name);

    public abstract Boolean hasAttribute(String name);
}
