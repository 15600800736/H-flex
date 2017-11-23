package com.frame.asychronous;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by fdh on 2017/11/23.
 */
public abstract class ListenerFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Filter next;

    private Listener listener;

    public Filter getNext() {
        return next;
    }

    public void setNext(Filter next) {
        this.next = next;
    }

    public Listener getListener() {
        return listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
        if (next != null) {
            logger.warn("the final filter's next filter is non-null, but will be ignored.");
        }
    }

    @Override
    final public Filter execute() throws Exception {
        execFilter();
        return next;
    }




}
