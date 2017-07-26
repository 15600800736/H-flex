package com.frame.execute.valid;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fdh on 2017/7/4.
 */
public class MethodFuzzyNameValidor extends Validor {


    public MethodFuzzyNameValidor(String validable) {
        super(validable);
    }
    @Override
    public Boolean execute() {
        Pattern methodFuzzyNamePattern = Pattern.compile("^[A-Za-z0-9*?][A-Za-z0-9_*?]*(\\.[A-Za-z0-9_*?]*)+$");
        Matcher matcher = methodFuzzyNamePattern.matcher(validable);
        return matcher.matches();
    }

    @Override
    public void prepareForExecute() {

    }

    @Override
    public void postProcessForExccute() {

    }
}
