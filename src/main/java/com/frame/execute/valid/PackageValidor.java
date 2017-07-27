package com.frame.execute.valid;

import com.frame.context.resource.Resource;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fdh on 2017/7/5.
 */
public class PackageValidor extends Validor {
    public PackageValidor(String validable) {
        super(validable);
    }

    @Override
    public Boolean exec() {
        Pattern pattern = Pattern.compile("^[a-zA-z_][a-zA-Z0-9_]*(\\.[a-zA-z_][a-zA-Z0-9_]*)*");
        Matcher matcher = pattern.matcher(validable);
        return matcher.matches();
    }



    @Override
    public Resource[] getResources() {
        return new Resource[0];
    }
}
