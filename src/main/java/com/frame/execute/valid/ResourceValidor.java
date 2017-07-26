package com.frame.execute.valid;

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
    public Boolean execute() {
        Pattern pattern = Pattern.compile("^([A-Z]:\\\\)?[^\\\\/:*?\"<>|]+?(\\\\[^\\\\/:*?\"<>|]+?)*\\.xml");
        Matcher matcher = pattern.matcher(validable);
        return matcher.matches();

    }

    @Override
    public void prepareForExecute() {

    }

    @Override
    public void postProcessForExccute() {

    }
}
