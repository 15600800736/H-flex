package com.frame.info;

import com.frame.transform.ElementTransformer;
import com.frame.transform.Transformer;
import org.dom4j.Attribute;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fdh on 2017/7/3.
 */
public class ConfigurationNode extends Node{
    private final String rootName = "frame-haug";

    private Element element;

    private Boolean root;

    private Boolean hasChild;

    private Boolean hasAttribute;

    public ConfigurationNode(Element element) {
        this.element = element;
        if (element != null) {
            this.root = this.element.getName().equals(rootName);
            this.hasChild = this.element.elements().size() > 0;
            this.hasAttribute = this.element.attributes().size() > 0;
        }
    }

    @Override
    public String getText() {
        return element.getText();
    }

    @Override
    public ConfigurationNode getChild(String name) {
        Element child = element.element(name);
        return new ConfigurationNode(child);
    }

    @Override
    public List<ConfigurationNode> getChildren(String name) {
        List<Element> elements = element.elements(name);
        return decorate(elements);
    }

    @Override
    public List<ConfigurationNode> getAllChildren() {
        List<Element> elements = element.elements();
        return decorate(elements);
    }

    @Override
    public String getAttributeText(String name) {
        Attribute attribute = element.attribute(name);
        return attribute.getText();
    }

    @Override
    public Map<String, String> getAllAttributes() {
        List<Attribute> attributes = element.attributes();
        Map attributesMap = new HashMap();
        for (Attribute attribute : attributes) {
            attributesMap.put(attribute.getName(), attribute.getValue());
        }
        return attributesMap;
    }

    @Override
    public ConfigurationNode getParent() {
        return new ConfigurationNode(element.getParent());
    }

    @Override
    public Boolean isRoot() {
        return root;
    }

    @Override
    public Boolean isHasChild() {
        return hasChild;
    }

    @Override
    public Boolean isHasAttribute() {
        return hasAttribute;
    }

    private List<ConfigurationNode> decorate(List<Element> original) {
        Transformer<List<ConfigurationNode>> transformer = new ElementTransformer(original);
        return transformer.transform();
    }
}
