package com.frame.parse.parser;

import com.frame.exceptions.ParseException;
import com.frame.info.ActionInfoHolder;
import com.frame.info.Configuration;
import org.dom4j.Element;

import java.util.List;

/**
 * Created by fdh on 2017/7/3.
 */
public class ConfigurationParser implements Parser {

    private Element root;
    private Configuration configuration;

    public ConfigurationParser(Element root, Configuration configuration) {
        this.root = root;
        this.configuration = configuration;
    }
    @Override
    public Object parse(ActionInfoHolder actionInfo) throws ParseException {
        registerActionClass(root.element("action-class"), configuration);
        registerBaseContent(root.element("base-content"), configuration);
        return null;
    }

    public void registerActionClass(Element action_class, Configuration configuration) throws ParseException {
        if(action_class == null) {
            return;
        }
        if(configuration == null) {
            throw new ParseException(null,"configuration为空");
        }
    }
    public void registerBaseContent(Element base_content, Configuration configuration) {

    }
}
