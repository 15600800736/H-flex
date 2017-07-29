package com.frame.execute.control;


/**
 * Created by fdh on 2017/7/24.
 */

import com.frame.execute.Task;
import com.frame.context.resource.Resource;
import com.frame.enums.ConfigurationStringPool;
import com.frame.exceptions.ScanException;
import com.frame.execute.scan.RegisterScanner;
import com.frame.execute.scan.Scanner;
import com.frame.context.info.StringInfomation.Configuration;
import com.frame.context.read.ConfigurationReader;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>The main controller takes charge of all processes and states.
 * There is a {@link Task} queue in the controller, and the main controller will loop check
 * if the current task is finished and the field {@code state}, if false, the controller will yield to let other threads
 * work, otherwise, it will call {@code step()} to get next task to execute until the state equals finished</p>
 */
public class MainController extends Controller<Object,Boolean> {
    /**
     * <p>Reader is used for reading resources and transform it to a tree with returning its root</p>
     */
    private volatile ConfigurationReader reader;

    /**
     * <p>currentTask represents the currentTask in the taskQueue</p>
     */
    private volatile Task currentTask;

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
     * <p>Task queue contains all of the work should be done, every time the controller
     * will take the head task to execute util the state is finished. If there are no tasks,
     * The thread will wait until another task is put in. So every thing the frame want to do should put an
     * executor in it and let it do it.</p>
     */
    private BlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>();

    /**
     * <p>The singleService is used for start a new thread to execute task, the single guarantee the task will be
     * execute by its order in queue. Starting a new thread is for the main thread do other works instead of waiting the
     * task finished. If you want to know if the task has finished, call {@code Boolean isFinished = currentTask.isDone()}</p>
     */
    private ExecutorService singleService = Executors.newSingleThreadExecutor();


    @Override
    public void prepareForExecute() {

        reader = createConfigurationReader("F:\\SourceTreeGit\\H-flex\\src\\main\\resources\\test.xml");
        configuration = reader.createConfiguration();
        registerExecutor();
        step();
    }

    private void initTaskQueue() {

    }

    @Override
    public Resource[] getResources() {
        return new Resource[0];
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
        // if the frame is ready for initialize
        Scanner scanner = null;
        if (reader != null) {
            scanner = new RegisterScanner(configuration, configuration.getRoot().getChild(ConfigurationStringPool.ACTION_REGISTER),
                    configuration.getRoot().getChild(ConfigurationStringPool.ACTION_GROUPS));
        }
        if (scanner != null) {
            try {
                scanner.execute();
            } catch (ScanException e) {
                e.printStackTrace();
            }
        }
        return true;
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
     * <p>When this method is invoked, the frame's initialization will become the next stage</p>
     */
    private void step() {
    }


    /**
     * <p>Register the handler to initialize the frame,
     * include Scanner, Parser etc.</p>
     */
    private void registerExecutor() {

    }
}
