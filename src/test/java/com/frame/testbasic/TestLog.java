package com.frame.testbasic;

import com.frame.asychronous.Filter;
import com.frame.asychronous.Listener;
import com.frame.asychronous.ListenerFilter;
import org.junit.Test;

/**
 * Created by fdh on 2017/11/23.
 */

public class TestLog {


    @Test
    public void testLog() {
        ListenerFilter listenerFilter = new ListenerFilter() {
            @Override
            public void execFilter() {
                // do nothing
            }
        };

        listenerFilter.setNext(new ListenerFilter() {
            @Override
            public void execFilter() {
                // do nothing
            }
        });

        listenerFilter.setListener(new Listener() {
            @Override
            public void registerFilter(Filter filter) {
                return;
            }

            @Override
            public void setKey(Object key) {

            }

            @Override
            public Object getKey() {
                return null;
            }

            @Override
            public Object callback() {
                return null;
            }
        });
    }
}
