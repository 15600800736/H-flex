package com.frame.execute.valid;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fdh on 2017/7/4.
 */
public class ResourceValidor implements Validor {

    String resourcePath;

    public ResourceValidor(String resourcePath) {
        this.resourcePath = resourcePath;
    }
    @Override
    public Boolean valid() {
        Pattern pattern = Pattern.compile("^([A-Z]:\\\\)?[^\\\\/:*?\"<>|]+?(\\\\[^\\\\/:*?\"<>|]+?)*\\.xml");
        Matcher matcher = pattern.matcher(resourcePath);
        return matcher.matches();

    }

    @Override
    public void prepareForExecute() {

    }

    @Override
    public void postProcessForExccute() {

    }
}
