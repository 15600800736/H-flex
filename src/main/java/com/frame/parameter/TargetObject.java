package com.frame.parameter;

import com.frame.annotations.Param;

/**
 * Created by fdh on 2017/5/30.
 */
public class TargetObject {
    @Param(paramName = "a",paramType = String.class)
    private String a;
    @Param(paramName = "b",paramType = String.class)
    private String b;
    @Param(paramName = "c",paramType = Boolean.class)
    private Boolean c;
    @Param(paramName = "d",paramType = Integer.class)
    private Integer d;
    public Boolean getC() {
        return c;
    }

    public void setC(Boolean c) {
        this.c = c;
    }

    public String getB() {
        return b;
    }

    public void setB(@Param(paramName = "b", paramType = String.class) String b,
                     @Param(paramName = "a", paramType = Integer.class) Integer a,
                     @Param(paramName = "c", paramType = String.class) Integer c,
                     @Param(paramName = "d", paramType = Integer.class)Integer d) {
        this.b = b;
    }

    public TargetObject(String a) {
        this.a = a;
    }

    public TargetObject() {
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }
}
