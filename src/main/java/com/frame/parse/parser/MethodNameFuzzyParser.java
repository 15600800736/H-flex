
package com.frame.parse.parser;


import com.frame.exceptions.ParseException;
import com.frame.info.ActionInfoHolder;
import com.frame.validor.MethodFuzzyNameValidor;
import com.frame.validor.Validor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodNameFuzzyParser implements Parser {
    private String source;
    private String paramsName;
    private Object[] args;

    public MethodNameFuzzyParser(String source, String paramsName, Object[] args) {
        this.source = source;
        this.paramsName = paramsName;
        this.args = args;
    }
    @Override
    public Object parse(ActionInfoHolder actionInfo) throws ParseException {
        Validor validor = new MethodFuzzyNameValidor(source);
        if(!validor.valid()) {
            throw new ParseException("c?m.f*me.p?rse.Parse", "方法名无法解析");
        }


        return null;
    }
}