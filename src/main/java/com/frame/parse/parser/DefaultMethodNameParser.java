
package com.frame.parse.parser;

import com.frame.exceptions.ParseException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DefaultMethodNameParser implements Parser {
    @Override
    public Object parse(Object... objects) {

        String source = (String) objects[0];
        String paramsName = (String) objects[1];
        Object[] args = (Object[]) objects[2];
        source = source.trim();
        Parser parser = null;
        Method targetMethod = null;
        if(source.endsWith("()")) {
            source = source.substring(0,source.length() - 2);
            if(source.contains("*") || source.contains("?")) {
                parser = new MethodNameFuzzyParser();
            } else {
                parser = new MethodNameExactlyParser();
            }
            if(parser != null) {
                try {
                    targetMethod = (Method) parser.parse(source, paramsName, args);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        } else {
            parser = new MethodAliasParser();
            try {
                targetMethod = (Method) parser.parse(source);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if(targetMethod != null) {

        } else {

        }
        return null;
    }

}