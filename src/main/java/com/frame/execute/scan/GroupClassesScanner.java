package com.frame.execute.scan;

import com.frame.context.info.StringInfomation.ActionGroupInfo;
import com.frame.context.info.StringInfomation.Configuration;
import com.frame.context.info.StringInfomation.ConfigurationNode;
import com.frame.context.info.StringInfomation.ProcessorInfo;
import com.frame.enums.ConfigurationStringPool;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by fdh on 2017/8/27.
 */
public class GroupClassesScanner extends Scanner {
    private ConfigurationNode actionClass;
    public GroupClassesScanner(Configuration production, ConfigurationNode actionClasses) {
        super(production);
        this.actionClass = actionClasses;
    }

    @Override
    protected Object exec() throws Exception {
        List<ConfigurationNode> actions = actionClass.getChildren(ConfigurationStringPool.EXECUTION);
        actions.forEach(a -> {
            ActionGroupInfo groupInfo = new ActionGroupInfo();
            String alias = a.getAttributeText(ConfigurationStringPool.ACTION_ALIAS_ATTRIBUTE);
            String fieldName = a.getAttributeText(ConfigurationStringPool.FIELD_NAME_ATTRIBUTE);
            groupInfo.alias = alias;
            groupInfo.fieldName = fieldName;
            List<ProcessorInfo> processorInfos = new LinkedList<>();
            List<ConfigurationNode> processors = a.getChildren(ConfigurationStringPool.PROCESSOR);
            processors.forEach(p -> {
                ProcessorInfo pInfo = new ProcessorInfo();
                String path = p.getAttributeText(ConfigurationStringPool.PATH_ATTRIBUTE);
                String type = p.getAttributeText(ConfigurationStringPool.TYPE);
                pInfo.path = path;
                pInfo.type = type;
                processorInfos.add(pInfo);
            });
            groupInfo.processors = processorInfos;
        });
        return null;
    }
}
