
package com.frame.execute.parse.parser;

import com.frame.exceptions.ScanException;
import com.frame.info.ActionInfoHolder;
import com.frame.info.Configuration;

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
            } catch (ScanException e) {
                e.printStackTrace();
            }
        }
        return targetMethod;
    }

    @Override
    public Configuration getConfiguration() {
        return null;
    }

    @Override
    public void prepareForExecute() {

    }

    @Override
    public void postProcessForExccute() {

    }
}