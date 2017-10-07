package com.frame.testbasic;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by fdh on 2017/10/7.
 */
public class Test {
    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        Thread t1 = new Thread(() -> {
            lock.lock();
            try {
                condition.await();
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().isInterrupted());
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

        });
        Thread t2 = new Thread(() -> {
            lock.lock();
            t1.interrupt();
            System.out.println(t1.isInterrupted());
            lock.unlock();
        });
        t1.start();
        t2.start();
    }
}
