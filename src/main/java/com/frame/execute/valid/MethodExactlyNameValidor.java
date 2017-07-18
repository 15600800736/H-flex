package com.frame.execute.valid;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fdh on 2017/7/4.
 */
public class MethodExactlyNameValidor implements Validor {

    private String source;

    public MethodExactlyNameValidor(String source) {
        this.source = source;
    }
    @Override
    public Boolean valid() {
        source = source.trim();
        Pattern methodCompleteNamePattern = Pattern.compile("^[A-Za-z0-9][A-Za-z0-9_]*(\\.[A-Za-z0-9_]*)+$");
        Matcher matcher = methodCompleteNamePattern.matcher(source);
        return matcher.matches();
    }

    @Override
    public void prepareForExecute() {

    }

    @Override
    public void postProcessForExccute() {

    }
}
