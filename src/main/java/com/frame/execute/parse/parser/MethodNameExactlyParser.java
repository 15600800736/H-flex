
package com.frame.execute.parse.parser;

import com.frame.exceptions.ParseException;
import com.frame.info.ActionInfoHolder;
import com.frame.execute.valid.MethodExactlyNameValidor;
import com.frame.execute.valid.Validor;
import com.frame.info.Configuration;

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
    public Object parse(ActionInfoHolder actionInfo) throws ParseException {
        Validor validor = new MethodExactlyNameValidor(source);
        if (!validor.valid()) {
            throw new ParseException("*.*.*.*", "方法名格式错误");
        }
        Parser parser = new OverloadMethodParser(source,paramsName,args);
        return parser.parse(actionInfo);

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