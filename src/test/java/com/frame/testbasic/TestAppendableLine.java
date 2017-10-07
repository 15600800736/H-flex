package com.frame.testbasic;

import com.frame.execute.Executor;
import com.frame.flow.flows.AppendableLine;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by fdh on 2017/8/7.
 */
public class TestAppendableLine {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void testBasicLogic() throws InterruptedException {
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
        for (int i = 0; i < 2; i++) {
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
                for (int i = 1; i < 10; i++) {
                    line.appendProduction(i);
                }
                line.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.start();
        line.close();

        while (line.hasNext()) {
            System.out.println(line.get());
        }
    }

    @Test
    public void testEmptyLine() {
        AppendableLine<Integer> line = new AppendableLine<>(0, 10);
        Thread t = new Thread(() -> {
            try {
                line.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.start();
        line.close();
        while (line.hasNext()) {
            System.out.println(line.get());
        }
    }

    @Test
    public void testMultiThreadCloseLine() {
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

        Thread t1 = new Thread(() -> {
            try {
                Thread.sleep(1000);
                line.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread t2 = new Thread(()-> {
            try {
                Thread.sleep(1000);
                line.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.start();
        t2.start();
    }

}
