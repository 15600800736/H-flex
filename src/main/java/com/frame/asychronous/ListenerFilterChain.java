package com.frame.asychronous;

import java.util.LinkedList;

/**
 * Created by fdh on 2017/11/23.
 */
public class ListenerFilterChain extends ListenerFilter {

    private String chainName;

    class HeadFilter extends ListenerFilter {


        @Override
        public void execFilter() {
            // do nothing
        }
    }

    @Override
    public void execFilter() {

    }
}
