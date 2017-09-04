package com.frame.exceptions;

/**
 * Created by fdh on 2017/9/4.
 */
public class ParseException extends RuntimeException {

    /**
     * <p>The target to parse</p>
     */
    private String target;

    /**
     * <p>The reason of exceptions</p>
     */
    private String cause;

    public ParseException(String target, String cause) {
        this.target = target;
        this.cause = cause;
    }

    @Override
    public String getMessage() {
        return "exception is thrown when parsing " + target + " because" + cause;
    }
}
