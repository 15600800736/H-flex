package com.frame.execute.control;


/**
 * Created by fdh on 2017/7/24.
 */

import com.frame.flow.flows.AppendableLine;
import com.frame.flow.flows.AppendableTask;
import com.frame.enums.ConfigurationStringPool;
import com.frame.exceptions.ScanException;
import com.frame.execute.scan.RegisterScanner;
import com.frame.context.info.StringInfomation.Configuration;
import com.frame.context.read.ConfigurationReader;
import com.frame.flow.flows.ReusableTask;

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
        //first step: scan from xml
        AppendableLine<Configuration> configurationLine = new AppendableLine<>(1);
        ReusableTask<Configuration> registerTask = new ReusableTask<>();

        registerTask.appendWorker(new RegisterScanner(configuration,
                reader.getRoot().getChild(ConfigurationStringPool.ACTION_REGISTER),
                reader.getRoot().getChild(ConfigurationStringPool.ACTION_GROUPS)));

        configurationLine.appendProduction(configuration);
        configurationLine.appendWorker(registerTask);

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
                Configuration configuration = configurationLine.get();
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
        ConfigurationReader reader = null;
        try {
            reader = new ConfigurationReader(configurationPath);
        } catch (ScanException e) {
            e.printStackTrace();
        }
        return reader;
    }


    /**
     * <p>Register the handler to initialize the frame,
     * include Scanner, Parser etc.</p>
     */
    private void registerExecutor() {

    }

    public static void main(String[] args) throws Exception {
        for(int i = 0; i < 1000; i++) {
            MainController controller = new MainController();
            controller.execute();
            System.out.println(i);
            Thread.sleep(10);
        }
    }

}
