package com.frame.execute.transform;

import com.frame.info.ConfigurationNode;

import org.dom4j.Element;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by fdh on 2017/7/4.
 */
public class ElementTransformer implements Transformer<List<ConfigurationNode>> {

    private List<Element> elements;

    public ElementTransformer(List<Element> elements) {
        this.elements = elements;
    }
    @Override
    public List<ConfigurationNode> transform() {
        List<ConfigurationNode> nodes = new LinkedList<>();
        for(Element element : elements) {
            ConfigurationNode node = new ConfigurationNode(element);
            if(node != null) {
                nodes.add(node);
            }
        }
        return nodes;
    }

    @Override
    public void prepareForExecute() {

    }

    @Override
    public void postProcessForExccute() {

    }
}
