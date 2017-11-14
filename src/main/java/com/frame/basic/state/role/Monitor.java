package com.frame.basic.state.role;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by fdh on 2017/11/14.
 */
public class Monitor<C> extends Role {

    public Monitor() {
        super("monitor");
    }

    Map<C, BlockingQueue<Thread>> waitingList = new ConcurrentHashMap<>(/*todo how many*/);

    public void registerThread(C state, Thread t) {
        BlockingQueue<Thread> tQueue = this.waitingList.get(state);
        if (tQueue == null) {
            tQueue = new LinkedBlockingQueue<>();
            waitingList.put(state, tQueue);
        }
        // make sure that the thread is put into the queue
        while (true) {
            try {
                tQueue.put(t);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Monitor<Integer> m = new Monitor<>();
        m.registerThread(1, Thread.currentThread());
        System.out.println(m.waitingList.get(1));
    }
}
