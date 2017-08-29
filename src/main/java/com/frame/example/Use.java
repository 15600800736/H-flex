package com.frame.example;

import com.frame.annotations.Action;
import com.frame.annotations.ActionGroup;
import com.frame.annotations.Execution;
import com.frame.context.ExecutionContext;

/**
 * Created by fdh on 2017/8/28.
 */

@ActionGroup
public class Use {
    @Execution(actionAlias = "getBookId")
    private String bookId;

    private String getBookId(int i) {
        return bookId;
    }

    public static void main(String...strings) {
        ((Use)new ExecutionContext().get("Use")).getBookId(10);
    }
}
