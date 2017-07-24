package com.frame.execute.control;


/**
 * Created by fdh on 2017/7/24.
 */

import com.frame.exceptions.ScanException;
import com.frame.execute.scan.BaseContentsScanner;
import com.frame.execute.scan.Scanner;
import com.frame.info.Configuration;
import com.frame.util.ConfigurationReader;

import java.util.Map;
import java.util.concurrent.CyclicBarrier;

/**
 * The main controller takes charge of all processes and states
 */
public class MainController implements Controller {
    // singleton barrier, used for controlling progress.
    private volatile CyclicBarrier progressBarrier;
    @Override
    public void prepareForExecute() {

    }

    @Override
    public void postProcessForExccute() {

    }

    @Override
    public void control() {
        ConfigurationReader reader = createConfigurationReader("G:\\test.xml");
        Configuration configuration = reader.createConfiguration();
        initProgressCyclicBarrier(3);
        Scanner scanner = null;
        if(reader != null) {
            scanner = new BaseContentsScanner(progressBarrier);
        }
        if(scanner != null) {
            try {
                scanner.scan(configuration);
            } catch (ScanException e) {
                e.printStackTrace();
            }
        }
    }

    private ConfigurationReader createConfigurationReader(String configurationPath) {
        ConfigurationReader reader = null;
        try {
            reader = new ConfigurationReader(configurationPath);
        } catch (ScanException e) {
            e.printStackTrace();
        }
        return reader;
    }

    private void initProgressCyclicBarrier(Integer number) {
        synchronized (this) {
            if(progressBarrier == null) {
                progressBarrier = new CyclicBarrier(number, this::step);
            }
        }
    }

    private void step() {
        
    }
}
