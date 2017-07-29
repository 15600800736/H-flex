package com.frame.execute.valid;

import com.frame.context.resource.Resource;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fdh on 2017/7/4.
 */
public class ResourceValidor extends Validor {


    public ResourceValidor(String validable) {
        super(validable);
    }


    @Override
    public Boolean exec() {
        Pattern pattern = Pattern.compile("^([A-Z]:\\\\)?[^\\\\/:*?\"<>|]+?(\\\\[^\\\\/:*?\"<>|]+?)*\\.xml");
        Matcher matcher = pattern.matcher(validable);
        return matcher.matches();

    }


    @Override
    public Resource[] getResources() {
        return new Resource[0];
    }
}
