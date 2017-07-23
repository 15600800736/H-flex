package com.frame.exceptions;

import com.frame.enums.exceptions.ExtractorExceptionType;

/**
 * Created by fdh on 2017/7/23.
 */
public class ExtractorException extends RuntimeException {
    // the name of the object that is being extracted
    private String required;
    // the class which is being extracted
    private Class<?> fromType;
    // the reason of exception
    private ExtractorExceptionType exceptionType;

    public ExtractorException(String required, Class<?> fromType, ExtractorExceptionType exceptionType) {
        this.required = required;
        this.fromType = fromType;
        this.exceptionType = exceptionType;
    }

    @Override
    public String getMessage() {
        return "extract " + required + "from " + fromType.getName() + "failed, because " + exceptionType.getReason();
    }
}
