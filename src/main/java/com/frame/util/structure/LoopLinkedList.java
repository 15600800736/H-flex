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
         * used for removing
         */
        private Node<E> lastReturned;
        /**
         * check if the list has been modified after the iterator had been created.
         */
        private int expectedCount = modCount;

        public LoopIterator(Node<E> next) {
            initItr(next);
        }

        public LoopIterator(int nextIndex) {
            initItr(nextIndex);
        }


        /**
         * called in constructor, pack the construction logic
         * @param next the initial node, the {@code nextIndex} also should be initialized.
         */
        private void initItr(Node<E> next) {
            this.next = next;
            this.nextIndex = indexOf(next);
        }

        /**
         * called in constructor, pack the construction logic
         * @param nextIndex the initial node's index
         */

        private void initItr(int nextIndex) {
            this.nextIndex = getRealIndex(nextIndex);

        }
        /**
         * return the index of the specific node, if the node is not in the list, return null
         *
         * @param node the specific node
         * @return index of the specific node, if the node is not in the list, null
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
                    return null;
                }
                return 0;
            }

            Node<E> n = header.next;
            int index = 1;
            while (n != header) {
                if (n == node) {
                    return index;
                }
                n = n.next;
                index++;
            }
            return null;
        }

        private Node<E> nodeOf(int i) {
            i = getRealIndex(i);
            Node<E> n = header;
            if (nextIndex < (size >> 1)) {
                for (int index = 0; index < i; index++) {
                    n = n.next;
                }
            } else {
                for (int index = 0; index < (size - i); index++) {
                    n = n.prev;
                }
            }
            return n;
        }

        private int getRealIndex(Integer index) {
            if (index == null) {
                throw new NullPointerException("index is null");
            }

            if (index < 0) {
                return size - index;
            }

            if (index < size) {
                return index;
            }
            return index % size;
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
            if (checkModification()) {
                throw new ConcurrentModificationException();
            }
            if (next == null) {
                throw new NullPointerException("the next node in the list is null");
            }

            if (next.item == null) {
                throw new NullPointerException("null value is not allowed in the list");
            }

            E item = next.item;

            next = next.next;
            nextIndex = getRealIndex(nextIndex + 1);

            return item;
        }

        /**
         * loop list always has previous element
         * @return true, because the loop list always has previous element.
         */
        @Override
        public boolean hasPrevious() {
            return true;
        }

        @Override
        public E previous() {
            if (checkModification()) {
                throw new ConcurrentModificationException();
            }
            if (next == null) {
                throw new NullPointerException("the current node in the list is null");
            }

            next = next.prev;
            nextIndex = getRealIndex(nextIndex - 1);

            if (next.item == null) {
                throw new NullPointerException("null value is not allowed in the list");
            }
            return next.item;
        }

        @Override
        public int nextIndex() {
            return this.nextIndex;
        }

        @Override
        public int previousIndex() {
            return this.nextIndex - 1;
        }

        @Override
        public void remove() {
            if (checkModification()) {
                throw new ConcurrentModificationException();
            }
            if (next == null) {
                throw new NullPointerException("the removed node in the list is null");
            }

            Node<E> n = next.next;

            if (n == null) {
                throw new NullPointerException("the next of the removed node in the list is null");
            }

            Node<E> p = next.prev;

            if (p == null) {
                throw new NullPointerException("the previous of the removed node in the list is null");
            }

            p.next = n;
            n.prev = p;

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
