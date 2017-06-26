
package com.frame.parse.parser;

import com.frame.exceptions.ParseException;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodNameExactlyParser implements Parser {

    @Override
    public Object parse(Object... objects) throws ParseException {
        String source = (String) objects[0];
        String paramsName = (String) objects[1];
        Object[] args = (Object[]) objects[2];
        if (!isSourceValid(source)) {
            throw new ParseException("*.*.*.*", "方法名格式错误");
        }
        String[] cells = source.split("\\.");
        String methodName = cells[cells.length - 1];
        Class<?> methodClazz = getClazzBySource(cells);
        Parser parser = new OverloadMethodParser();
        return parser.parse(methodClazz,methodName,paramsName,args);

    }

    private Boolean isSourceValid(String source) {
        source = source.trim();
        Pattern methodCompleteNamePattern = Pattern.compile("^[A-Za-z0-9][A-Za-z0-9_]*(\\.[A-Za-z0-9_]*)+$");
        Matcher matcher = methodCompleteNamePattern.matcher(source);
        return matcher.matches();
    }

    private Class<?> getClazzBySource(String[] cells) {
        StringBuilder clazzPath = new StringBuilder(cells[0]);
        if (cells.length > 1) {
            for (int i = 1; i < cells.length - 1; i++) {
                clazzPath.append(".").append(cells[i]);
            }
        }
        Class<?> methodClazz = null;
        try {
            methodClazz = Class.forName(clazzPath.toString());
        } catch (ClassNotFoundException e) {
            return null;
        }
        return null;
    }



}