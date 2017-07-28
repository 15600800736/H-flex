package com.frame.execute;

import com.frame.context.resource.Resource;

/**
 * Created by fdh on 2017/7/27.
 */
public class EndExecutor implements Executor<Object> {
    private Object result;

    public void setResult(Object result) {
        this.result = result;
    }

    public Object getResult() {
        return result;
    }

    @Override
    public Object exec() throws Exception {
        return result;
    }
}
