package com.frame.execute.scan;

import com.frame.enums.ConfigurationStringPool;
import com.frame.exceptions.ScanException;
import com.frame.info.Configuration;
import com.frame.info.ConfigurationNode;
import com.frame.info.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by fdh on 2017/7/2.
 */

/**
 * <p>The scanner is used for reading base contents from xml or other outer files,
 * then scanning all actions under the base contents and putting them into Configuration.</p>
 * <p>The scanner will start two thread, one takes charge of looking for .class file under the path,
 * and putting them into a blocking queue, the other thread takes charge of instantiating class and check if
 * it is a action class and register it.</p>
 */
public class BaseContentsScanner
        implements Scanner {
    private final Integer CLASS_SUFFIX_LENGTH = 6;

    /**
     * <p>The queue stores the action class path like "com.xxx.xxx"</p>
     */
    private final BlockingQueue<String> classesQueue = new LinkedBlockingQueue<>(256);

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * <p>The put-thread is used for scanning all of the classpath, and put the string-type class name
     * to the blocking queue</p>
     */
    class PutThread extends Thread {
        private List<String> paths;

        public void setPaths(List<String> paths) {
            this.paths = paths;
        }

        @Override
        public void run() {
            
        }
    }

    class TakeThread extends Thread {

        private Configuration configuration;
        @Override
        public void run() {
//            ClassLoader loader = ClassLoader.getSystemClassLoader();
//            try {
//                 get the class's full name
//
//                Class<?> actionClass = loader.loadClass(classPath);
//                if (actionClass.isAnnotationPresent(ActionClass.class)) {
//                    actionClasses.add(actionClass);
//                }
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();

        }
    }

    @Override
    public void scan(Configuration configuration) throws ScanException {
        Node root = configuration.getRoot();
        if (root == null && configuration.isAnnotationScan() == null) {
            throw new ScanException(null, "无法获取<annotation-scan/>状态");
        }
        if (root == null) {
            // todo
            return;
        }
        // check if annotation-scan is on
        if (configuration.isAnnotationScan() == null) {
            ConfigurationNode annotationScan = root.getChild(ConfigurationStringPool.ANNOTATION_SCAN);
            if (annotationScan == null) {
                configuration.setAnnotationScan(false);
            } else {
                configuration.setAnnotationScan(true);
            }
        }

        if (configuration.isAnnotationScan()) {
            ConfigurationNode baseContents = root.getChild(ConfigurationStringPool.ACTION_REGISTER).getChild(ConfigurationStringPool.BASE_CONTENTS);
            if (baseContents == null) {
                throw new ScanException("<base-contents></base-contents>", "缺少<base-contents>元素");
            }
            // get all path to check
            List<ConfigurationNode> pathNodeList = baseContents.getChildren(ConfigurationStringPool.PATH);
            List<String> paths = new LinkedList<>();
            pathNodeList.forEach(p -> {
                String text = p.getText();
                paths.add(text);
            });

        }
    }

    private void getClassesFromClasspath(String pathName, String packageName) {
        File file = new File(pathName);
        if (file != null) {
            // list all files under the path
            File[] children = file.listFiles();
            if (children == null) {
                return;
            }
            for (File child : children) {
                if (child == null) {
                    continue;
                }
                // recursion and update package name
                if (child.isDirectory()) {
                    String childPackageName = packageName + "." + child.getName();
                    getClassesFromClasspath(child.getPath(), childPackageName);
                } else {
                    // load the class and check if the class is a action class
                    String className = child.getName();
                    if (className.endsWith(".class")) {
                        // add it to classes queue
                        String classPath = packageName + "." + className.substring(0, className.length() - CLASS_SUFFIX_LENGTH);
                        try {
                            classesQueue.put(classPath);
                        } catch (InterruptedException e) {
                            if(logger.isErrorEnabled()) {
                                logger.error("The scanning has been cancelled");
                            }
                            return;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void prepareForExecute() {

    }

    @Override
    public void postProcessForExccute() {

    }


    public static void main(String... strings) {
        BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>(64);
        Object lock = new Object();
        Thread t1 = new Thread(() -> {
            int i = 0;
            while (true) {
                i++;
                try {
                    synchronized (lock) {
                        System.out.println("putting " + i);
                        System.out.println("thread 1");
                        blockingQueue.put(String.valueOf(i));
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }
        });

        Thread t2 = new Thread(() -> {
            t1.interrupt();
        });
        t1.start();
        t2.start();
    }
}

