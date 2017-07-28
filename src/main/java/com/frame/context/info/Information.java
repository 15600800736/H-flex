package com.frame.context.info;

/**
 * Created by fdh on 2017/7/22.
 */
public class Information {
    // the unique name of a information
    private String name;
    // the value that the information can supply
    private Object value;
    // the type of the information
    private Class<?> valueClass;

    private Information(String name, Object value) {
        this(name,value,null);
    }

    private Information(String name, Object value, Class<?> valueClass) {
        this.name = name;
        this.value = value;
        this.valueClass = valueClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Class<?> getValueClass() {
        return valueClass;
    }

    public void setValueClass(Class<?> valueClass) {
        this.valueClass = valueClass;
    }

    public static Information createInformation(String name, Object value) {
        return new Information(name,value);
    }

    public static Information createInformation(String name, Object value, Class<?> valueClass) {
        return new Information(name,value,valueClass);
    }
}
