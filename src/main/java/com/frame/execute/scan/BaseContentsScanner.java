package com.frame.execute.scan;

import com.frame.annotations.Action;
import com.frame.annotations.ActionClass;
import com.frame.context.resource.Resource;
import com.frame.enums.ConfigurationStringPool;
import com.frame.enums.TrueOrFalse;
import com.frame.exceptions.ScanException;
import com.frame.context.info.StringInfomation.ActionInfo;
import com.frame.context.info.StringInfomation.Configuration;
import com.frame.context.info.StringInfomation.ConfigurationNode;
import com.frame.context.info.Node;
import com.frame.thread.ScanThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

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
@ActionClass(className = "c")
public class BaseContentsScanner extends Scanner {

    /**
     *
     */
    class ActionRegisterScanner extends Scanner {
        /**
         *
         */
        private Class<?> actionClass;
        protected ActionRegisterScanner(Configuration configuration, Class<?> actionClass) {
            super(configuration);
            this.actionClass = actionClass;
        }

        @Override
        public Object exec() throws Exception {
            if(actionClass == null) {
                throw new ScanException(null,"actionClasses is null");
            }
            // get all methods
            Method[] methods = actionClass.getDeclaredMethods();
            for (Method method : methods) {
                if(method.isAnnotationPresent(Action.class)) {
                    Action actionAnnotation = method.getAnnotation(Action.class);
                    String id = actionAnnotation.id();
                    TrueOrFalse overload = actionAnnotation.overload();

                    ActionInfo newAction = ActionInfo.createActionInfo(id)
                            .setName(method.getName())
                            .setAlias(getAlias(actionAnnotation))
                            .setParam(getParamType(method.getParameterTypes()))
                            .setActionClass(actionClass.getName())
                            .setOverload(overload.getBool());
                    configuration.appendAction(id, newAction);
                }
            }
            return true;
        }
        @Override
        public Resource[] getResources() {
            return new Resource[0];
        }

        private List<String> getAlias(Action actionAnnotation) {
            String alias = actionAnnotation.alias();
            String[] aliases = alias.split(",");
            return Arrays.asList(aliases);
        }
        private List<String> getParamType(Class<?>[] paramType) {
            if(paramType == null) {
                return null;
            }
            List<String> result = new LinkedList<>();
            for (Class<?> pt : paramType) {
                result.add(pt.getName());
            }
            return result;
        }
    }
    private final Integer CLASS_SUFFIX_LENGTH = 6;

    /**
     * <p>The queue stores the action class path like "com.xxx.xxx"</p>
     */
    private final BlockingQueue<String> classesQueue = new LinkedBlockingQueue<>(256);

    /**
     * <p>The flag field represents the states of putting-thread's work.
     * When it finished, it will update the field into true. While the taking-thread
     * will keep checking the field, when it has been changed into true and the block queue is empty,
     * the taking-thread will return to finish the whole scanning progress.
     * </p>
     */
    private volatile Boolean flag = false;

    /**
     * <p>Total num of starting threads in this class</p>
     */
    private final int threadNumber = 2;
    /**
     * <p>represents the scanning progress's endpoint</p>
     */
    private final CountDownLatch scannerLatch = new CountDownLatch(2);

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    public BaseContentsScanner(Configuration configuration) {
        super(configuration);
    }

    public BaseContentsScanner(Configuration configuration, CyclicBarrier barrier) {
        super(configuration, barrier);
    }

    /**
     * <p>The put-thread is used for scanning all of the classpath, and put the string-type class name
     * to the blocking queue, it will wait for other scanner thread </p>
     */
    class PutThread extends ScanThread {
        private List<String> paths;

        public PutThread(List<String> paths, CountDownLatch scannerLatch) {
            super(scannerLatch);
            this.paths = paths;
        }

        @Override
        public void run() {
            String classpath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
            paths.forEach(p -> {
                String pathName = classpath + p.replace(".", "\\");
                getClassesFromClasspath(pathName, p);
            });
            flag = true;
            scannerLatch.countDown();
        }
    }

    /**
     * The taking thread is for checking if a class is a action class and register it into configuration
     */
    class TakeThread extends ScanThread {
        private Configuration configuration;

        public TakeThread(Configuration configuration, CountDownLatch scannerLatch) {
            super(scannerLatch);
            this.configuration = configuration;
        }

        @Override
        public void run() {
            ClassLoader loader = ClassLoader.getSystemClassLoader();
            while (true) {
                if (flag && classesQueue.size() == 0) {
                    flag = false;
                    scannerLatch.countDown();
                    return;
                }
                try {
                        // get the class's full name
                    String classFullName = classesQueue.take();
                    Class<?> actionClass = loader.loadClass(classFullName);
                    if (actionClass.isAnnotationPresent(ActionClass.class)) {
                        ActionClass annotation = actionClass.getAnnotation(ActionClass.class);
                        String className = annotation.className();
                        if(className == null) {
                            className = classFullName;
                        }
                        configuration.appendClass(className, classFullName);
                        Scanner actionRegisterScanner = new ActionRegisterScanner(configuration, actionClass);
                        actionRegisterScanner.execute();
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    if (logger.isErrorEnabled()) {
                        logger.error("scanning has been cancelled.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Object exec() throws Exception {
        Node root = configuration.getRoot();
        if (root == null && configuration.isAnnotationScan() == null) {
            throw new ScanException(null, "无法获取<annotation-scan/>状态");
        }
        if (root == null) {
            // todo
            return false;
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

            // ThreadPool
            ExecutorService ex = Executors.newCachedThreadPool();
            Thread pathScanThread = new PutThread(paths, scannerLatch);
            Thread classRegisterThread = new TakeThread(configuration, scannerLatch);
            ex.execute(pathScanThread);
            ex.execute(classRegisterThread);
            try {
                scannerLatch.await();
            } catch (InterruptedException e) {
                if (logger.isErrorEnabled()) {
                    logger.error("The scanning thread has been interrupt");
                }
            } finally {
                ex.shutdown();
            }
        }
        return true;
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
                        String classFullName = packageName + "." + className.substring(0, className.length() - CLASS_SUFFIX_LENGTH);
                        try {
                            classesQueue.put(classFullName);
                        } catch (InterruptedException e) {
                            if (logger.isErrorEnabled()) {
                                logger.error("The scanning has been cancelled");
                            }
                            return;
                        }
                    }
                }
            }
        }
    }
}