package com.frame.execute.scan;

import com.frame.context.info.StringInformation.Configuration;
import com.frame.context.info.StringInformation.ConfigurationNode;
import com.frame.enums.ConfigurationStringPool;
import com.frame.exceptions.ScanException;
import com.frame.execute.scan.action.ActionClassesScanner;
import com.frame.execute.scan.execution.ExecutionScanner;
import com.frame.basic.flow.flows.ReusableTask;


/**
 * Created by fdh on 2017/7/14.
 */

/**
 * The RegisterScanner is used for registering all of the production like actions or executions,
 * It will be called to collect the information from xml or annotations if it is enabled when the frame starts
 * You can configure both <action-classes></action-classes> to specify the path of a method
 * and <base-contents></base-contents> to let the frame scan the class with annotation @ActionClasses
 * under the path. You also can configure <action-groups></action-groups> to register your action groups to call
 * in your program.
 */
public class RegisterScanner extends Scanner {
    private ConfigurationNode actionRegister;
    private ConfigurationNode executions;

    /**
     * Constructor
     * @param production
     * @param actionRegister
     * @param executions
     */
    public RegisterScanner(Configuration production, ConfigurationNode actionRegister, ConfigurationNode executions) {
        super(production);
        this.actionRegister = actionRegister;
        this.executions = executions;
    }


    /**
     * <p>The exec() get the nodes : <action-classes></action-classes> , <annotation-scan/> and <base-content></base-content>,
     * to register actions and executions</p>
     * @return
     * @throws Exception
     */
    @Override
    protected Object exec() throws Exception {
        if (actionRegister == null) {
            throw new ScanException("<action-register></action-register>", "未定义<action-register>");
        }
        // register actions and executions
        // get nodes
        ConfigurationNode actionClasses = actionRegister.getChild(ConfigurationStringPool.ACTION_CLASSES);
        ConfigurationNode annotationScan = actionRegister.getChild(ConfigurationStringPool.ANNOTATION_SCAN);
        ConfigurationNode baseContents = this.production.getRoot().getChild(ConfigurationStringPool.BASE_CONTENTS);
        Boolean canRegisterAction = (actionClasses != null) || (annotationScan != null && baseContents != null);

        if (!canRegisterAction) {
            throw new ScanException("<action-classes></action-classes> 或 <annotation-scan/><base-contents></base-contents>"
                    , "没有可供注册的方法");
        }

        // use task to register
        ReusableTask<Configuration> actionRegisterTask = new ReusableTask<>(2);
        // register action scan by xml
        if (actionClasses != null) {
            actionRegisterTask.appendWorker(new ActionClassesScanner(production));
        }
        // register action scan by annotations
        if (baseContents != null) {
            actionRegisterTask.appendWorker(new BaseContentsScanner(production));
        }
        // register execution scan by xml
        if (executions != null) {
            actionRegisterTask.appendWorker(new ExecutionScanner(production));
        }
        actionRegisterTask.setProduction(production);
        actionRegisterTask.execute();
        production.setIsRegisterd(true);
        return production;
    }

}