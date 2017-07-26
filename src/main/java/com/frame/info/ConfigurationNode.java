package com.frame.info;

import com.frame.execute.transform.ElementTransformer;
import com.frame.execute.transform.Transformer;
import org.dom4j.Attribute;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by fdh on 2017/7/3.
 */
public class ConfigurationNode extends Node {
    private final String rootName = "frame-haug";

    private Element element;

    // 是否是根节点
    private AtomicBoolean root;
    // 是否有子节点
    private AtomicBoolean hasChild;
    // 是否有属性
    private AtomicBoolean hasAttribute;

    public ConfigurationNode(Element element) {
        this.element = element;
        if (element != null) {
            this.root = new AtomicBoolean(this.element.getName().equals(rootName));
            this.hasChild = new AtomicBoolean(this.element.elements().size() > 0);
            this.hasAttribute = new AtomicBoolean(this.element.attributes().size() > 0);
        }
    }

    @Override
    public String getName() {
        return element.getName();
    }

    @Override
    public String getText() {
        return element.getText();
    }

    @Override
    public ConfigurationNode getChild(String name) {
        Element child = element.element(name);
        return child == null ? null : new ConfigurationNode(child);
    }

    @Override
    public List<ConfigurationNode> getChildren(String name){
        List<Element> elements = element.elements(name);
        List<ConfigurationNode> children = null;
        try {
            children = decorate(elements);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return children;
    }

    @Override
    public List<ConfigurationNode> getAllChildren(){
        List<Element> elements = element.elements();
        List<ConfigurationNode> children = null;
        try {
            children = decorate(elements);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return children;
    }

    @Override
    public String getAttributeText(String name) {
        Attribute attribute = element.attribute(name);
        return attribute == null ? null : attribute.getText();
    }

    @Override
    public Map<String, String> getAllAttributes() {
        List<Attribute> attributes = element.attributes();
        Map<String, String> attributesMap = new HashMap<>();
        attributes.forEach( attribute -> attributesMap.put(attribute.getName(), attribute.getValue()));
        return attributesMap;
    }

    @Override
    public ConfigurationNode getParent() {
        Element parent = element.getParent();
        return parent == null ? null : new ConfigurationNode(parent);
    }

    @Override
    public Boolean isRoot() {
        return root.get();
    }

    @Override
    public Boolean hasChild() {
        return hasChild.get();
    }

    @Override
    public Boolean hasAttribute() {
        return hasAttribute.get();
    }

    @Override
    public Boolean hasChild(String name) {
        return element.element(name) != null;
    }

    @Override
    public Boolean hasAttribute(String name) {
        return element.attribute(name) != null;
    }

    private List<ConfigurationNode> decorate(List<Element> original) throws Exception {
        Transformer<List<ConfigurationNode>> transformer = new ElementTransformer(original);
        return transformer.execute();
    }
}
