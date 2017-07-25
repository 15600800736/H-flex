package com.frame.execute.valid;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fdh on 2017/7/5.
 */
public class PackageValidor implements Validor {
    private List<String> pakageList;

    public PackageValidor(List<String> pakageList) {
        this.pakageList = pakageList;
    }

    @Override
    public Boolean valid() {
        Pattern pattern = Pattern.compile("^[a-zA-z_][a-zA-Z0-9_]*(\\.[a-zA-z_][a-zA-Z0-9_]*)*");
        for(String pack : pakageList) {
            Matcher matcher = pattern.matcher(pack);
            if(!matcher.matches()) {
                return false;
            }
        }
        return true;
    }


    @Override
    public void prepareForExecute() {

    }

    @Override
    public void postProcessForExccute() {

    }
}
