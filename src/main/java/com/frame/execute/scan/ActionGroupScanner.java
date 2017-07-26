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
    public void prepareForExecute() {

    }

    @Override
    public void postProcessForExceute() {

    }

    @Override
    public Resource[] getResources() {
        return new Resource[0];
    }

    @Override
    public Boolean execute() throws Exception {
        return true;
    }
}
