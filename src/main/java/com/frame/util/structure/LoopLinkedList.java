package com.frame.util.structure;

import java.util.*;

/**
 * Created by fdh on 2017/11/23.
 */
public class LoopLinkedList<E> extends AbstractSequentialList<E>
        implements List<E>, Cloneable, java.io.Serializable {

    private static class Node<E> {

        E item;
        Node<E> next;
        Node<E> prev;

        public Node() {
        }

        public Node(E item) {
            this(null, item, null);
        }

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }

    }

    private volatile int size;

    private volatile int modCount;

    /**
     * <p>The guard node in the list, it's also a normal node with data
     * the header will never be null, also, null is an invalid value in the list.
     * You can check {@code header.item == null} to make sure if the header has been filled.</p>
     * <p>So only if the header has non-null data, it's a real node, otherwise, it's only a place holder</p>
     */
    Node header = new Node();

    // make a loop
    {
        header.next = header;
        header.prev = header;
    }

    class LoopIterator implements ListIterator<E> {

        /**
         * the element returned when calling {@code next()}
         */
        private Node<E> next;

        /**
         * the index of the element returned when calling {@code next()}
         */
        private int nextIndex;

        /**
         * check if the list has been modified after the iterator had been created.
         */
        private int expectedCount = modCount;

        public LoopIterator(Node<E> next) {
            this.next = next;

        }

        public LoopIterator(int nextIndex) {
            this.nextIndex = nextIndex;
        }


        /**
         * return the index of the specific node, if the node is not in the list, return -1
         *
         * @param node the specific node
         * @return index of the specific node, if the node is not in the list, -1
         */
        private Integer indexOf(Node<E> node) {
            if (checkModification()) {
                throw new ConcurrentModificationException();
            }
            if (node == null) {
                throw new NullPointerException("null is not an allowed node in LoopLinkedList");
            }
            if (node == header) {
                if (header.item == null) {
                    return -1;
                }
                return 0;
            }

            Node<E> n = header.next;
            // todo
            return 0;
        }

        /**
         * check if the list has been modified after the iterator had been created.
         * @return true if the list has been modified, otherwise, false.
         */
        private boolean checkModification() {
            return expectedCount != modCount;
        }
        /**
         * loop list always has next element
         *
         * @return true, because the loop list always has next element.
         */
        @Override
        public boolean hasNext() {
            return true;
        }

        /**
         * query the next element
         *
         * @return
         */
        @Override
        public E next() {
            return null;
        }

        @Override
        public boolean hasPrevious() {
            return false;
        }

        @Override
        public E previous() {
            return null;
        }

        @Override
        public int nextIndex() {
            return 0;
        }

        @Override
        public int previousIndex() {
            return 0;
        }

        @Override
        public void remove() {

        }

        @Override
        public void set(E e) {

        }

        @Override
        public void add(E e) {

        }

    }

    @Override
    public ListIterator<E> listIterator(int index) {
        // cause this is a loop list, so we should mod the index by size
        if (isEmpty()) {

        }
        int realIndex = index % size;
        return null;
    }

    @Override
    public int size() {
        return 0;
    }


}
