package com.frame.exceptions.invalid;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by fdh on 2017/11/4.
 */
public class InvalidStateException extends RuntimeException {
    private String expectedState;

    private String currentState;

    private Class<?> clazz;

    private List<String> solutions;

    public InvalidStateException(String expectedState, String currentState, Class<?> clazz, List<String> solutions) {
        this.expectedState = expectedState;
        this.currentState = currentState;
        this.clazz = clazz;
        this.solutions = solutions;
    }

    public String getMessage() {
        StringBuilder info = new StringBuilder("You got an invalid state in your " + clazz.getName() + " class, expected " + expectedState +
                ", found " + currentState);
        if (solutions == null ||solutions.size() == 0) {
            info.append(". Maybe you should");
        }
        solutions.forEach(s -> info.append("\n").append(s));
        return info.toString();

    }

}
