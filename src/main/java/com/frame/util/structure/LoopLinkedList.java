package com.frame.util.structure;

import com.frame.exceptions.invalid.InvalidStateException;
import com.frame.util.lambda.Condition;
import sun.nio.ch.ThreadPool;

import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by fdh on 2017/11/23.
 */

/**
 * <p>LoopLinkedList is a implementation of {@link List}, this implementation extends the abstract class
 * {@link AbstractSequentialList}, the intern implementation of the list will invoking internal iterator's operation,
 * like add, remove.</p>
 * <p>There is a Loop iterator in the list which extends {@link ListIterator}</p>
 * <p>Every time you call the list's operation directly, the modCount will increase, so all of the iterators created before this operation will be invalid,
 * if you don't want this case, you should do like follow:
 * {@code
 *      Iterator<Integer> itr = list.iterator();
 *      itr.add(...);}
 * </p>
 * <p>The LoopList has two mode: Loop and non-loop, the default</p>
 *
 * @param <E>
 */
public class LoopLinkedList<E> extends AbstractSequentialList<E>
        implements List<E>, Cloneable, java.io.Serializable {

//    private Logger logger = LoggerFactory.getLogger(this.getClass());

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

    private static class NodeFactory {
        private static <E> Node<E> produceHeader() {
            Node<E> header = new Node<>();
            header.next = header;
            header.prev = header;
            return header;
        }
    }

    /**
     * Size of this list
     */
    private volatile int size;

    /**
     * the count of modification
     */
    private volatile int modCount;

    /**
     *
     */
    private volatile AtomicBoolean loop = new AtomicBoolean();
    /**
     * <p>The guard node in the list, it's also a normal node with data
     * the header will never be null, also, null is an invalid value in the list.
     * You can check {@code header.item == null} to make sure if the header has been filled.</p>
     * <p>So only if the header has non-null data, it's a real node, otherwise, it's only a place holder</p>
     */
    Node header = NodeFactory.<E>produceHeader();


    public LoopLinkedList() {
        this(false);
    }

    public LoopLinkedList(boolean openLoop) {
        this.loop.set(openLoop);
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

//        private boolean checkBound(int index) {
//            return index >=0 && index < size;
//        }

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
            for (int index = 0; index < i; index++) {
                n = n.next;
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
         * Loop list always has next element except there ain't any elements
         *
         * @return true, because the loop list always has next element.
         */
        @Override
        public boolean hasNext() {
            return !isEmpty() && !(!nextFirstVisitFlag && cursor == header);

        }

        public boolean hasNextWithLoop(int nextLoop) {
            boolean res = !isEmpty() && (!isLoop() || !(this.nextLoop >= nextLoop));
            if (!res) {
                this.nextLoop = 0;
            }
            return res;
        }

        public boolean hasNextWhile(Condition condition) {
            return !isEmpty() && condition.condition();
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
                // todo remove
                throw new NullPointerException("the cursor node in the list is null");
            }

            if (cursor.item == null) {
                throw new NoSuchElementException("element 0");
            }

            E item = cursor.item;
            // sign that we already visit the header
            if (cursor == header) {
                if (nextFirstVisitFlag) {
                    nextFirstVisitFlag = false;
                } else {
                    if (!isLoop()) {
                        throw new NoSuchElementException("element 0");
                    }
                }
            }
            lastReturned = cursor;
            cursor = cursor.next;

            // record loop time
            if (cursor == header) {
                if (!nextFirstVisitFlag) {
                    nextLoop++;
                }
            }
            cursorIndex = getRealIndex(cursorIndex + 1);

            return item;
        }

        /**
         * Loop list always has previous element except there ain't any elements
         *
         * @return true, because the loop list always has previous element.
         */
        @Override
        public boolean hasPrevious() {
            return !isEmpty() && cursor != header.prev;
        }

        public boolean hasPreviousWithLoop(int previousLoop) {
            boolean res = !isEmpty() &&
                    (!isLoop() || !(this.previousLoop >= previousLoop));
            if (!res) {
                this.nextLoop = 0;
            }
            return res;
        }

        public boolean hasPreviousWhile(Condition condition) {
            return !isEmpty() && condition.condition();
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
                throw new NoSuchElementException("element 0");
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
            // delete header and make it empty
            if (size == 1) {
                if (lastReturned == header && header.item != null) {
                    header.item = null;
                    size--;
                    return;
                }
            }
            // means just call next(), and the iterator should stay where it is
            if (lastReturned == cursor.prev) {
                Node<E> p = lastReturned.prev;
                p.next = cursor;
                cursor.prev = p;
                // update header
                if (lastReturned == header) {
                    header = cursor;
                }
                // clear
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
                // update header
                if (lastReturned == header) {
                    header = cursor;
                }
                // clear
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

    public boolean openLoop() {
        return this.loop.compareAndSet(false, true);
    }

    public boolean closeLoop() {
        return this.loop.compareAndSet(true, false);
    }

    public boolean isLoop() {
        return loop.get();
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

    private int getRealIndex(Integer index) {
        if (size == 0) {
            return index;
        }

        if (index == null) {
            throw new NullPointerException("index is null");
        }

        if (index < 0) {
            Integer tmp = -index % size;
            return size - (tmp == 0 ? size : tmp);
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

    @Override
    public E remove(int index) {
        if (!isLoop()) {
            if (!checkBound(index)) {
                throw new IndexOutOfBoundsException("element " + index);
            }
        }
        E e = super.remove(index);
        modCount++;
        return e;
    }

    @Override
    public void add(int index, E element) {
        super.add(index, element);
        modCount++;
    }

    private boolean checkBound(int index) {
        return index > -1 && index < size;
    }


    //todo temporary method, remove
    public int temporyGetRealIndex(int index) {
        return getRealIndex(index);
    }
//    @Override
//    public boolean add(E e) {
//        LinkedLoopItr itr = new LinkedLoopItr(header);
//
//        return true;
//    }

    // test getRealIndex
    public static void main(String[] args) {
        LoopLinkedList<Integer> list = new LoopLinkedList<>();
        list.size = 10;
        for (int i = -100000; i < 100000; i++) {
            int tmp = list.getRealIndex(i);
            if (tmp < 0 || tmp >= 10) {
                System.out.println("error");
                return;
            }
        }

        System.out.println("success!");
    }

}
