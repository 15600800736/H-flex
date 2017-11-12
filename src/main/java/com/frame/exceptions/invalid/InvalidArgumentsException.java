package com.frame.exceptions.invalid;

/**
 * Created by fdh on 2017/11/4.
 */
public class InvalidArgumentsException extends RuntimeException {
    private String argName;

    private String argValue;

    private String description;

    public InvalidArgumentsException(String argName, String argValue, String description) {
        this.argName = argName;
        this.argValue = argValue;
        this.description = description;
    }


    public String getMessage() {
        return "the argument '" + argName + "' can't be assigned by " + argValue + ", because " + description;
    }

}
