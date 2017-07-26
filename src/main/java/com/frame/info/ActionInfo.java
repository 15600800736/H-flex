package com.frame.info;


/**
 * Created by fdh on 2017/7/17.
 */

import com.frame.context.resource.MapperResource;
import com.frame.context.resource.Resource;
import com.frame.enums.ConfigurationStringPool;

import java.util.List;

/**
 * ActionInfo is a delegate of xml tag <action></action>
 * include all attributes and child nodes.
 */
public class ActionInfo extends MapperResource{
    // <alias></alias>
    private List<String> alias;
    // <name></name>
    private String name;
    // <id></id>
    private String id;

    public List<String> getAlias() {
        return alias;
    }

    public void setAlias(List<String> alias) {
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    protected void initResourceOrder() {

    }

    @Override
    protected void initInformationRequired() {
        informationRequired.put(ConfigurationStringPool.ALIAS_ATTRIBUTE, String.class);
        informationRequired.put(ConfigurationStringPool.NAME_ATTRIBUTE, String.class);
        informationRequired.put(ConfigurationStringPool.ID_ATTRIBUTE, String.class);
    }
}
