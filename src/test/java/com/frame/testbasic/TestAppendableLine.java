package com.frame.testbasic;

import com.frame.execute.Executor;
import com.frame.execute.structure.AppendableLine;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by fdh on 2017/8/7.
 */
public class TestAppendableLine {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void testBasicLogic() throws InterruptedException {
        for (int j = 0; j < 10; j++) {
            AppendableLine<Integer> line = new AppendableLine<>(100, 100);
            for (int i = 0; i < 10; i++) {
                int finalI = i;
                Executor<Integer, Integer> executor = new Executor<Integer, Integer>() {
                    @Override
                    protected Object exec() throws Exception {
                        this.production += finalI;
                        Thread.sleep(10L);
                        return this.production;
                    }
                };

                line.appendWorker(executor);
            }
            Thread t = new Thread(() -> {
                try {
                    for (int i = 1; i < 100; i++) {
                        line.appendProduction(i);
                    }
                    line.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            t.start();
            Thread.sleep(10);
            Assert.assertTrue(line.isStarted());
            Assert.assertFalse(line.isDone());
            Assert.assertFalse(line.isClosed());
            Thread.sleep(10000);
            Assert.assertTrue(line.isStarted());
            Assert.assertTrue(line.isDone());
            Assert.assertFalse(line.isClosed());
            line.close();
            Thread.sleep(100);
            Assert.assertTrue(line.isStarted());
            Assert.assertTrue(line.isDone());
            Assert.assertTrue(line.isClosed());
        }
    }

    @Test
    public void testAppendProduction() throws InterruptedException {
        AppendableLine<Integer> line = new AppendableLine<>(100, 100);
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            Executor<Integer, Integer> executor = new Executor<Integer, Integer>() {
                @Override
                protected Object exec() throws Exception {
                    this.production += finalI;
                    Thread.sleep(10L);
                    return this.production;
                }
            };

            line.appendWorker(executor);
        }
        Thread t = new Thread(() -> {
            try {
                for (int i = 1; i < 100; i++) {
                    line.appendProduction(i);
                }
                line.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.start();
        Thread.sleep(100);
        Assert.assertTrue(line.isStarted());
        Assert.assertFalse(line.isDone());
        Assert.assertFalse(line.isClosed());
        Thread.sleep(10000);
        Assert.assertTrue(line.isStarted());
        Assert.assertTrue(line.isDone());
        Assert.assertFalse(line.isClosed());
        Thread t2 = new Thread(() -> {
            for (int i = 1; i < 100; i++) {
                line.appendProduction(i);
            }
        });
        t2.start();
        Thread.sleep(10);
        Assert.assertTrue(line.isStarted());
        Assert.assertFalse(line.isDone());
        Assert.assertFalse(line.isClosed());
        Thread.sleep(10000);
        Assert.assertTrue(line.isStarted());
        Assert.assertTrue(line.isDone());
        Assert.assertFalse(line.isClosed());
//            line.close();
//            Thread.sleep(100);
//            Assert.assertTrue(line.isStarted());
//            Assert.assertTrue(line.isDone());
//            Assert.assertTrue(line.isClosed());
        line.close();
    }

    @Test
    public void testGetResults() throws InterruptedException {
        AppendableLine<Integer> line = new AppendableLine<>(100, 100);
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            Executor<Integer, Integer> executor = new Executor<Integer, Integer>() {
                @Override
                protected Object exec() throws Exception {
                    this.production += finalI;
                    Thread.sleep(10L);
                    return this.production;
                }
            };

            line.appendWorker(executor);
        }
        List<Integer> result = new LinkedList<>();
        Thread t = new Thread(() -> {
            try {
                for (int i = 1; i < 100; i++) {
                    line.appendProduction(i);
                }
                result.addAll(line.execute());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.start();

        Thread.sleep(30000);
        System.out.println(line.isDone());
        System.out.println(result.size());
        result.forEach(r->{
            if(logger.isDebugEnabled()) {
                logger.debug(String.valueOf(r));
            } else {
                System.out.println(r);
            }
        });
    }
}
