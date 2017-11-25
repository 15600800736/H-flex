package com.frame.util.structure;

import com.frame.exceptions.invalid.InvalidStateException;

import java.util.*;

/**
 * Created by fdh on 2017/11/23.
 */
// todo check the documents to make sure my implementations is reasonable.
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

    class LinkedLoopItr implements ListIterator<E> {

        /**
         * the element returned when calling {@code next()}
         */
        private Node<E> cursor;

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

        public LinkedLoopItr(Node<E> next) {
            initItr(next);
        }

        public LinkedLoopItr(int nextIndex) {
            initItr(nextIndex);
        }


        /**
         * called in constructor, pack the construction logic
         * @param cursor the initial node, the {@code nextIndex} also should be initialized.
         */
        private void initItr(Node<E> cursor) {
            this.cursor = cursor;
            this.nextIndex = indexOf(cursor);
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


        /**
         * check if the list has been modified after the iterator had been created.
         * @return true if the list has been modified, otherwise, false.
         */
        private boolean checkModification() {
            return expectedCount != modCount;
        }


        @SafeVarargs
        private final void connectNode(Node<E>... nodes) {
            Node<E> cn;
            Node<E> nn;

            for (int i = 0; i < nodes.length - 1; i++) {
                cn = nodes[i];
                nn = nodes[i + 1];

                if (cn == null || nn == null) {
                    throw new NullPointerException("Some nodes in your parameter are null");
                }

                cn.next = nn;
                nn.prev = cn;
            }
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
            if (cursor == null) {
                throw new NullPointerException("the next node in the list is null");
            }

            if (cursor.item == null) {
                throw new NullPointerException("null value is not allowed in the list");
            }

            E item = cursor.item;

            lastReturned = cursor;
            cursor = cursor.next;
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
            if (cursor == null) {
                throw new NullPointerException("the current node in the list is null");
            }

            cursor = cursor.prev;
            lastReturned = cursor;
            nextIndex = getRealIndex(nextIndex - 1);

            if (cursor.item == null) {
                throw new NullPointerException("null value is not allowed in the list");
            }
            return cursor.item;
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
            if (cursor == null) {
                throw new NullPointerException("the current node in the list is null");
            }

            if (lastReturned == null) {
                throw new NullPointerException("the removed node in the list is null");
            }
            // means just call next(), and the iterator should stay where it is
            if (lastReturned == cursor.prev) {
                Node<E> p = lastReturned.prev;
                p.next = cursor;
                cursor.prev = p;
                //clear
                lastReturned.next = null;
                lastReturned.prev = null;
            // means just call previous(), and the iterator should move to next node
            } else if (lastReturned == cursor) {
                Node<E> p = lastReturned.prev;
                Node<E> n = lastReturned.next;
                p.next = n;
                n.prev = p;

                // iterator point to next element
                cursor = cursor.next;

                //clear
                lastReturned.prev = null;
                lastReturned.next = null;
            } else {
                throw new InvalidStateException("lastReturned equals cursor or cursor's previous node",
                        "lastReturned is + " + lastReturned.toString() + ": " + lastReturned.item.toString(),
                        this.getClass(),
                        null);
            }

            size--;
        }

        @Override
        public void set(E e) {
            if (checkModification()) {
                throw new ConcurrentModificationException();
            }
            lastReturned.item = e;
        }

        @Override
        public void add(E e) {
            if (checkModification()) {
                throw new ConcurrentModificationException();
            }
            if (e == null) {
                throw new NullPointerException("Null value is not allowed in the loop list");
            }
            Node<E> p = cursor.prev;
            Node<E> n = new Node<>(e);
            // add the node before the cursor
            connectNode(p, n, cursor);
        }

    }

    private int getRealIndex(Integer index) {
        if (size == 0) {
            return index;
        }

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


    @Override
    public ListIterator<E> listIterator(int index) {
        // cause this is a loop list, so we should mod the index by size
        index = getRealIndex(index);
        return new LinkedLoopItr(index);
    }

    @Override
    public int size() {
        return size;
    }


    /**
     * Test {@code connectNode(...)}
     */
//    public static void main(String[] args) {
//        Node<Integer> n1 = new Node<>(1);
//        Node<Integer> n2 = new Node<>(2);
//        Node<Integer> n3 = new Node<>(3);
//        Node<Integer> n4 = new Node<>(4);
//
//
//        LoopLinkedList<Integer> list = new LoopLinkedList<>();
//        LoopLinkedList.LinkedLoopItr litr = (LoopLinkedList.LinkedLoopItr) list.iterator();
//        litr.connectNode(n1, n2, n3, n4);
//        Node<Integer> n = n4;
//
//        while (n != null) {
//            System.out.println(n.item);
//            n = n.prev;
//        }
//    }

}
