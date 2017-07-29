package com.frame.execute.transform;

import com.frame.context.resource.Resource;
import com.frame.context.info.StringInfomation.ConfigurationNode;

import org.dom4j.Element;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by fdh on 2017/7/4.
 */
public class ElementTransformer extends Transformer<List<ConfigurationNode>> {

    private List<Element> elements;

    public ElementTransformer(List<Element> elements) {
        this.elements = elements;
    }

    public ElementTransformer(CyclicBarrier barrier, List<Element> elements) {
        super(barrier);
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
}
