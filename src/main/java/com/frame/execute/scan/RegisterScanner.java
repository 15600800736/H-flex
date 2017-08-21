package com.frame.execute.scan;

import com.frame.annotations.ActionClass;
import com.frame.context.info.StringInfomation.Configuration;
import com.frame.context.info.StringInfomation.ConfigurationNode;
import com.frame.enums.ConfigurationStringPool;
import com.frame.exceptions.ScanException;
import com.frame.execute.Executor;
import com.frame.execute.structure.AppendableTask;
import com.frame.execute.structure.ReusableTask;

import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;


/**
 * Created by fdh on 2017/7/14.
 */

/**
 * The RegisterScanner is used for register all of the production like actions or action groups
 * It will be called to collect the information from xml or annotations if it is enabled when the frame starts
 * You can configure both <action-classes></action-classes> to specify the path of a method
 * and <base-contents></base-contents> to let the frame scan the class with annotation @ActionClasses
 * under the path. You also can configure <action-groups></action-groups> to register your action groups to call
 * in your program.
 */
@ActionClass(className = "e")
public class RegisterScanner extends Scanner {
    private ConfigurationNode actionRegister;
    private ConfigurationNode actionGroups;

    public RegisterScanner(Configuration production, ConfigurationNode actionRegister, ConfigurationNode actionGroups) {
        super(production);
        this.actionRegister = actionRegister;
        this.actionGroups = actionGroups;
    }

    @Override
    protected Object exec() throws Exception {
        if (actionRegister == null) {
            throw new ScanException("<action-register></action-register>", "未定义<action-register>");
        }
        // register actions
        // get all of the child node
        ConfigurationNode actionClasses = actionRegister.getChild(ConfigurationStringPool.ACTION_CLASSES);
        ConfigurationNode annotationScan = actionRegister.getChild(ConfigurationStringPool.ANNOTATION_SCAN);
        ConfigurationNode baseContents = actionRegister.getChild(ConfigurationStringPool.BASE_CONTENTS);
        Boolean canRegisterAction = (actionClasses != null) || (annotationScan != null && baseContents != null);

        if (!canRegisterAction) {
            throw new ScanException("<action-classes></action-classes> 或 <annotation-scan/><base-contents></base-contents>"
                    , "没有可供注册的方法");
        }
        Scanner scanner;
        ReusableTask<Configuration> actionRegisterTask = new ReusableTask<>(2);
        // register scan by xml
        if (actionClasses != null) {
            actionRegisterTask.appendWorker(new ActionClassesScanner(production));
        }
        // register scan by annotations
        if (baseContents != null) {
            actionRegisterTask.appendWorker(new BaseContentsScanner(production));
        }
        actionRegisterTask.setProduction(production);
        actionRegisterTask.execute();
        production.setIsRegisterd(true);
        return production;
    }

}