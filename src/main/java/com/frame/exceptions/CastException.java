package com.frame.exceptions;

/**
 * Created by fdh on 2017/7/21.
 */
public class CastException extends Exception{
    // the offered type
    private Class<?> offeredType;
    // the type that the the object required
    private Class<?> requiredType;

    public CastException(Class<?> offeredType, Class<?> requiredType) {
        this.offeredType = offeredType;
        this.requiredType = requiredType;
    }

    public Class<?> getOfferedType() {
        return offeredType;
    }

    public void setOfferedType(Class<?> offeredType) {
        this.offeredType = offeredType;
    }

    public Class<?> getRequiredType() {
        return requiredType;
    }

    public void setRequiredType(Class<?> requiredType) {
        this.requiredType = requiredType;
    }

    public String getMessage() {
        return "cast error:Require Type " + requiredType + ", but found " + offeredType;
    }
}
