package com.frame.util.structure;

import com.frame.exceptions.invalid.InvalidStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by fdh on 2017/11/23.
 */
// todo check the documents to make sure my implementations is reasonable.
public class LoopLinkedList<E> extends AbstractSequentialList<E>
        implements List<E>, Cloneable, java.io.Serializable {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

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

    public class LinkedLoopItr implements ListIterator<E> {

        /**
         * The element returned when calling {@code next()}
         */
        private Node<E> cursor;

        /**
         * The index of the element returned when calling {@code next()}
         */
        private int cursorIndex;

        /**
         * Used for removing
         */
        private Node<E> lastReturned;
        /**
         * Check if the list has been modified after the iterator had been created.
         */
        private int expectedCount = modCount;

        /**
         * The number of repeating visit with {@code next()}, as soon as the {@code cursor} return header except the first visit, the {@code nextLoop} increase.
         */
        private int nextLoop = 0;

        /**
         * The number of repeating visit with {@code previous()}, as soon as the {@code cursor} return header except the first visit, the {@code previousLoop} increase.
         */
        private int previousLoop = 0;

        /**
         * Flag represents if the iterator visit the list for the first time with {@code next()}
         */
        private boolean nextFirstVisitFlag = true;

        /**
         * Flag represents if the iterator visit the list for the first time with {@code previous()}
         */
        private boolean previousFirstVisitFlag = true;

        /**
         * Constructor with specific node
         *
         * @param next
         */
        public LinkedLoopItr(Node<E> next) {
            initItr(next);
        }

        /**
         * Constructor with specific index
         *
         * @param nextIndex
         */
        public LinkedLoopItr(int nextIndex) {
            initItr(nextIndex);
        }


        /**
         * called in constructor, pack the construction logic
         *
         * @param cursor the initial node, the {@code nextIndex} also should be initialized.
         */
        private void initItr(Node<E> cursor) {
            this.cursor = cursor;
            this.cursorIndex = indexOf(cursor);
        }

        /**
         * Called in constructor, pack the construction logic
         *
         * @param nextIndex the initial node's index
         */

        private void initItr(int nextIndex) {
            this.cursorIndex = getRealIndex(nextIndex);
            this.cursor = nodeOf(nextIndex);
        }

        /**
         * Return the index of the specific node, if the node is not in the list, return null
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
            if (cursorIndex < (size >> 1)) {
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
         * Check if the list has been modified after the iterator had been created.
         *
         * @return true if the list has been modified, otherwise, false.
         */
        private boolean checkModification() {
            return expectedCount != modCount;
        }

        /**
         * limit the loop visit times when call {@code next()}
         * You can use like this,
         * {@code
         * while(itr.hasNext() && itr.limitNextLoop(1)) {
         * ...
         * }
         * }
         * Then, the iterator will loop only once.
         *
         * @param nextLoop the time of loop visit
         * @return
         */
        public boolean limitNextLoop(int nextLoop) {
            return !(this.nextLoop >= nextLoop);
        }


        /**
         * limit the loop visit times when call {@code previous()}
         * You can use like this,
         * {@code
         * while(itr.hasPrevious() && itr.limitPreviousLoop(1)) {
         * ...
         * }
         * }
         * Then, the iterator will loop only once.
         *
         * @param previousLoop the time of loop visit
         * @return
         */
        public boolean limitPreviousLoop(int previousLoop) {
            return !(this.previousLoop >= previousLoop);
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
         * Loop list always has next element
         *
         * @return true, because the loop list always has next element.
         */
        @Override
        public boolean hasNext() {
            return true;
        }

        /**
         * Query the next element
         *
         * @return
         */
        @Override
        public E next() {
            if (checkModification()) {
                throw new ConcurrentModificationException();
            }
            if (cursor == null) {
                throw new NullPointerException("the cursor node in the list is null");
            }

            if (cursor.item == null) {
                logger.warn("call next(), but the list is empty.");
                return null;
            }

            E item = cursor.item;
            if (cursor == header) {
                if (nextFirstVisitFlag) {
                    nextFirstVisitFlag = false;
                } else {
                    nextLoop++;
                }
            }
            lastReturned = cursor;
            cursor = cursor.next;
            // record loop time
            if (cursor == header) {
                if (nextFirstVisitFlag) {
                    nextFirstVisitFlag = false;
                } else {
                    nextLoop++;
                }
            }
            cursorIndex = getRealIndex(cursorIndex + 1);

            return item;
        }

        /**
         * Loop list always has previous element
         *
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
            cursorIndex = getRealIndex(cursorIndex - 1);
            if (cursor == header) {
                // the first element gotten by previous isn't header, so when the cursor meets header for the first time,
                // it's already loop once.
                previousLoop++;
            }
            if (cursor.item == null) {
                throw new NullPointerException("null value is not allowed in the list");
            }
            return cursor.item;
        }

        @Override
        public int nextIndex() {
            return this.cursorIndex;
        }

        @Override
        public int previousIndex() {
            return cursorIndex - 1;
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
            if (header.item == null) {
                header.item = e;
                size++;
                return;
            }
            Node<E> p = cursor.prev;
            Node<E> n = new Node<>(e);
            // add the node before the cursor
            connectNode(p, n, cursor);
            size++;
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
//        LnkedList<Integer> list = new LoopLinkedList<>();
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
