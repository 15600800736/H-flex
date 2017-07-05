
package com.frame.parse.parser;

import com.frame.exceptions.ParseException;
import com.frame.validor.MethodExactlyNameValidor;
import com.frame.validor.Validor;

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
        Validor validor = new MethodExactlyNameValidor(source);
        if (!validor.valid()) {
            throw new ParseException("*.*.*.*", "方法名格式错误");
        }
        Parser parser = new OverloadMethodParser(source,paramsName,args);
        return parser.parse();

    }

}