package com.frame.testbasic;

import com.frame.execute.Executor;
import com.frame.execute.structure.AppendableTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by fdh on 2017/7/30.
 */
public class TestTask {

    static class IntegerWrapper {
        Integer integer = 0;
    }
    static class Test {
        IntegerWrapper integerWrapper = new IntegerWrapper();
    }
    public static void main(String... strings) throws InterruptedException, ExecutionException {
        Test test = new Test();
        AppendableTask<IntegerWrapper> appendableTask = new AppendableTask<>();
        appendableTask.setProduction(test.integerWrapper);
        Thread t = new Thread(() -> {
            for(int i = 5; i < 10; i++) {
                int temp = i;
                Executor<IntegerWrapper,Integer> executor = new Executor<IntegerWrapper,Integer>() {
                    @Override
                    protected Object exec() throws Exception {
                        int result = 1;
                        for(int j = 1; j < temp; j++) {
                            result *= j;
                        }
                        production.integer += result;
                        return result;
                    }
                };
                appendableTask.appendExecutor(executor);
            }
            try {
                test.integerWrapper = appendableTask.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.start();
        System.out.println(appendableTask.isDone());
        System.out.println(appendableTask.isClosed());
//        Assert.assertFalse(appendableTask.isDone());
//        Assert.assertFalse(appendableTask.isClosed());
        Thread.sleep(10000L);
        System.out.println(appendableTask.isDone());
        System.out.println(appendableTask.isClosed());

        List<?> results = appendableTask.get();
        results.forEach(System.out::println);


//        Assert.assertTrue(appendableTask.isDone());
//        Assert.assertFalse(appendableTask.isClosed());
//
        Executor<IntegerWrapper,String> newExe = new Executor<IntegerWrapper,String>() {
            @Override
            protected Object exec() throws Exception {
                Thread.sleep(10000);
                return "Hello, world!";
            }
        };
        appendableTask.appendExecutor(newExe);
        System.out.println(appendableTask.isDone());
        System.out.println(appendableTask.isClosed());
//        Assert.assertFalse(appendableTask.isDone());
//        Assert.assertFalse(appendableTask.isClosed());
        appendableTask.close();
        System.out.println(appendableTask.isDone());
        System.out.println(appendableTask.isClosed());
//        Assert.assertFalse(appendableTask.isDone());
//        Assert.assertTrue(appendableTask.isClosed());
        Thread.sleep(20000);
        System.out.println(appendableTask.isDone());
        System.out.println(appendableTask.isClosed());
        List<?> result = appendableTask.get();
        System.out.println(result.size());
        result.forEach(System.out::println);
//        Assert.assertTrue(appendableTask.isDone());
//        Assert.assertTrue(appendableTask.isClosed());
        System.out.println(test.integerWrapper.integer);
    }
}
