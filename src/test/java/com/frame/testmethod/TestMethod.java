package com.frame.testmethod;

import com.frame.annotations.Action;
import com.frame.annotations.ActionClass;
import com.frame.annotations.ActionGroup;
import com.frame.process.DefaultProcessor;

/**
 * Created by fdh on 2017/7/13.
 */
@ActionGroup
public class TestMethod  {
    @Action(alias = "findBookIdByName")
    private Integer bookId;
    @Action(alias = "insertBook",processors = DefaultProcessor.class)
    private Integer insertNumber;

    public Integer getBookId(Object...args) {
        return bookId;
    }


    public static void main(String...strings) {
        Integer bookId = new TestMethod().getBookId("name");

    }
}

@ActionGroup(prefix = "findBookId")
class Test {
    @Action(alias = "byName")
    @Action(alias = "byISBN")
    private Integer bookId;


    public Integer getBookId(String alias,Object...args) {
        return bookId;
    }
}

@ActionClass
class AA {
    @Action(alias = "findBookIdByName")
    public Integer findBookIdByName(String name) {
        return null;
    }
}

