package com.frame.execute.valid;

import com.frame.context.resource.Resource;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fdh on 2017/7/4.
 */
public class MethodExactlyNameValidor extends Validor {

    public MethodExactlyNameValidor(String validable) {
        super(validable);
    }

    @Override
    public Boolean exec() {
        validable = validable.trim();
        Pattern methodCompleteNamePattern = Pattern.compile("^[A-Za-z0-9][A-Za-z0-9_]*(\\.[A-Za-z0-9_]*)+$");
        Matcher matcher = methodCompleteNamePattern.matcher(validable);
        return matcher.matches();
    }

    @Override
    public Resource[] getResources() {
        return new Resource[0];
    }
}
