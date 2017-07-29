package com.frame.testbasic;

import com.frame.annotations.ActionClass;
import com.frame.execute.Task;
import com.frame.execute.Executor;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by fdh on 2017/7/27.
 */

@ActionClass
public class TestTask {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Test
    public void test() throws InterruptedException {


        Task task = new Task();
        Thread t = new Thread(() -> {
            for(int i = 10; i < 20; i++) {
                int temp = i;
                Executor<Integer> executor = new Executor<Integer>() {
                    @Override
                    protected Object exec() throws Exception {
                        int result = 0;
                        for(int j = 1; j < temp; j++) {
                            result *= j;
                        }
                        return result;
                    }
                };
                task.appendExecutor(executor);
            }
            try {
                task.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.start();
        Assert.assertFalse(task.isDone());
        Assert.assertFalse(task.isClosed());
        Thread.sleep(1000L);
        Assert.assertTrue(task.isDone());
        Assert.assertFalse(task.isClosed());

        Executor<Object> newExe = new Executor<Object>() {
            @Override
            protected Object exec() throws Exception {
                Thread.sleep(10000);
                return null;
            }
        };
        task.appendExecutor(newExe);
        Assert.assertFalse(task.isDone());
        Assert.assertFalse(task.isClosed());
        task.close();
        Assert.assertFalse(task.isDone());
        Assert.assertTrue(task.isClosed());
        Thread.sleep(20000);
        Assert.assertTrue(task.isDone());
        Assert.assertTrue(task.isClosed());

        List<Object> results = task.get();
        Assert.assertNotNull(results);
        if(logger.isDebugEnabled()) {
            results.forEach(r -> logger.debug(String.valueOf(r)));
        } else {
            results.forEach(r -> System.out.println(r));
        }
    }
}
