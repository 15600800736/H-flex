package com.frame.info;


/**
 * Created by fdh on 2017/7/17.
 */

import java.util.List;

/**
 * ActionInfo is a delegate of xml tag <action></action>
 * include all attributes and child nodes.
 */
public class ActionInfo{
    // <alias></alias>
    private List<String> aliases;
    // <name></name>
    private String name;
    // <id></id>
    private String id;

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
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
}
