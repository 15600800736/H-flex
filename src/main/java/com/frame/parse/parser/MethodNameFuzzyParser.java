
package com.frame.parse.parser;


import com.frame.exceptions.ParseException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodNameFuzzyParser implements Parser {
    @Override
    public Object parse(Object... objects) throws ParseException {
        String source = (String) objects[0];
        String paramsName = (String) objects[1];
        Object[] args = (Object[]) objects[2];
        if(!isSourceValid(source)) {
            throw new ParseException("c?m.f*me.p?rse.Parse", "方法名无法解析");
        }


        return null;
    }

    private Boolean isSourceValid(String source) {
        Pattern methodFuzzyNamePattern = Pattern.compile("^[A-Za-z0-9*?][A-Za-z0-9_*?]*(\\.[A-Za-z0-9_*?]*)+$");
        Matcher matcher = methodFuzzyNamePattern.matcher(source);
        return matcher.matches();
    }
}