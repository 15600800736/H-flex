package com.frame.testbasic;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by fdh on 2017/11/25.
 */
public class TestIterator {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Test
    public void testIteratorRemove() {
        List<Integer> list = new LinkedList<>();
        for (int i = 0; i < (10); i++) {
            list.add(i);
        }

        list.forEach(i->logger.info(String.valueOf(i)));
        Iterator<Integer> itr = list.iterator();

        itr.next();
        itr.next();

        itr.remove();

        list.forEach(i-> logger.info(String.valueOf(i)));

    }

    @Test
    public void testIteratorAdd() {
        List<Integer> list = new LinkedList<>();
        ListIterator<Integer> itr = (ListIterator<Integer>) list.iterator();
        itr.add(0);



    }
}
