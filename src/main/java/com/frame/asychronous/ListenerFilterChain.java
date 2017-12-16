package com.frame.asychronous;

import java.util.LinkedList;

/**
 * Created by fdh on 2017/11/23.
 */
public class ListenerFilterChain extends ListenerFilter {

    private String chainName;

    private final HeadFilter head = new HeadFilter();

    class HeadFilter extends ListenerFilter {
        @Override
        public void execFilter() {
            // do nothing
        }
    }

    @Override
    public void execFilter() {
        ListenerFilter filter = head;
        while (head != null && head.getNext() != null) {
            filter = filter.getNext();
            filter.execFilter();
        }
    }
}
