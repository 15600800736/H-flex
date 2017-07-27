package com.frame.execute.scan;

import com.frame.annotations.ActionClass;
import com.frame.context.resource.Resource;
import com.frame.exceptions.ScanException;
import com.frame.info.Configuration;

/**
 * Created by fdh on 2017/7/20.
 */
@ActionClass(className = "a")
public class ActionGroupScanner extends Scanner {
    protected ActionGroupScanner(Configuration configuration) {
        super(configuration);
    }

    @Override
    public Object exec() throws Exception {
        return true;
    }

    @Override
    public Resource[] getResources() {
        return new Resource[0];
    }
}
