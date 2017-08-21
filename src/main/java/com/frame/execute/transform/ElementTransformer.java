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
public class ElementTransformer extends Transformer<List<Element>,List<ConfigurationNode>> {

    public ElementTransformer() {
    }

    public ElementTransformer(List<Element> production) {
        super(production);
    }

    @Override
    public List<ConfigurationNode> exec() throws Exception{
        List<ConfigurationNode> nodes = new LinkedList<>();
        for(Element element : production) {
            ConfigurationNode node = new ConfigurationNode(element);
            if(node != null) {
                nodes.add(node);
            }
        }
        return nodes;
    }
}
