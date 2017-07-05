package com.frame.validor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fdh on 2017/7/4.
 */
public class MethodFuzzyNameValidor implements Validor {

    private String source;

    public MethodFuzzyNameValidor(String source) {
        this.source = source;
    }
    @Override
    public Boolean valid() {
        Pattern methodFuzzyNamePattern = Pattern.compile("^[A-Za-z0-9*?][A-Za-z0-9_*?]*(\\.[A-Za-z0-9_*?]*)+$");
        Matcher matcher = methodFuzzyNamePattern.matcher(source);
        return matcher.matches();
    }
}
