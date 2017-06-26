
package com.frame.parse.parser;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodNameFuzzyParser implements Parser {

    @Override
    public Object parse(Object... objects) {
        String source = (String) objects[0];
        String paramsName = (String) objects[1];
        Object[] args = (Object[]) objects[2];
        if(!isSourceValid(source)) {

        }
        return null;
    }

    private Boolean isSourceValid(String source) {
        Pattern methodFuzzyNamePattern = Pattern.compile("^[A-Za-z0-9*?][A-Za-z0-9_*?]*(\\.[A-Za-z0-9_*?]*)+$");
        Matcher matcher = methodFuzzyNamePattern.matcher(source);
        return matcher.matches();
    }

    public static void main(String...strings) {
        String source = "co?.mei*.tes?";
        new MethodNameFuzzyParser().parse(source,null,null);
    }


}