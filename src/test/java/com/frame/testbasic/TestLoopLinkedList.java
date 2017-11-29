package com.frame.testbasic;

import com.frame.util.structure.LoopLinkedList;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by fdh on 2017/11/27.
 */
public class TestLoopLinkedList {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Test
    public void testInitialization() {
        List<Integer> list = new LoopLinkedList<>();
        Assert.assertTrue(list.size() == 1);
        logger.info(list.toString());
    }

    @Test
    public void testAddAndGet() {
        List<Integer> list = new LoopLinkedList<>();
        list.add(2);
        Assert.assertTrue(list.size() == 1);
        Assert.assertTrue(list.get(0) == 1);
    }

    @Test
    public void testMultiAddAndRandomGet() {
        List<Integer> list = new LoopLinkedList<>();
        for (int i = 0; i < 1000; i++) {
            list.add(i);
        }

        Assert.assertTrue(list.size() == 1000);

        for (int i = 0; i < 100; i++) {
            int j = new Random().nextInt(1000);
            Assert.assertTrue(j == list.get(j));
        }
//        System.out.println(list.get(6));
    }

    @Test
    public void testMultiAddAndIteratorVisitAndLimit() {
        List<Integer> list = new LoopLinkedList<>();
        for (int i = 0; i < 1000; i++) {
            list.add(i);
        }

        LoopLinkedList<Integer>.LinkedLoopItr itr = (LoopLinkedList<Integer>.LinkedLoopItr) list.iterator();

        int i = 0;
        while (itr.hasNext() && itr.limitNextLoop(1)) {
            Integer element = itr.next();
            logger.info((i % list.size()) + " = " + element);
            Assert.assertTrue(element == (i % list.size()));
            i++;
        }

        while (itr.hasNext() && itr.limitNextLoop(2)) {
            Integer element = itr.next();
            logger.info((i % list.size()) + " = " + element);
            Assert.assertTrue(element == (i % list.size()));
            i++;
        }

        i = 999;
        itr = (LoopLinkedList<Integer>.LinkedLoopItr) list.iterator();
        while (itr.hasPrevious() && itr.limitPreviousLoop(1)) {
            Integer element = itr.previous();
            logger.info((i % list.size()) + " = " + element);
            Assert.assertTrue(element == (i % list.size()));
            i--;
        }
    }


    @Test
    public void testRemove() {
        List<Integer> list = new LoopLinkedList<>();
        list.add(1);
    }

}
