package com.frame.execute.control;


/**
 * Created by fdh on 2017/7/24.
 */

import com.frame.exceptions.ScanException;
import com.frame.execute.scan.BaseContentsScanner;
import com.frame.execute.scan.Scanner;
import com.frame.info.Configuration;
import com.frame.context.read.ConfigurationReader;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The main controller takes charge of all processes and states
 */
public class MainController implements Controller {
    /**
     * <p>singleton barrier, used for controlling progress.</p>
     */
    private volatile CyclicBarrier progressBarrier;

    /**
     * <p>Reader is used for reading resources and transform it to a tree with returning its root</p>
     */
    private ConfigurationReader reader;

    /**
     * <p>Configuration is the main configure file of the frame</p>
     */
    private Configuration configuration;
    /**
     * <p>stage field indicates what the period in on.</p>
     * <ol>
     *     <li>0 - 0 represents nothing happens, usually at the beginning as default value</li>
     *     <li>1 - 1 represents the frame is ready for starting initialization</li>
     *     <li>2 - 2 represents the scanning is on</li>
     *     <li>3 - 3 represents the scanning has finished</li>
     * </ol>
     */
    private volatile AtomicInteger stage = new AtomicInteger(0);
    @Override
    public void prepareForExecute() {
        reader = createConfigurationReader("G:\\test.xml");
        configuration = reader.createConfiguration();
        initProgressCyclicBarrier(3);
        step();
    }

    @Override
    public void postProcessForExccute() {

    }

    /**
     * <p>The controller that controls the whole process of initialization.
     * It will </p>
     *
     */
    @Override
    public void control() {
        // if the frame is ready for initialize
        if(1 == getStage()) {
            Scanner scanner = null;
            if (reader != null) {
                scanner = new BaseContentsScanner(progressBarrier);
            }
            if (scanner != null) {
                try {
                    scanner.scan(configuration);
                } catch (ScanException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * create the configuration reader with specify path
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
     * <p>singleton factory for producing the barrier</p>
     * @param number
     */
    private void initProgressCyclicBarrier(Integer number) {
        synchronized (this) {
            if(progressBarrier == null) {
                progressBarrier = new CyclicBarrier(number, this::step);
            }
        }
    }

    /**
     * <p>When this method is invoked, the frame's initialization will become the next stage</p>
     */
    private void step() {
        stage.incrementAndGet();
    }

    /**
     * @return the stage of the frame
     */
    private Integer getStage() {
        return stage.get();
    }
}
