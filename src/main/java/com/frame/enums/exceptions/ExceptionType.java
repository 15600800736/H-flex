package com.frame.enums.exceptions;

/**
 * Created by fdh on 2017/5/30.
 */
public enum ExceptionType {
    NoPublicGetter("没有可用的getter"),
    NoPublicSetter("没有可用的setter");

    public String getName() {
        return name;
    }

    private String name;

    ExceptionType(String name) {
        this.name = name;
    }
}
