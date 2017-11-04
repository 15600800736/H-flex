package com.frame.exceptions.invalid;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by fdh on 2017/11/4.
 */
public class InvalidStateException extends RuntimeException {
    private String expectedException;

    private String currentException;

    private Class<?> clazz;

    private List<String> solutions;

    public InvalidStateException(String expectedException, String currentException, Class<?> clazz, List<String> solutions) {
        this.expectedException = expectedException;
        this.currentException = currentException;
        this.clazz = clazz;
        this.solutions = solutions;
    }

    public String getMessage() {
        StringBuilder info = new StringBuilder("You got an invalid state in your " + clazz.getName() + " class, expected " + expectedException +
                ", found " + currentException);
        if (solutions == null ||solutions.size() == 0) {
            info.append(". Maybe you should");
        }
        solutions.forEach(s -> info.append("\n").append(s));
        return info.toString();

    }

}
