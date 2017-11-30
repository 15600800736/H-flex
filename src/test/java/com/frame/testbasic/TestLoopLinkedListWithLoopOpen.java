package com.frame.testbasic;

import com.frame.util.structure.LoopLinkedList;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by fdh on 2017/11/27.
 */
public class TestLoopLinkedListWithLoopOpen {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Test
    public void testInitialization() {
        List<Integer> list = new LoopLinkedList<>();
        Assert.assertTrue(list.size() == 0);
    }

    @Test
    public void testAddAndGet() {
        List<Integer> list = new LoopLinkedList<>();
        ((LoopLinkedList)list).openLoop();
        list.add(2);
        Assert.assertTrue(list.size() == 1);
        Assert.assertTrue(list.get(0) == 2);
    }

    @Test
    public void testMultiAddAndRandomGet() {
        List<Integer> list = new LoopLinkedList<>();
        for (int i = 0; i < 1000; i++) {
            list.add(i);
        }

        Assert.assertTrue(list.size() == 1000);

        ((LoopLinkedList)list).openLoop();
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

        ((LoopLinkedList)list).openLoop();
        LoopLinkedList<Integer>.LinkedLoopItr itr = (LoopLinkedList<Integer>.LinkedLoopItr) list.iterator();

        int i = 0;
        while (itr.hasNextWithLoop(1)) {
            Integer element = itr.next();
            logger.info((i % list.size()) + " = " + element);
            Assert.assertTrue(element == (i % list.size()));
            i++;
        }

        while (itr.hasNextWithLoop(2)) {
            Integer element = itr.next();
            logger.info((i % list.size()) + " = " + element);
            Assert.assertTrue(element == (i % list.size()));
            i++;
        }

        i = 999;
        itr = (LoopLinkedList<Integer>.LinkedLoopItr) list.iterator();
        while (itr.hasPreviousWithLoop(1)) {
            Integer element = itr.previous();
            logger.info((i % list.size()) + " = " + element);
            Assert.assertTrue(element == (i % list.size()));
            i--;
        }
    }


    @Test
    public void testIteratorRemove() {
        List<Integer> list = new LoopLinkedList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
//        LoopLinkedList.LinkedLoopItr itr = (LoopLinkedList.LinkedLoopItr) list.iterator();
//        while (itr.hasNext()) {
//            System.out.println(itr.next());
//        }

        list.forEach(System.out::println);

        LoopLinkedList<Integer>.LinkedLoopItr itr = (LoopLinkedList<Integer>.LinkedLoopItr) list.iterator();


        Integer i = itr.next();
        itr.remove();
        System.out.println("delete " + i);
        i = itr.next();
        itr.remove();
        System.out.println("delete " + i);
        i = itr.next();
        System.out.println("jump " + i);
        i = itr.next();
        System.out.println("jump " + i);
        i = itr.next();
        itr.remove();
        System.out.println("delete " + i);

        i = itr.previous();
        itr.remove();
        System.out.println("delete " + i);

        i = itr.previous();
        itr.remove();
        System.out.println("delete " + i);
        list.forEach(System.out::println);
    }
    @Test
    public void testAdd() {
        List<Integer> list = new LoopLinkedList<>();
        boolean a = list.add(1);
        System.out.println(a);
        ((LoopLinkedList)list).openLoop();
        LoopLinkedList<Integer>.LinkedLoopItr itr = (LoopLinkedList<Integer>.LinkedLoopItr) list.iterator();
        while (itr.hasNextWithLoop(1)) {
            System.out.println(itr.next());
        }
    }

    private <E>boolean LoopListCompareToArray(LoopLinkedList<E> list, E[] arr) {
        if (list.size() != arr.length) {
            return false;
        }


    }
}
