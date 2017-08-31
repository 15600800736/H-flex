package com.frame.testmethod;

import com.frame.annotations.ActionClass;
import com.frame.annotations.Executions;

/**
 * Created by fdh on 2017/7/13.
 */
@Executions
public class TestMethod  {
    private Integer bookId;
    private Integer insertNumber;

    public Integer getBookId(Object...args) {
        return bookId;
    }


    public static void main(String...strings) {
        Integer bookId = new TestMethod().getBookId("name");

    }
}

@Executions
class Test {
    private Integer bookId;

    public Integer getBookId(Object...args) {
        return bookId;
    }
}



@ActionClass
class AA {
    public Integer findBookIdByName(String name) {
        return null;
    }
}

