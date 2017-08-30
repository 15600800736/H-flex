package com.frame.execute.scan.execution;

import com.frame.context.info.StringInfomation.Configuration;
import com.frame.context.info.StringInfomation.ConfigurationNode;
import com.frame.context.info.StringInfomation.ExecutionInfo;
import com.frame.context.info.StringInfomation.ProcessorInfo;
import com.frame.enums.ConfigurationStringPool;
import com.frame.execute.scan.Scanner;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by fdh on 2017/7/20.
 */
public class ExecutionScanner extends Scanner {

    private ConfigurationNode executions;

    public ExecutionScanner(Configuration production) {
        super(production);
    }

    class GroupClassesScanner extends Scanner {
        private ConfigurationNode executionClass;
        public GroupClassesScanner(Configuration production, ConfigurationNode executionClass) {
            super(production);
            this.executionClass = executionClass;
        }

        @Override
        protected Object exec() throws Exception {
            List<ConfigurationNode> fileds = executionClass.getChildren(ConfigurationStringPool.FIELD);
            fileds.forEach(f -> {
                ExecutionInfo ei = new ExecutionInfo();
                String name = f.getAttributeText(ConfigurationStringPool.NAME_ATTRIBUTE);
                ei.fieldName = name;
                List<ConfigurationNode> exs = f.getChildren(ConfigurationStringPool.EXECUTION);
                exs.forEach(ex -> {
                    ExecutionInfo.Execution e = new ExecutionInfo.Execution();
                    String alias = ex.getAttributeText(ConfigurationStringPool.ACTION_ALIAS_ATTRIBUTE);
                    e.alias = alias;
                    e.processors = new LinkedList<>();
                    List<ConfigurationNode> processors = ex.getChildren(ConfigurationStringPool.PROCESSOR);
                    processors.forEach(p -> {
                        ProcessorInfo pi = new ProcessorInfo();
                        pi.path = p.getAttributeText(ConfigurationStringPool.PATH_ATTRIBUTE);
                        pi.type = p.getAttributeText(ConfigurationStringPool.TYPE_ATTRIBUTE);
                        e.processors.add(pi);
                    });
                });

            });
            return null;
        }
    }


    @Override
    public Object exec() throws Exception {
        List<ConfigurationNode> actionClasses = executions == null ? null : executions.getChildren(ConfigurationStringPool.EXECUTION_CLASSES);
        if (actionClasses == null) {
            return false;
        }
        actionClasses.forEach(ac -> {
            try {
                String name = ac.getAttributeText(ConfigurationStringPool.NAME_ATTRIBUTE);
                String path = ac.getAttributeText(ConfigurationStringPool.PATH_ATTRIBUTE);
                this.production.appendExecutionClass(name, path);
                new GroupClassesScanner(this.production, ac).execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return null;
    }
}
