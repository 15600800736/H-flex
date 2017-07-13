package com.frame.info;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fdh on 2017/7/13.
 */
public class ActionInfoHolder {
    private List<String> alias = new ArrayList<>(16);
    private Class<?> actionClass;
    private Class<?>[] paramTypes;
    private Boolean isStatic;
    private Class<?> returnType;

    public List<String> getAlias() {
        return alias;
    }

    public void setAlias(List<String> alias) {
        this.alias = alias;
    }

    public Class<?> getActionClass() {
        return actionClass;
    }

    public void setActionClass(Class<?> actionClass) {
        this.actionClass = actionClass;
    }

    public Class<?>[] getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(Class<?>[] paramTypes) {
        this.paramTypes = paramTypes;
    }

    public Boolean isStatic() {
        return isStatic;
    }

    public void setStatic(Boolean aStatic) {
        isStatic = aStatic;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }
}
