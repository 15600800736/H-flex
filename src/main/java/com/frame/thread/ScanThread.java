package com.frame.thread;

import java.util.concurrent.CyclicBarrier;

/**
 * Created by fdh on 2017/7/24.
 */

/**
 * This is a abstract class for a base of a thread used for scanning
 */
public abstract class ScanThread extends Thread {
    /**
     * <p>The barrier is used for control the main thread waiting until all of the scanning are finished,
     * So,every scanner should be injected a scannerBarrier.</p>
     */
    protected CyclicBarrier scannerBarrier;

    public ScanThread(CyclicBarrier scannerBarrier) {
        this.scannerBarrier = scannerBarrier;
    }
}
