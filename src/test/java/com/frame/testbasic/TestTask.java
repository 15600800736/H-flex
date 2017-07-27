package com.frame.testbasic;

import com.frame.annotations.ActionClass;
import com.frame.context.Task;
import com.frame.context.resource.Resource;
import com.frame.execute.EndExecutor;
import com.frame.execute.Executor;
import org.junit.Test;

/**
 * Created by fdh on 2017/7/27.
 */

@ActionClass
public class TestTask {
    @Test
    public void test() {
        Executor<Object> a = new Executor<Object>() {
            @Override
            public Object exec() throws Exception {
                int result = 0;
                for (int i = 0; i < 10000; i++) {
                    result += i;
                }
                System.out.println(result);
                return result;
            }


            @Override
            public Resource[] getResources() {
                return new Resource[0];
            }
        };

        Executor<Object> b = new Executor<Object>() {
            @Override
            public Object exec() throws Exception {
                for (int i = 0; i < 100; i++) {
                    System.out.println("lalalalala");
                }
                return null;
            }


            @Override
            public Resource[] getResources() {
                return new Resource[0];
            }
        };
        Task task = new Task();
        Thread t = new Thread(() -> {
            task.appendExecutor(a);
            task.appendExecutor(b);
            task.execute();
        });
        t.start();
        System.out.println(task.isDone());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(task.isDone());
        task.appendExecutor(a);
        task.appendExecutor(new EndExecutor());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(task.isDone());
    }
}
