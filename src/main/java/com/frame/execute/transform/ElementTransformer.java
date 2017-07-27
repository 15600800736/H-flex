package com.frame.execute.transform;

import com.frame.context.resource.Resource;
import com.frame.info.ConfigurationNode;

import org.dom4j.Element;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by fdh on 2017/7/4.
 */
public class ElementTransformer extends Transformer<List<ConfigurationNode>> {

    private List<Element> elements;

    public ElementTransformer(List<Element> elements) {
        this.elements = elements;
    }
    @Override
    public List<ConfigurationNode> exec() throws Exception{
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
    public Resource[] getResources() {
        return new Resource[0];
    }
}
