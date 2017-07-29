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
    protected ActionGroupScanner(Configuration configuration) {
        super(configuration);
    }

    public ActionGroupScanner(Configuration configuration, CyclicBarrier barrier) {
        super(configuration, barrier);
    }

    @Override
    public Object exec() throws Exception {
        return true;
    }
}
