package com.frame.example;

import com.frame.annotations.Execution;
import com.frame.annotations.ExecutionSet;
import com.frame.annotations.Executions;

import java.lang.reflect.Field;

/**
 * Created by fdh on 2017/8/28.
 */

@Executions(name = "Use")
public class Use {
    @Execution(actionAlias = "getBookId")
    private String bookId;

    @Execution(actionAlias = "getBookISBNById")
    @Execution(actionAlias = "getBookISBNByName")
    private String ISBN;

    public String getBookId(Object...args) {
        return bookId;
    }
    public String getBookId(String name, Object...args) {
        return bookId;
    }
    public String getBookId() {
        return bookId;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public static void main(String... strings) {
    }
}
