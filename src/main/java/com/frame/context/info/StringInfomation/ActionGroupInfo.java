package com.frame.context.info.StringInfomation;

import com.frame.context.resource.MapperResource;
import com.frame.context.resource.Resource;

import java.util.List;

/**
 * Created by fdh on 2017/7/22.
 */
public class ActionGroupInfo extends MapperResource {

    /**
     * <p></p>
     */
    public String actionClass;

    /**
     * <p>The alias of the method</p>
     */
    public String alias;


    /**
     * <p>The field combined with the method</p>
     */
    public String fieldName;

    /**
     * <p>The processors</p>
     */
    public List<ProcessorInfo> processors;


    @Override
    protected void initInformationRequired() {

    }
}
