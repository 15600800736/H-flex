
package com.frame.parse.parser;

import com.frame.exceptions.ParseException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class DefaultMethodNameParser implements Parser {
    @Override
    public Object parse(Object... objects) {
        String source = (String) objects[0];
        String paramsName = (String) objects[1];
        Object[] args = Arrays.copyOfRange(objects, 2, objects.length);
        source = source.trim();
        Parser parser = null;
        Method targetMethod = null;
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
        System.out.println(targetMethod);
        return null;
    }

}