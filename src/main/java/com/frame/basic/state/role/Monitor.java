package com.frame.basic.state.role;

import com.frame.basic.strategy.specific.state.ConcurrentTransformationStrategy;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by fdh on 2017/11/14.
 */
public class Monitor<C> extends Role {

    private ConcurrentTransformationStrategy<C> strategy;

    public Monitor(ConcurrentTransformationStrategy strategy) {
        super("monitor");
        this.strategy = strategy;
    }

    Map<C, BlockingQueue<Thread>> waitingList = new ConcurrentHashMap<>(/*todo how many*/);

    public void registerThread(C expectState, Thread t) {
        BlockingQueue<Thread> tQueue = this.waitingList.get(expectState);
        if (tQueue == null) {
            tQueue = new LinkedBlockingQueue<>();
            waitingList.put(expectState, tQueue);
        }
        // make sure that the thread is put into the queue
        while (true) {
            try {
                tQueue.put(t);
                // deal with failure of transformation
                this.strategy.transFailStrategy(expectState);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void notificationThread(C expectState) {
        
    }
}
