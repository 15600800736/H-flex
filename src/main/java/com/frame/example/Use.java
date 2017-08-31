package com.frame.example;

import com.frame.annotations.Execution;
import com.frame.annotations.ExecutionSet;
import com.frame.annotations.Executions;

import java.lang.reflect.Field;

/**
 * Created by fdh on 2017/8/28.
 */

@Executions
public class Use {
    @Execution(actionAlias = "getBookId")
    private String bookId;

    @Execution(actionAlias = "getBookISBNById")
    @Execution(actionAlias = "getBookISBNByName")
    private String ISBN;

    public String getBookId(int i) {
        return bookId;
    }

    public String getISBN(String name) {
        return ISBN;
    }

    public String getISBN(int id) {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public static void main(String...strings) {
        try {
            Field field = Use.class.getDeclaredField("ISBN");
            System.out.println(field.isAnnotationPresent(Execution.class));
            Execution[] executions = field.getAnnotationsByType(Execution.class);
            System.out.println(executions.length);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
