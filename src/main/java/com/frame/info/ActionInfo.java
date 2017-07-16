package com.frame.info;


import java.util.List;

/**
 * Created by fdh on 2017/7/16.
 */
public class ActionInfo {
    // 方法别名列表，未指定时，为方法路径
    private List<String> alias;
    // 方法路径
    private String methodPath;
    // 方法所属的类
    private String classMethod;

    public List<String> getAlias() {
        return alias;
    }

    public void setAlias(List<String> alias) {
        this.alias = alias;
    }

    public String getMethodPath() {
        return methodPath;
    }

    public void setMethodPath(String methodPath) {
        this.methodPath = methodPath;
    }

    public String getClassMethod() {
        return classMethod;
    }

    public void setClassMethod(String classMethod) {
        this.classMethod = classMethod;
    }
}
