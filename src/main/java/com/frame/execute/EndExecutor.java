package com.frame.execute;

import com.frame.context.resource.Resource;

/**
 * Created by fdh on 2017/7/27.
 */
public class EndExecutor implements Executor<Object> {
    @Override
    public Object exec() throws Exception {
        return null;
    }


    @Override
    public Resource[] getResources() {
        return new Resource[0];
    }
}
