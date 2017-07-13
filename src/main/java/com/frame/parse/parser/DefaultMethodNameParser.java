
package com.frame.parse.parser;

import com.frame.exceptions.ParseException;
import com.frame.info.ActionInfoHolder;

import java.lang.reflect.Method;

public class DefaultMethodNameParser implements Parser {

    private String source;
    private String paramsName;
    private Object[] args;

    public DefaultMethodNameParser(String source, String paramsName, Object[] args) {
        this.source = source;
        this.paramsName = paramsName;
        this.args = args;
    }

    @Override
    public Object parse(ActionInfoHolder actionInfo) {
        source = source.trim();
        Parser parser = null;
        Method targetMethod = null;
        if(source.contains("*") || source.contains("?")) {
            parser = new MethodNameFuzzyParser(source,paramsName,args);
        } else {
            parser = new MethodNameExactlyParser(source,paramsName,args);
        }
        if(parser != null) {
            try {
                targetMethod = (Method) parser.parse(actionInfo);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return targetMethod;
    }

}