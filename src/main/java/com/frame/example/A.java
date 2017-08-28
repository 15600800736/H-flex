package com.frame.example;

import com.frame.annotations.Action;
import com.frame.annotations.ActionClass;

/**
 * Created by fdh on 2017/8/25.
 */

@ActionClass(className = "a")
public class A {

    @Action(id = "getBookId")
    public int method1() {
        return 0;
    }

    @Action(id = "8", alias = "lalala, lueluelue")
    public void method2() {

    }
}
