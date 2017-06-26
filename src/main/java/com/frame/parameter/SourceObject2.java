package com.frame.parameter;

import com.frame.annotations.Param;

/**
 * Created by fdh on 2017/6/4.
 */
public class SourceObject2 {
    @Param(paramName = "c",paramType = Boolean.class)
    private Boolean c = false;

    public Boolean getC() {
        return c;
    }

    public void setC(Boolean c) {
        this.c = c;
    }
}
