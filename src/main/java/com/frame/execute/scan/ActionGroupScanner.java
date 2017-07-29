package com.frame.execute.scan;

import com.frame.annotations.ActionClass;
import com.frame.context.resource.Resource;
import com.frame.context.info.StringInfomation.Configuration;

import java.util.concurrent.CyclicBarrier;

/**
 * Created by fdh on 2017/7/20.
 */
@ActionClass(className = "a")
public class ActionGroupScanner extends Scanner {

    public ActionGroupScanner(Configuration production) {
        super(production);
    }

    public ActionGroupScanner(CyclicBarrier barrier, Configuration production) {
        super(barrier, production);
    }

    @Override
    public Object exec() throws Exception {
        return true;
    }
}
