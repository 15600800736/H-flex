package com.frame.testbasic;

import com.frame.execute.Executor;
import com.frame.execute.structure.AppendableTask;

import java.util.List;
import java.util.Map;
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
        int integer = 0;
    }
    public static void main(String... strings) throws InterruptedException, ExecutionException {
        while(true) {
            Test test = new Test();
            AppendableTask<IntegerWrapper> appendableTask = new AppendableTask<>();
            appendableTask.setProduction(test.integerWrapper);
            Thread t = new Thread(() -> {
                for (int i = 5; i < 10; i++) {
                    int temp = i;
                    Executor<IntegerWrapper, Integer> executor = new Executor<IntegerWrapper, Integer>() {
                        @Override
                        protected Object exec() throws Exception {
                            int result = 1;
                            for (int j = 1; j < temp; j++) {
                                result *= j;
                            }
                            production.integer += result;
                            return result;
                        }
                    };
                    appendableTask.appendWorker(executor);
                }
                try {
                    test.integerWrapper = appendableTask.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            t.start();
            Thread.sleep(1000L);
            Map<Executor<IntegerWrapper,?>, Object> results = appendableTask.getResults();
            Executor<IntegerWrapper, String> newExe = new Executor<IntegerWrapper, String>() {
                @Override
                protected Object exec() throws Exception {
                    return "Hello, world!";
                }
            };

            Executor<IntegerWrapper, String> newExe2 = new Executor<IntegerWrapper, String>() {
                @Override
                protected Object exec() throws Exception {
                    return "I love you";
                }
            };
            appendableTask.appendWorker(newExe);
            appendableTask.appendWorker(newExe2);
            appendableTask.close();
            Thread.sleep(1000);
            Map<Executor<IntegerWrapper, ?>, Object> result = appendableTask.getResults();
            result.entrySet().forEach(System.out::println);
        }
    }
}
