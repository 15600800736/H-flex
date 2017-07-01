
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
        Parser parser = new OverloadMethodParser();
        return parser.parse(source,paramsName,args);

    }

    private Boolean isSourceValid(String source) {
        source = source.trim();
        Pattern methodCompleteNamePattern = Pattern.compile("^[A-Za-z0-9][A-Za-z0-9_]*(\\.[A-Za-z0-9_]*)+$");
        Matcher matcher = methodCompleteNamePattern.matcher(source);
        return matcher.matches();
    }
    

}