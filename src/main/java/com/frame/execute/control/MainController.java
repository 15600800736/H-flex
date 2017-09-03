package com.frame.execute.control;


/**
 * Created by fdh on 2017/7/24.
 */

import com.frame.context.ParserContext;
import com.frame.context.info.StringInfomation.ExecutionInfo;
import com.frame.context.resource.Resource;
import com.frame.context.resource.XmlResource;
import com.frame.execute.Executor;
import com.frame.execute.parse.ActionClassParser;
import com.frame.execute.scan.TypeAliasScanner;
import com.frame.execute.scan.action.ActionClassesScanner;
import com.frame.flow.FlowFactory;
import com.frame.flow.SimpleFactory;
import com.frame.flow.flows.AppendableLine;
import com.frame.flow.flows.AppendableTask;
import com.frame.enums.ConfigurationStringPool;
import com.frame.exceptions.ScanException;
import com.frame.execute.scan.RegisterScanner;
import com.frame.context.info.StringInfomation.Configuration;
import com.frame.context.read.ConfigurationReader;

import java.util.List;
import java.util.Set;

/**
 * <p>The main controller takes charge of all processes and states.
 * There is a {@link AppendableTask} queue in the controller, and the main controller will loop check
 * if the current task is finished and the field {@code state}, if false, the controller will yield to let other threads
 * work, otherwise, it will call {@code step()} to get next task to execute until the state equals finished</p>
 */
public class MainController extends Controller<Object, Boolean> {
    /**
     * <p>Reader is used for reading resources and transform it to a tree with returning its root</p>
     */
    private volatile ConfigurationReader reader;

    /**
     * <p>currentAppendableTask represents the currentAppendableTask in the appendableTaskQueue</p>
     */
    private volatile AppendableTask currentAppendableTask;
    /**
     * <p>Configuration is the main configure file of the frame</p>
     */
    public Configuration configuration;


    @Override
    public void prepareForExecute() {
        reader = createConfigurationReader("F:\\SourceTreeGit\\H-flex\\src\\main\\resources\\test.xml");
        configuration = reader.createConfiguration();
        registerExecutor();
    }

    @Override
    public Boolean postProcessForExecute(Object result) {
        return (Boolean) result;
    }

    /**
     * <p>The controller that controls the whole process of initialization.
     * It will </p>
     */
    @Override
    public Boolean exec() throws Exception {
        FlowFactory<Configuration> factory = new SimpleFactory<>();
        Executor[][] executors = new Executor[][]{
                {
                        new TypeAliasScanner(configuration)
                }
                , {
                new RegisterScanner(configuration,
                        reader.getRoot().getChild(ConfigurationStringPool.ACTION_REGISTER),
                        reader.getRoot().getChild(ConfigurationStringPool.EXECUTIONS))
        }
        };
        factory.setExecutors(executors, configuration);
        AppendableLine<Configuration> configurationLine = factory.getLine();

        Thread lineExecThread = new Thread(() -> {
            try {
                configurationLine.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "lineExecThread");

        lineExecThread.start();
        configurationLine.close();
        for (; ; ) {
            if (configurationLine.isDone()) {
                while (configurationLine.hasNext()) {
                    Configuration configuration = configurationLine.get();
//                    configuration.getExecutions().forEach((key, ex) -> {
//                        System.out.println(ex.fieldName);
//                        System.out.println(ex.actionClass);
//                        System.out.println(this.configuration.getExecutionClassesPath().get(ex.actionClass));
//                        ex.executions.forEach(e -> {
//                            System.out.println(e.alias + " " + e.returnType);
//                        });
//                        System.out.println("-----------------------------");
//                    });
//                    configuration.getActions().forEach((key, ac) -> {
//                        System.out.print(ac.getName());
//                        for (String s : ac.getParam()) {
//                            System.out.println(s);
//                        }
//                    });
//                    configuration.getActionAlias().forEach((k, v) -> {
//                        System.out.println(k + " " + v);
//                    });
                    ActionClassParser parser = new ActionClassParser(new ParserContext(),configuration,"a");
                    parser.execute();
                    System.out.println(parser.getProduction().getActionClazz("a"));
                }
                break;
            }
        }

        return null;
    }

    /**
     * create the configuration reader with specify path
     *
     * @param configurationPath
     * @return
     */
    private ConfigurationReader createConfigurationReader(String configurationPath) {
        Resource xmlResource = new XmlResource(configurationPath);
        try {
            return (ConfigurationReader) xmlResource.getReader();
        } catch (ScanException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * <p>Register the handler to initialize the frame,
     * include Scanner, Parser etc.</p>
     */
    private void registerExecutor() {

    }


    public static void main(String[] args) throws Exception {
        MainController mainController = new MainController();
        mainController.execute();
    }
}
