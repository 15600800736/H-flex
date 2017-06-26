package com.frame.exceptions;

import com.frame.enums.exceptions.ExceptionType;

/**
 * Created by fdh on 2017/5/30.
 */
public class InjectException extends Throwable{
    private ExceptionType exceptionType;
    private String info;
    private Throwable cause;
    private Class<?> sourceObject;
    private Class<?> targetObject;

    public InjectException(ExceptionType exceptionType, String info, Throwable cause, Class<?> sourceObject, Class<?> targetObject) {
        this.exceptionType = exceptionType;
        this.info = info;
        this.cause = cause;
        this.sourceObject = sourceObject;
        this.targetObject = targetObject;
    }

    public void setExceptionType(ExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }

    public void setSourceObject(Class<?> sourceObject) {
        this.sourceObject = sourceObject;
    }

    public void setTargetObject(Class<?> targetObject) {
        this.targetObject = targetObject;
    }

    public ExceptionType getExceptionType() {

        return exceptionType;
    }

    public String getInfo() {
        return info;
    }

    public Throwable getCause() {
        return cause;
    }

    public Class<?> getSourceObject() {
        return sourceObject;
    }

    public Class<?> getTargetObject() {
        return targetObject;
    }
}
