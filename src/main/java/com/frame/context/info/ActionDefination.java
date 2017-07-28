package com.frame.context.info;

import com.frame.context.resource.MapperResource;
import com.frame.execute.process.Processor;

import java.lang.reflect.Method;

/**
 * Created by fdh on 2017/7/28.
 */
public class ActionDefination extends MapperResource {
    // the id of the action
    private String id;

    // the method inside a action
    private Method method;



    @Override
    protected void initInformationRequired() {

    }
}
