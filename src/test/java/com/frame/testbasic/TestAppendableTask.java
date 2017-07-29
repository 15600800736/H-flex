package com.frame.testbasic;

import com.frame.annotations.ActionClass;
import com.frame.execute.structure.AppendableTask;
import com.frame.execute.Executor;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by fdh on 2017/7/27.
 */

@ActionClass
public class TestAppendableTask {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Test
    public void test() throws InterruptedException {


        AppendableTask appendableTask = new AppendableTask();
        Thread t = new Thread(() -> {
            for(int i = 10; i < 20; i++) {
                int temp = i;
                Executor<Object,Integer> executor = new Executor<Object, Integer>() {
                    @Override
                    protected Object exec() throws Exception {
                        int result = 1;
                        for(int j = 1; j < temp; j++) {
                            result *= j;
                        }
                        return result;
                    }
                };
                appendableTask.appendExecutor(executor);
            }
            try {
                appendableTask.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.start();
        Assert.assertFalse(appendableTask.isDone());
        Assert.assertFalse(appendableTask.isClosed());
        Thread.sleep(1000L);
        Assert.assertTrue(appendableTask.isDone());
        Assert.assertFalse(appendableTask.isClosed());

//        Executor<Object,Object> newExe = new Executor<Object, Object>() {
//            @Override
//            protected Object exec() throws Exception {
//                Thread.sleep(10000);
//                return null;
//            }
//        };
//        appendableTask.appendExecutor(newExe);
//        Assert.assertFalse(appendableTask.isDone());
//        Assert.assertFalse(appendableTask.isClosed());
//        appendableTask.close();
//        Assert.assertFalse(appendableTask.isDone());
//        Assert.assertTrue(appendableTask.isClosed());
//        Thread.sleep(20000);
//        Assert.assertTrue(appendableTask.isDone());
//        Assert.assertTrue(appendableTask.isClosed());
//
//        List<Object> results = appendableTask.get();
//        Assert.assertNotNull(results);
//        if(logger.isDebugEnabled()) {
//            logger.debug(String.valueOf(results.size()));
//        } else {
//            System.out.println(results.size());
//        }
//        if(logger.isDebugEnabled()) {
//            results.forEach(r -> logger.debug(String.valueOf(r)));
//        } else {
//            results.forEach(r -> System.out.println(r));
//        }
    }
}
