package com.frame.parameter;

import com.frame.annotations.Param;

/**
 * Created by fdh on 2017/5/30.
 */
public class SourceObject {
    @Param(paramName = "c", paramType = String.class)
    private String a;
    @Param(paramName = "d", paramType = Integer.class)
    private Integer b;
//    @ParamObject
    private InnerSourceObject innerSourceObject;

    public InnerSourceObject getInnerSourceObject() {
        return innerSourceObject;
    }

    public void setInnerSourceObject(InnerSourceObject innerSourceObject) {
        this.innerSourceObject = innerSourceObject;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public Integer getB() {
        return b;
    }

    public void setB(Integer b) {
        this.b = b;
    }

    public SourceObject() {
    }


}