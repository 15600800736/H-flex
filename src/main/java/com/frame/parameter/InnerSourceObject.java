package com.frame.parameter;

import com.frame.annotations.Param;

/**
 * Created by fdh on 2017/5/30.
 */
public class InnerSourceObject {
    @Param(paramName = "e", paramType = String.class)
    private String e;
    @Param(paramName = "f", paramType = String.class)
    private String f;

    public String getF() {
        return f;
    }

    public void setF(String f) {
        this.f = f;
    }

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }
}
