package com.frame.testmethod;

import com.frame.annotations.Action;
import com.frame.annotations.ActionChain;
import com.frame.annotations.ActionChains;

/**
 * Created by fdh on 2017/7/13.
 */
@Action
public class TestMethod  {
    @ActionChain(actionName = "method1")
    @ActionChain(actionName = "method2")
    @ActionChain(actionName = "method3")
    @ActionChain(actionName = "method4")
    public void methodGroup(@Action(alias = "method1")Object[] method1args,
                            @Action(alias = "method2")Object[] method2args,
                            @Action(alias = "method3")Object[] method3args,
                            @Action(alias = "method4")Object[] method4args) {

    }
}
