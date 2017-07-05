package com.frame.scan;

import com.frame.info.ConfigurationNode;
import com.frame.info.XmlConfiguration;
import org.dom4j.Element;

import javax.security.auth.login.Configuration;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by fdh on 2017/7/2.
 */
public class BaseContentScanner implements Scanner {
    private ConfigurationNode base_contents;

    public BaseContentScanner(ConfigurationNode base_contents) {
        this.base_contents = base_contents;
    }
    @Override
    public XmlConfiguration scan() {
        List<ConfigurationNode> pathNodeList = base_contents.getChildren("path");
        List<String> paths = new LinkedList<>();
        for(ConfigurationNode pathNode : pathNodeList) {
            String text = pathNode.getText();

            paths.add(text);
        }
        for(String path : paths) {

        }
        return null;
    }

    private List<Class<?>> getClassInPath(List<String> paths) {
        return null;
    }
}
