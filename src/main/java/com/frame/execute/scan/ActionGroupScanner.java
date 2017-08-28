package com.frame.execute.scan;

import com.frame.context.info.StringInfomation.Configuration;
import com.frame.context.info.StringInfomation.ConfigurationNode;
import com.frame.enums.ConfigurationStringPool;

import java.util.List;

/**
 * Created by fdh on 2017/7/20.
 */
public class ActionGroupScanner extends Scanner {

    private ConfigurationNode actionGroups;

    public ActionGroupScanner(Configuration production) {
        super(production);
    }


    @Override
    public Object exec() throws Exception {
        List<ConfigurationNode> actionClasses = actionGroups == null ? null : actionGroups.getChildren(ConfigurationStringPool.ACTION_CLASSES);
        if (actionClasses == null) {
            return false;
        }
        actionClasses.forEach(ac -> {
            try {
                new GroupClassesScanner(this.production, ac).execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return null;
    }
}
