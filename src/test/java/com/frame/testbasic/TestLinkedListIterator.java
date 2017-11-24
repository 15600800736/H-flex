package com.frame.testbasic;

import org.junit.Test;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by fdh on 2017/11/24.
 */
public class TestLinkedListIterator {
    @Test
    public void testLinkedListIterator() {
        List<Integer> list = new LinkedList<>();
        Iterator<Integer> itr = list.iterator();
        System.out.println(itr.next());
    }
}
