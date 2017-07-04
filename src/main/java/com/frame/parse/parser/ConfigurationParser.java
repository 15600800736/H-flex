package com.frame.parse.parser;

import com.frame.exceptions.ParseException;
import com.frame.parse.infoholder.XmlConfiguration;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.List;

/**
 * Created by fdh on 2017/7/3.
 */
public class ConfigurationParser implements Parser {

    private Element root;
    private XmlConfiguration configuration;

    public ConfigurationParser(Element root, XmlConfiguration configuration) {
        this.root = root;
        this.configuration = configuration;
    }
    @Override
    public Object parse() throws ParseException {
        registerActionClass(root.element("action-class"), configuration);
        registerBaseContent(root.element("base-content"), configuration);
        return null;
    }

    public void registerActionClass(Element action_class, XmlConfiguration configuration) throws ParseException {
        if(action_class == null) {
            return;
        }
        if(configuration == null) {
            throw new ParseException(null,"configuration为空");
        }
        List<Element> pathList = root.elements("path");
        for(Element path : pathList) {
            configuration.addActionClass(path.getText());
        }
    }
    public void registerBaseContent(Element base_content, XmlConfiguration configuration) {

    }
}
