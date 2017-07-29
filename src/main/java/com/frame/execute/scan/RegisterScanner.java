package com.frame.execute.scan;

import com.frame.annotations.ActionClass;
import com.frame.context.resource.Resource;
import com.frame.enums.ConfigurationStringPool;
import com.frame.exceptions.ScanException;
import com.frame.execute.Executor;
import com.frame.execute.Task;
import com.frame.execute.control.Controller;
import com.frame.execute.control.MainController;
import com.frame.context.info.StringInfomation.ActionInfo;
import com.frame.context.info.StringInfomation.Configuration;
import com.frame.context.info.StringInfomation.ConfigurationNode;

import java.util.Map;
import java.util.concurrent.CyclicBarrier;


/**
 * Created by fdh on 2017/7/14.
 */

/**
 * The RegisterScanner is used for register all of the configuration like actions or action groups
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

    public RegisterScanner(ConfigurationNode actionRegister, ConfigurationNode actionGroups, Configuration configuration) {
        super(configuration);
        this.actionRegister = actionRegister;
        this.actionGroups = actionGroups;
    }

    public RegisterScanner(Configuration configuration, CyclicBarrier barrier, ConfigurationNode actionRegister, ConfigurationNode actionGroups) {
        super(configuration, barrier);
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
        Scanner scanner = null;

        // register scan by xml
        if (actionClasses != null) {
            scanner = new ActionClassesScanner(configuration);
            scanner.execute();
        }
        // register scan by annotations
        if (baseContents != null) {
            scanner = new BaseContentsScanner(configuration);
            scanner.execute();
        }

        configuration.setIsRegisterd(true);
        return configuration;
    }


    public static void main(String...strings) throws InterruptedException {
        Task task = new Task();
        Thread t = new Thread(() -> {
            for(int i = 10; i < 20; i++) {
                int temp = i;
                Executor<Integer> executor = new Executor<Integer>() {
                    @Override
                    protected Object exec() throws Exception {
                        int result = 0;
                        for(int j = 1; j < temp; j++) {
                            result *= j;
                        }
                        return result;
                    }
                };
                task.appendExecutor(executor);
            }
            try {
                task.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.start();
        System.out.println(task.isDone());
        System.out.println(task.isClosed());
//        Assert.assertFalse(task.isDone());
//        Assert.assertFalse(task.isClosed());
        Thread.sleep(1000L);
        System.out.println(task.isDone());
        System.out.println(task.isClosed());
//        Assert.assertTrue(task.isDone());
//        Assert.assertFalse(task.isClosed());

        Executor<Object> newExe = new Executor<Object>() {
            @Override
            protected Object exec() throws Exception {
                Thread.sleep(10000);
                return null;
            }
        };
        task.appendExecutor(newExe);
        System.out.println(task.isDone());
        System.out.println(task.isClosed());
//        Assert.assertFalse(task.isDone());
//        Assert.assertFalse(task.isClosed());
        task.close();
        System.out.println(task.isDone());
        System.out.println(task.isClosed());
//        Assert.assertFalse(task.isDone());
//        Assert.assertTrue(task.isClosed());
        Thread.sleep(20000);
        System.out.println(task.isDone());
        System.out.println(task.isClosed());
//        Assert.assertTrue(task.isDone());
//        Assert.assertTrue(task.isClosed());
    }
}
