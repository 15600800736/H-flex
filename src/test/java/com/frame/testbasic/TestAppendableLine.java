package com.frame.testbasic;

import com.frame.execute.Executor;
import com.frame.execute.structure.AppendableLine;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by fdh on 2017/8/7.
 */
public class TestAppendableLine {
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
                for(int i = 1; i < 100; i++) {
                    line.appendProduction(i);
                }
                line.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.start();
        Thread.sleep(1);
        Assert.assertTrue(line.isStarted());
        Assert.assertFalse(line.isDone());
        Assert.assertFalse(line.isClosed());
        Thread.sleep(1000);
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
