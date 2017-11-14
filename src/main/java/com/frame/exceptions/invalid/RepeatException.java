package com.frame.exceptions.invalid;

/**
 * Created by fdh on 2017/11/14.
 */
public class RepeatException extends Throwable{

    String repeatArgName;

    String repeatValue;

    public RepeatException(String repeatArgName, String repeatValue) {
        this.repeatArgName = repeatArgName;
        this.repeatValue = repeatValue;
    }

    public String getMessage() {
        return "The value of " + repeatArgName + ": " + repeatValue + " repeated but not permitted, maybe you should change your input value";
    }
}
