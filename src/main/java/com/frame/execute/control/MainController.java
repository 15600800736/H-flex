package com.frame.execute.control;


/**
 * Created by fdh on 2017/7/24.
 */

import com.frame.execute.structure.AppendableLine;
import com.frame.execute.structure.AppendableTask;
import com.frame.enums.ConfigurationStringPool;
import com.frame.exceptions.ScanException;
import com.frame.execute.scan.RegisterScanner;
import com.frame.execute.scan.Scanner;
import com.frame.context.info.StringInfomation.Configuration;
import com.frame.context.read.ConfigurationReader;
import com.frame.execute.structure.ReusableTask;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>The main controller takes charge of all processes and states.
 * There is a {@link AppendableTask} queue in the controller, and the main controller will loop check
 * if the current task is finished and the field {@code state}, if false, the controller will yield to let other threads
 * work, otherwise, it will call {@code step()} to get next task to execute until the state equals finished</p>
 */
public class MainController extends Controller<Object,Boolean> {
    /**
     * <p>Reader is used for reading resources and transform it to a tree with returning its root</p>
     */
    private volatile ConfigurationReader reader;

    /**
     * <p>currentAppendableTask represents the currentAppendableTask in the appendableTaskQueue</p>
     */
    private volatile AppendableTask currentAppendableTask;

    /**
     * <p>state represents what progress the controller has made.
     * <ol>
     * <li>0 represents not start</li>
     * <li>1 represents doing work</li>
     * <li>2 represents initialization finished</li>
     * <li>3 represents frame is ready to be used</li>
     * <li>4 represents something wrong has happened</li>
     * </ol>
     * </p>
     */
    private AtomicInteger state = new AtomicInteger(0);
    /**
     * <p>Configuration is the main configure file of the frame</p>
     */
    public Configuration configuration;

    /**
     * <p>AppendableTask queue contains all of the work should be done, every time the controller
     * will take the head task to execute util the state is finished. If there are no tasks,
     * The thread will wait until another task is put in. So every thing the frame want to do should put an
     * executor in it and let it do it.</p>
     */
    private BlockingQueue<AppendableTask> appendableTaskQueue = new LinkedBlockingQueue<>();

    /**
     * <p>The singleService is used for start a new thread to execute task, the single guarantee the task will be
     * execute by its order in queue. Starting a new thread is for the main thread do other works instead of waiting the
     * task finished. If you want to know if the task has finished, call {@code Boolean isFinished = currentAppendableTask.isDone()}</p>
     */
    private ExecutorService singleService = Executors.newSingleThreadExecutor();


    @Override
    public void prepareForExecute() {

        reader = createConfigurationReader("F:\\SourceTreeGit\\H-flex\\src\\main\\resources\\test.xml");
        configuration = reader.createConfiguration();
        registerExecutor();
    }

    private void initTaskQueue() {

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

        Thread lineExecThread = new Thread(()-> {
            try {
                configurationLine.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "lineExecThread");

        lineExecThread.start();
        configurationLine.close();
        for(; ;) {
            if(configurationLine.isDone()) {
                configuration = configurationLine.get();
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

    public static void main(String[] args) {
        for(int i = 0; i < 100; i++) {
            MainController m = new MainController();
            try {
                m.execute();
                Thread.sleep(300);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
