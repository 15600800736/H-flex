package com.frame.testbasic;

import com.frame.util.structure.LoopLinkedList;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by fdh on 2017/11/27.
 */
public class TestLoopLinkedList {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Integer counter;

    @Test
    public void testInitialization() {
        List<Integer> list = new LoopLinkedList<>();
        Assert.assertTrue(list.size() == 0);
    }

    @Test
    public void testAddAndGet() {
        List<Integer> list = new LoopLinkedList<>();
        ((LoopLinkedList) list).openLoop();
        Assert.assertTrue(((LoopLinkedList) list).isLoop());
        list.add(2);
        Assert.assertTrue(list.size() == 1);
        Assert.assertTrue(list.get(0) == 2);
    }

    @Test
    public void testMultiAddAndRandomGet() {
        List<Integer> list = new LoopLinkedList<>();
        ((LoopLinkedList) list).openLoop();
        Assert.assertTrue(((LoopLinkedList) list).isLoop());
        for (int i = 0; i < 1000; i++) {
            list.add(i);
        }

        Assert.assertTrue(list.size() == 1000);


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
        ((LoopLinkedList) list).openLoop();
        Assert.assertTrue(((LoopLinkedList) list).isLoop());
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }

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
        ((LoopLinkedList) list).openLoop();
        Assert.assertTrue(((LoopLinkedList) list).isLoop());
        boolean a = list.add(1);
        System.out.println(a);
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
        ((LoopLinkedList) list).openLoop();
        Assert.assertTrue(listCompareToArray(list, new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}));
        list.remove(0);
        Assert.assertTrue(listCompareToArray(list, new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9}));
        list.remove(6);
        Assert.assertTrue(listCompareToArray(list, new Integer[]{1, 2, 3, 4, 5, 6, 8, 9}));
        list.remove(2);
        Assert.assertTrue(listCompareToArray(list, new Integer[]{1, 2, 4, 5, 6, 8, 9}));
        list.remove(20);
        Assert.assertTrue(listCompareToArray(list, new Integer[]{1, 2, 4, 5, 6, 8}));

        ((LoopLinkedList) list).closeLoop();
        list.remove(30);
    }

    @Test
    public void testHasNextWhile() {
        LoopLinkedList<Integer> list = new LoopLinkedList<>();
        list.openLoop();
        Assert.assertTrue(((LoopLinkedList) list).isLoop());
        for (int i = 0; i < (10); i++) {
            list.add(i);
        }
        LoopLinkedList<Integer>.LinkedLoopItr itr = (LoopLinkedList.LinkedLoopItr) list.iterator();
        counter = 0;
        List<Integer> result = new LinkedList<>();
        while (itr.hasNextWhile(() -> counter < 3)) {
            Integer element = itr.next();
            System.out.println(element);
            result.add(element);
            counter++;
        }

        Assert.assertTrue(listCompareToArray(result, new Integer[]{0, 1, 2}));
    }

    @Test
    public void testHasPreviousWhile() {
        LoopLinkedList<Integer> list = new LoopLinkedList<>();
        list.openLoop();
        Assert.assertTrue(((LoopLinkedList) list).isLoop());
        for (int i = 0; i < (10); i++) {
            list.add(i);
        }
        Assert.assertTrue(((LoopLinkedList) list).isLoop());
        LoopLinkedList<Integer>.LinkedLoopItr itr = (LoopLinkedList<Integer>.LinkedLoopItr) list.iterator();
        counter = 0;
        List<Integer> result = new LinkedList<>();
        while (itr.hasPreviousWhile(() -> counter < 3)) {
            Integer element = itr.previous();
            System.out.println(element);
            result.add(element);
            counter++;
        }
        Assert.assertTrue(listCompareToArray(result, new Integer[]{9, 8, 7}));
    }

    // todo bug:because when the iterator return the header with previous(), the nextFirstVisitFlag has already be set by false. how to deal with it.
    @Test
    public void testRoundTrip() {
        LoopLinkedList<Integer> list = new LoopLinkedList<>();
        for (int i = 0; i < (3); i++) {
            list.add(i);
        }
        LoopLinkedList<Integer>.LinkedLoopItr itr = (LoopLinkedList<Integer>.LinkedLoopItr) list.iterator();
        List<Integer> result = new LinkedList<>();
        for (int i = 0; i < (4); i++) {
            while (itr.hasNext()) {
                Integer element = itr.next();
                result.add(element);
                System.out.println(element);
            }
            while (itr.hasPrevious()) {
                Integer element = itr.previous();
                result.add(element);
                System.out.println(element);
            }
        }
        Assert.assertTrue(listCompareToArray(result,
                new Integer[]{
                        0, 1, 2,
                        2, 1, 0,
                        0, 1, 2,
                        2, 1, 0,
                        0, 1, 2,
                        2, 1, 0,
                        0, 1, 2,
                        2, 1, 0,
                }));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCloseLoop() {
        LoopLinkedList<Integer> list = new LoopLinkedList<>();
        LoopLinkedList<Integer>.LinkedLoopItr itr = (LoopLinkedList<Integer>.LinkedLoopItr) list.iterator();
        List<Integer> result = new LinkedList<>();
        while (itr.hasNext()) {
            Integer element = itr.next();
            System.out.println(element);
            result.add(element);
        }

        Assert.assertTrue(listCompareToArray(result, new Integer[]{}));
        result.clear();

        for (int i = 0; i < 3; i++) {
            list.add(i);
        }
        itr = (LoopLinkedList<Integer>.LinkedLoopItr) list.iterator();
        while (itr.hasNext()) {
            Integer element = itr.next();
            System.out.println(element);
            result.add(element);
        }
        Assert.assertTrue(listCompareToArray(result, new Integer[]{
                0, 1, 2
        }));
        result.clear();

        itr = (LoopLinkedList<Integer>.LinkedLoopItr) list.iterator();
        while (itr.hasNextWithLoop(2)) {
            Integer element = itr.next();
            System.out.println(element);
            result.add(element);
        }
        Assert.assertTrue(listCompareToArray(result, new Integer[]{
                0, 1, 2
        }));
        result.clear();
        list.remove(30);

    }

    @Test(expected = NoSuchElementException.class)
    public void testCloseLoopWithPrevious() {
        LoopLinkedList<Integer> list = new LoopLinkedList<>();
        List<Integer> result = new LinkedList<>();
        for (int i = 0; i < 3; i++) {
            list.add(i);
        }
        LoopLinkedList<Integer>.LinkedLoopItr itr = (LoopLinkedList<Integer>.LinkedLoopItr) list.iterator();

        result.add(itr.next());
        result.add(itr.previous());
        Assert.assertTrue(listCompareToArray(result, new Integer[]{0, 0}));
        result.add(itr.previous());

    }

    @Test(expected = NoSuchElementException.class)
    public void testForeverNext() {
        LoopLinkedList<Integer> list = new LoopLinkedList<>();
        for (int i = 0; i < (10); i++) {
            list.add(i);
        }

        Assert.assertFalse(list.isLoop());

        Iterator<Integer> itr = list.iterator();

        while (true) {
            itr.next();
        }
    }

    @Test(expected = NoSuchElementException.class)
    public void testForeverPrevious() {
        LoopLinkedList<Integer> list = new LoopLinkedList<>();
        for (int i = 0; i < (10); i++) {
            list.add(i);
        }

        Assert.assertFalse(list.isLoop());

        ListIterator<Integer> itr = (ListIterator<Integer>) list.iterator();

        while (true) {
            itr.previous();
        }
    }

    private <E> boolean listCompareToArray(List<E> list, E[] arr) {
        return Arrays.equals(list.toArray(), arr);
    }


}
