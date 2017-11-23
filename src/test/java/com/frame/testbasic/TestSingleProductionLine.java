package com.frame.testbasic;

import com.frame.execute.Executor;
import com.frame.flow.flows.SingleProductionLine;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by fdh on 2017/10/6.
 */
public class TestSingleProductionLine {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Test
    public void testSingleProductionLine() {
        Integer i = 1;
        SingleProductionLine<Integer> line = new SingleProductionLine<>(i);
        Executor<Integer, Integer> ex = new Executor<Integer, Integer>() {
            @Override
            protected Object exec() throws Exception {
                return this.production + 1;
            }
        };

        for (int j = 0; j < (10); j++) {
            line.appendWorker(ex);
        }

        try {
            Integer result = line.execute();
            Assert.assertTrue(11 == result);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
