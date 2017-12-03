package com.frame.testbasic;

import com.frame.util.structure.LoopLinkedList;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

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
        ((LoopLinkedList) list).openLoop();
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

        ((LoopLinkedList) list).openLoop();
        for (int i = 0; i < 100; i++) {
            int j = new Random().nextInt(1000);
            Assert.assertTrue(j == list.get(j));
        }


        // one element
        List<Integer> l = new LoopLinkedList<>();
        l.add(0);

        Assert.assertTrue(l.size() == 1);

        ((LoopLinkedList) l).openLoop();
        for (int i = 0; i < 1; i++) {
            int j = new Random().nextInt(1);
            Assert.assertTrue(j == l.get(j));
        }
    }

    @Test
    public void testMultiAddAndIteratorVisitAndLimit() {
        List<Integer> list = new LoopLinkedList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }

        ((LoopLinkedList) list).openLoop();
        LoopLinkedList<Integer>.LinkedLoopItr itr = (LoopLinkedList<Integer>.LinkedLoopItr) list.iterator();

        int i = 0;
        while (itr.hasNextWithLoop(1)) {
            Integer element = itr.next();
            logger.info((i % list.size()) + " = " + element);
            Assert.assertTrue(element == (i % list.size()));
            i++;
        }

        itr = (LoopLinkedList<Integer>.LinkedLoopItr) list.iterator();
        while (itr.hasNextWithLoop(2)) {
            Integer element = itr.next();
            logger.info((i % list.size()) + " = " + element);
            Assert.assertTrue(element == (i % list.size()));
            i++;
        }

        i = 9;
        itr = (LoopLinkedList<Integer>.LinkedLoopItr) list.iterator();
        while (itr.hasPreviousWithLoop(1)) {
            Integer element = itr.previous();
            logger.info((i % list.size()) + " = " + element);
            Assert.assertTrue(element == (i % list.size()));
            i--;
        }
        i = 9;
        itr = (LoopLinkedList<Integer>.LinkedLoopItr) list.iterator();
        while (itr.hasPreviousWithLoop(2)) {
            Integer element = itr.previous();
            logger.info(i % list.size() + " = " + element);
            Assert.assertTrue(element == (i % list.size()));
            i--;
            if (i == -1) {
                i = 9;
            }
        }
        list.add(10);
        itr = (LoopLinkedList<Integer>.LinkedLoopItr) list.iterator();
        i = 10;
        while (itr.hasPreviousWithLoop(100)) {
            Integer element = itr.previous();
            logger.info(i % list.size() + " = " + element);
            Assert.assertTrue(element == (i % list.size()));
            i--;
            if (i == -1) {
                i = 10;
            }
        }
        list.add(11);
        itr = (LoopLinkedList<Integer>.LinkedLoopItr) list.iterator();
        i = 0;
        while (itr.hasNextWithLoop(100)) {
            Integer element = itr.next();
            logger.info(i % list.size() + " = " + element);
            Assert.assertTrue(element == (i % list.size()));
            i++;
        }
    }


    @Test(expected = NoSuchElementException.class)
    public void testIteratorRemove() {
        List<Integer> list = new LoopLinkedList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }

        list.forEach(System.out::println);

        LoopLinkedList<Integer>.LinkedLoopItr itr = (LoopLinkedList<Integer>.LinkedLoopItr) list.iterator();

        Assert.assertTrue(listCompareToArray(list, new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}));
        // remove specific element
        // 0
        Integer i = itr.next();
        itr.remove();
        System.out.println("delete " + i);
        list.forEach(System.out::print);
        Assert.assertTrue(listCompareToArray(list, new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9}));
        System.out.println();
        // 1
        i = itr.next();
        itr.remove();
        System.out.println("delete " + i);
        list.forEach(System.out::print);
        Assert.assertTrue(listCompareToArray(list, new Integer[]{2, 3, 4, 5, 6, 7, 8, 9}));
        System.out.println();
        // 2
        i = itr.next();
        System.out.println("jump " + i);
        // 3
        i = itr.next();
        System.out.println("jump " + i);
        // 4
        i = itr.next();
        itr.remove();
        System.out.println("delete " + i);
        Assert.assertTrue(listCompareToArray(list, new Integer[]{2, 3, 5, 6, 7, 8, 9}));
        list.forEach(System.out::print);
        System.out.println();
        // 3
        i = itr.previous();
        itr.remove();
        System.out.println("delete " + i);
        list.forEach(System.out::print);
        Assert.assertTrue(listCompareToArray(list, new Integer[]{2, 5, 6, 7, 8, 9}));
        System.out.println();
        // 2
        i = itr.previous();
        itr.remove();
        System.out.println("delete " + i);
        list.forEach(System.out::print);
        Assert.assertTrue(listCompareToArray(list, new Integer[]{5, 6, 7, 8, 9}));
        System.out.println();
        // previous remove and called next
        // 5
        i = itr.next();
        itr.remove();
        System.out.println("delete " + i);
        list.forEach(System.out::print);
        System.out.println();
        Assert.assertTrue(itr.previous() == 9);


        List<Integer> l = new LoopLinkedList<>();
        l.add(0);
        Assert.assertTrue(l.get(0) == 0);
        ListIterator<Integer> iterator = (ListIterator<Integer>) l.iterator();
        iterator.next();
        iterator.remove();

        iterator.previous();
    }

    @Test
    public void testHasNext() {
        List<Integer> list = new LoopLinkedList<>();
        for (int i = 0; i < (12); i++) {
            list.add(i);
        }
        Iterator<Integer> itr = list.iterator();

        List<Integer> result = new LinkedList<>();
        while (itr.hasNext()) {
            Integer element = itr.next();
            result.add(element);
        }
        Assert.assertTrue(listCompareToArray(result, new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11}));

        ((LoopLinkedList) list).openLoop();
        itr = list.iterator();
        while (itr.hasNext()) {
            Integer element = itr.next();
            System.out.println(element);
        }
        Assert.assertTrue(listCompareToArray(result, new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11}));
        ((LoopLinkedList) list).closeLoop();
        itr = list.iterator();
        while (itr.hasNext()) {
            Integer element = itr.next();
            System.out.println(element);
        }
        Assert.assertTrue(listCompareToArray(result, new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11}));
    }

    @Test
    public void testToArray() {
        List<Integer> list = new LoopLinkedList<>();
        for (int i = 0; i < (10); i++) {
            list.add(i);
        }
        Integer[] arr = list.toArray(new Integer[0]);
        Assert.assertTrue(Arrays.equals(arr, new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}));
    }

    @Test
    public void testAdd() {
        List<Integer> list = new LoopLinkedList<>();
        boolean a = list.add(1);
        System.out.println(a);
        ((LoopLinkedList) list).openLoop();
        LoopLinkedList<Integer>.LinkedLoopItr itr = (LoopLinkedList<Integer>.LinkedLoopItr) list.iterator();

        List<Integer> result = new LinkedList<>();
        while (itr.hasNextWithLoop(1)) {
            result.add(itr.next());
        }
        Assert.assertTrue(listCompareToArray(result, new Integer[]{1}));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemove() {
        List<Integer> list = new LoopLinkedList<>();
        for (int i = 0; i < (10); i++) {
            list.add(i);
        }
        ((LoopLinkedList)list).openLoop();
        Assert.assertTrue(listCompareToArray(list, new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}));
        list.remove(0);
        Assert.assertTrue(listCompareToArray(list, new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9}));
        list.remove(6);
        Assert.assertTrue(listCompareToArray(list, new Integer[]{1, 2, 3, 4, 5, 6, 8, 9}));
        list.remove(2);
        Assert.assertTrue(listCompareToArray(list, new Integer[]{1, 2, 4, 5, 6, 8, 9}));
        list.remove(20);
        Assert.assertTrue(listCompareToArray(list, new Integer[]{1, 2, 4, 5, 6, 8}));

        ((LoopLinkedList)list).closeLoop();
        list.remove(30);
    }

    private <E> boolean listCompareToArray(List<E> list, E[] arr) {
        return Arrays.equals(list.toArray(), arr);
    }
}
