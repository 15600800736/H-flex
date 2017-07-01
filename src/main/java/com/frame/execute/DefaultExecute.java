
package com.frame.execute;

import com.frame.exceptions.ParseException;
import com.frame.parse.parser.DefaultMethodNameParser;
import com.frame.parse.parser.Parser;

import java.lang.reflect.Method;

public class DefaultExecute implements Execute {

    @Override
    public Object execute(String methodName, String paramsName, Object... args) {
        Parser methodParser = new DefaultMethodNameParser();
        try {
            Method method= (Method) methodParser.parse(methodName,paramsName,args);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}