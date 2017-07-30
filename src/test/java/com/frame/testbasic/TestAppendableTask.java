package com.frame.testbasic;

import com.frame.annotations.ActionClass;
import com.frame.execute.structure.AppendableTask;
import com.frame.execute.Executor;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by fdh on 2017/7/27.
 */

@ActionClass
public class TestAppendableTask {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Test
    public void test() throws InterruptedException {
        Boolean taskProduction = false;
        AppendableTask<Boolean> appendableTask = new AppendableTask<>(taskProduction,20);
        List<Executor<Boolean,Integer>> executors = new LinkedList<>();
        Thread t = new Thread(() -> {
            for(int i = 10; i < 20; i++) {
                int temp = i;
                Executor<Boolean,Integer> executor = new Executor<Boolean, Integer>() {
                    @Override
                    protected Object exec() throws Exception {
                        int result = 1;
                        for(int j = 1; j < temp; j++) {
                            result *= j;
                        }
                        return result;
                    }
                };
                Boolean executorProduction = true;
                executor.setProduction(executorProduction);
                executors.add(executor);
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

        Executor<Boolean,Integer> newExe = new Executor<Boolean, Integer>() {
            @Override
            protected Object exec() throws Exception {
                Thread.sleep(10000);
                return null;
            }
        };
        appendableTask.appendExecutor(newExe);
        Assert.assertFalse(appendableTask.isDone());
        Assert.assertFalse(appendableTask.isClosed());
        appendableTask.close();
        Assert.assertFalse(appendableTask.isDone());
        Assert.assertTrue(appendableTask.isClosed());
        Thread.sleep(20000);
        Assert.assertTrue(appendableTask.isDone());
        Assert.assertTrue(appendableTask.isClosed());

        List<?> results = appendableTask.get();
        Assert.assertNotNull(results);
        if(logger.isDebugEnabled()) {
            logger.debug(String.valueOf(results.size()));
        } else {
            System.out.println(results.size());
        }
        if(logger.isDebugEnabled()) {
            results.forEach(r -> logger.debug(String.valueOf(r)));
        } else {
            results.forEach(System.out::println);
        }

        Thread.sleep(1000L);

        executors.forEach(e -> {
            Assert.assertTrue(e.getProduction());
        });
    }
}
