package com.frame.example;

import com.frame.annotations.Action;
import com.frame.annotations.ActionGroup;
import com.frame.annotations.Execution;

/**
 * Created by fdh on 2017/8/28.
 */

@ActionGroup
public class Use {
    @Execution(actionAlias = "getBookId")
    String bookId;


}
