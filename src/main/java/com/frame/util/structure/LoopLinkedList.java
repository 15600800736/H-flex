package com.frame.util.structure;

import java.util.*;

/**
 * Created by fdh on 2017/11/23.
 */
public class LoopLinkedList<E> extends AbstractSequentialList<E>
    implements List<E>, Cloneable, java.io.Serializable {

    @Override
    public int size() {
        return 0;
    }

    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        public Node() {
        }

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

    Node header = new Node();
    // make a loop
    {
        header.next = header;
        header.prev = header;

    }

    class LoopIterator implements ListIterator<E> {

        /**
         * loop list always has next element
         * @return
         */
        @Override
        public boolean hasNext() {
            return true;
        }

        /**
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
        return null;
    }

}
