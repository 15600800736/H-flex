
package com.frame.exceptions;

public class ParseException extends Exception {
    private String example;
    private String message;

    public ParseException(String example, String message) {
        this.example = example;
        this.message = message;
    }

    public String getExample() {
        return example;
    }

    @Override
    public String getMessage() {
        return message;
    }

}