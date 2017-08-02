package com.frame.util.structure;

import com.frame.enums.ListActionType;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by fdh on 2017/8/1.
 */
public class ConcurrentLinkedList<E> implements List<E> {

    class Node {
        E data;

        Node next;
    }

    class ListAction {

        private ListActionType actionType;

        private Object[] args;

        public ListAction(ListActionType actionType, Object[] args) {
            this.actionType = actionType;
            this.args = args;
        }

        public ListActionType getActionType() {
            return actionType;
        }

        public void setActionType(ListActionType actionType) {
            this.actionType = actionType;
        }

        public Object[] getArgs() {
            return args;
        }

        public void setArgs(Object[] args) {
            this.args = args;
        }
    }
    /**
     * <p>The size of the list</p>
     */
    private volatile int size;
    /**
     *
     */
    private ConcurrentLinkedQueue<ListAction> actionQueue = new ConcurrentLinkedQueue<>();

    /**
     *
     */
    private Node header;

    /**
     *
     */
    private Node tail;
    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean add(E e) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public E get(int index) {
        return null;
    }

    @Override
    public E set(int index, E element) {
        return null;
    }

    @Override
    public void add(int index, E element) {

    }

    @Override
    public E remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<E> listIterator() {
        return null;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return null;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return null;
    }
}
