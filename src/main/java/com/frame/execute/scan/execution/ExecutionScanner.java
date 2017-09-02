package com.frame.execute.scan.execution;

import com.frame.context.info.StringInfomation.Configuration;
import com.frame.context.info.StringInfomation.ConfigurationNode;
import com.frame.context.info.StringInfomation.ExecutionInfo;
import com.frame.context.info.StringInfomation.ProcessorInfo;
import com.frame.enums.ConfigurationStringPool;
import com.frame.execute.scan.Scanner;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by fdh on 2017/7/20.
 */

/**
 *
 */
public class ExecutionScanner extends Scanner {

    private ConfigurationNode executions;

    private final String fieldSeperator = "&";

    public ExecutionScanner(Configuration production) {
        super(production);
        this.executions = this.production.getRoot().getChild(ConfigurationStringPool.EXECUTIONS);
    }

    class ExecutionClassScanner extends Scanner {
        private ConfigurationNode executionClass;
        public ExecutionClassScanner(Configuration production, ConfigurationNode executionClass) {
            super(production);
            this.executionClass = executionClass;
        }

        @Override
        protected Object exec() throws Exception {
            List<ConfigurationNode> fields = executionClass.getChildren(ConfigurationStringPool.FIELD);
            fields.forEach(f -> {
                String prefix = f.getAttributeText(ConfigurationStringPool.PREFIX);
                String suffix = f.getAttributeText(ConfigurationStringPool.SUFFIX);
                ExecutionInfo ei = new ExecutionInfo();
                String name = f.getAttributeText(ConfigurationStringPool.NAME_ATTRIBUTE);
                ei.fieldName = executionClass.getAttributeText(ConfigurationStringPool.PATH_ATTRIBUTE) + fieldSeperator + name;
                List<ConfigurationNode> exs = f.getChildren(ConfigurationStringPool.EXECUTION);
                Set<ExecutionInfo.Execution> executions = new HashSet<>(128);
                exs.forEach(ex -> {
                    ExecutionInfo.Execution e = new ExecutionInfo.Execution();
                    StringBuilder aliasAppender = new StringBuilder();
                    if (prefix != null) {
                        aliasAppender.append(prefix);
                    }
                    aliasAppender.append(ex.getAttributeText(ConfigurationStringPool.ACTION_ALIAS_ATTRIBUTE));
                    if (suffix != null) {
                        aliasAppender.append(suffix);
                    }
                    e.alias = aliasAppender.toString();
                    e.processors = new LinkedList<>();
                    List<ConfigurationNode> processors = ex.getChildren(ConfigurationStringPool.PROCESSOR);
                    processors.forEach(p -> {
                        ProcessorInfo pi = new ProcessorInfo();
                        pi.path = p.getAttributeText(ConfigurationStringPool.PATH_ATTRIBUTE);
                        pi.type = p.getAttributeText(ConfigurationStringPool.TYPE_ATTRIBUTE);
                        e.processors.add(pi);
                    });
                    e.returnType = ex.getAttributeText(ConfigurationStringPool.RETURN_TYPE);
                    executions.add(e);
                });
                ei.executions = executions;
                ei.actionClass = executionClass.getAttributeText(ConfigurationStringPool.NAME_ATTRIBUTE);
                this.production.appendExecution(ei.fieldName, ei);
            });
            return null;
        }
    }


    @Override
    public Object exec() throws Exception {
        ConfigurationNode executionClasses = executions == null ? null : executions.getChild(ConfigurationStringPool.EXECUTION_CLASSES);
        if (executionClasses == null) {
            return false;
        }
        List<ConfigurationNode> executionClass = executionClasses.getChildren(ConfigurationStringPool.EXECUTION_CLASS);
        if(executionClass == null) {
            return false;
        }
        executionClass.forEach(ec -> {
            try {
                String name = ec.getAttributeText(ConfigurationStringPool.NAME_ATTRIBUTE);
                String path = ec.getAttributeText(ConfigurationStringPool.PATH_ATTRIBUTE);
                this.production.appendExecutionClass(name, path);
                new ExecutionClassScanner(this.production, ec).execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return null;
    }
}
