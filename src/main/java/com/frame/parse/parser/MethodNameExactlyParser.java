
package com.frame.parse.parser;

import com.frame.exceptions.ParseException;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodNameExactlyParser implements Parser {

    private String source;
    private String paramsName;
    private Object[] args;

    public MethodNameExactlyParser(String source, String paramsName, Object[] args) {
        this.source = source;
        this.paramsName = paramsName;
        this.args = args;
    }
    @Override
    public Object parse() throws ParseException {
        if (!isSourceValid(source)) {
            throw new ParseException("*.*.*.*", "方法名格式错误");
        }
        Parser parser = new OverloadMethodParser(source,paramsName,args);
        return parser.parse();

    }

    private Boolean isSourceValid(String source) {
        source = source.trim();
        Pattern methodCompleteNamePattern = Pattern.compile("^[A-Za-z0-9][A-Za-z0-9_]*(\\.[A-Za-z0-9_]*)+$");
        Matcher matcher = methodCompleteNamePattern.matcher(source);
        return matcher.matches();
    }
    

}