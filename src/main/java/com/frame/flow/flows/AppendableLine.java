package com.frame.flow.flows;

import com.frame.execute.Executor;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

/**
 * Created by fdh on 2017/7/29.
 */

/**
 * <p>AppendableLine is a controlling-model abstracting the reality line of production. Appendable means you can always put the production into the line until
 * the method {@code close()} is called by another thread.
 * The method {@code close()} won't shut the line immediately, it just represents you cannot append productions into it, and the line will continue to process the
 * productions that already in the line. You can check the state of close by method {@code isClosed()}</p>
 * <p>The state of the line are follows:
 * <ol>
 * <li>start - start is the state that represents if the line has executed productions, once the line executed, the start will be true,
 * you can check it by {@code isStarted()}</li>
 * <li>close - close is the state that represents if you can put another production into the line. you can change it to true by invoking {@code close()},
 * and check it by invoking {@code isClosed()}</li>
 * <li>done - done is the state that represents if there are more productions unprocessed in the line, if there are, the done will be false,
 * otherwise, true. if the state done is true and the line has closed, the thread will be over, and if the close is false, the line will be waiting for another
 * production to process. you can check it by invoke {@code isDone()}</li>
 * </ol>
 * </p>
 * <p>After you execute the line, you can use {@code while(line.hasNext()) {
 * P production = line.get();
 * }} to gain the result of the line.</p>
 * <p>Here is an example of using AppendableLine:</p>
 * <pre>{@code
 *      for (int i = 0; i < 2; i++) {
 *          int finalI = i;
 *          Executor<Integer, Integer> executor = new Executor<Integer, Integer>() {
 *              @param <P> production type
 *              @Override
 *              protected Object exec() throws Exception {
 *                  this.production += finalI;
 *                  Thread.sleep(10L);
 *                  return this.production;
 *              }
 *          };
 *          line.appendWorker(executor);
 *      }
 *      Thread t = new Thread(() -> {
 *          try {
 *              for (int i = 1; i < 10; i++) {
 *                  line.appendProduction(i);
 *              }
 *              line.execute();
 *          } catch (Exception e) {
 *              e.printStackTrace();
 *          }
 *      });
 *      t.start();
 *      line.close();
 *      while (line.hasNext()) {
 *          System.out.println(line.get());
 *      }
 *   }
 * }</pre>
 * <p>And the out put is :3 4 5 6 7 8 9 10 2</p>
 * <p>So as we can see, the line can't guarantee the production will went out by its order of putting into the line, means
 * that the first production can go out as the last one.</p>
 */
public class AppendableLine<P> extends Flow<BlockingQueue<P>, List<P>> {

    /**
     * The class is used for identify the thread
     */
    protected class NamedThreadFactory implements ThreadFactory {
        ThreadFactory tf = Executors.defaultThreadFactory();

        public NamedThreadFactory() {

        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = tf.newThread(r);
            t.setName("line's task thread");
            return t;
        }
    }

    /**
     * <p>A process is a abstraction of a point in a line of factory, like a Node, it's a unit of processing.
     * It has worker and the next one, every production will be processed by current process and then pass it to next,
     * and take </p>
     */
    protected class Process implements Runnable {
        /**
         * <p>The object of execute to do work</p>
         */
        WorkerInfo worker;
        /**
         * <p>Next step, if null, means there is no more</p>
         */
        Process nextProcessor;

        /**
         * <p>The current thread</p>
         */
        Thread currentThread;

        /**
         * <p>The method will take productions from production cache, process it and pass it to next worker,
         * when a production is interrupted when being executed, the production will be put into the cache again, but
         * you should make sure that the production will be reset before interrupt, because the production will be processed again.</p>
         */
        @Override
        public void run() {
            this.currentThread = Thread.currentThread();
            for (; ; ) {
                try {
                    if (Thread.interrupted()) {
                        throw new InterruptedException();
                    }
                    if (isClosed() && isStarted() && isDone()) {
                        return;
                    }
                    P production = worker.productionCache.take();
                    try {
                        injectProduction(worker.worker, production);

                        P finishedProduction = worker.worker.execute();
                        // if this is last processor pushing production into tail processor, check if done(There are some mistakes here)
                        nextProcessor.worker.addProdution(finishedProduction);
                        if (this == lastProcessor) {
                            processedProduction.incrementAndGet();
                            LockSupport.unpark(lineThread);
                        }
                    } catch (Exception e) {
                        if (e instanceof InterruptedException) {
                            System.out.println("execute interrupt");
                            worker.productionCache.put(production);
                        } else {
                            e.printStackTrace();
                        }
                    }
                } catch (InterruptedException e) {

                }
            }
        }
    }

    /**
     * <p>The worker info holds some information about the worker, like where he is and what it deals with</p>
     */
    protected class WorkerInfo {
        /**
         * <p>The main part of the worker information</p>
         */
        Executor<P, P> worker;
        /**
         * <p>The index of the worker in this line</p>
         */
        Integer position;
        /**
         * <p>The productions that is waiting to be processed</p>
         */
        BlockingQueue<P> productionCache;

        /**
         * <p>If the cache should be infinite.</p>
         */
        Boolean needMax = false;

        /**
         * Constructor
         * @param worker
         * @param position
         */
        public WorkerInfo(Executor<P, P> worker, Integer position) {

            if (productionCacheSize == null) {
                productionCache = new LinkedBlockingQueue<>();
            } else {
                productionCache = new LinkedBlockingQueue<>(productionCacheSize);
            }
            this.worker = worker;
            this.position = position;
        }


        private WorkerInfo(Executor<P, P> worker, Integer position, Boolean needMax) {
            if (needMax || productionCacheSize == null) {
                productionCache = new LinkedBlockingQueue<>();
            } else {
                productionCache = new LinkedBlockingQueue<>(productionCacheSize);
            }
            this.worker = worker;
            this.position = position;
            this.needMax = needMax;
        }

        /**
         * <p>add a production to the production queue.</p>
         *
         * @param production
         */
        public void addProdution(P production) {
            try {
                this.productionCache.put(production);
            } catch (InterruptedException e) {
                while (!this.productionCache.add(production));
            }
        }
    }

    /**
     * <p>how many workers in this line</p>
     */
    private int workerNum = 16;

    /**
     * <p>The first processor is the next processor of header processor</p>
     */
    public Process firstProcessor;

    /**
     * <p>The previous processor of tail processor</p>
     */
    public Process lastProcessor = firstProcessor;

    /**
     * <p>Hold the line thread in order to interrupt</p>
     */
    private Thread lineThread;

    /**
     * <p>Cause the count of worker is certain, so we build a fixed thread pool.</p>
     */
    private ExecutorService pool = Executors.newFixedThreadPool(workerNum, new NamedThreadFactory());

    /**
     * <p>The size of cache, 16 default</p>
     */
    private Integer productionCacheSize = 16;

    /**
     * <p>The header processor takes charge of extracting production from cache and push it to first processor to execute </p>
     */
    private Process headerProcessor;

    /**
     * <p>The tail processor takes charge of accepting the finished productions and let other thread get it.</p>
     */
    private Process tailProcessor;

    /**
     * <p></p>
     */
    private AtomicInteger countOfProduction = new AtomicInteger(0);

    /**
     *
     */
    private AtomicInteger processedProduction = new AtomicInteger(0);

    /**
     * Constructors
     */

    public AppendableLine() {
        this.production = new LinkedBlockingQueue<>(productionCacheSize);
    }

    public AppendableLine(int productionCacheSize) {
        this.productionCacheSize = productionCacheSize;
        this.production = new LinkedBlockingQueue<>(productionCacheSize);
    }

    public AppendableLine(int workerNum, int productionCacheSize) {
        this.workerNum = workerNum;
        this.productionCacheSize = productionCacheSize;
        this.production = new LinkedBlockingQueue<>(productionCacheSize);
    }


    @Override
    public void prepareForExecute() {
        this.lineThread = Thread.currentThread();
        createHeaderProcessor();
        createTailProcessor();
        // start to inject production into first processor;
        if (headerProcessor != null) {
            pool.submit(headerProcessor.worker.worker);
        }

    }

    @Override
    protected Object exec() throws Exception {
        try {
            if (!isStarted()) {
                compareAndSetStarted(false, true);
            }
            Process currentProcessor = firstProcessor;

            while (currentProcessor != tailProcessor) {
                if (!pool.isShutdown()) {
                    pool.submit(currentProcessor);
                }
                currentProcessor = currentProcessor.nextProcessor;
            }
            for (; ; ) {
                if (isDone()) {
                    if (isClosed()) {
                        return tailProcessor.worker.productionCache;
                    } else {
                        LockSupport.park();
                    }
                }
                if (countOfProduction.get() == processedProduction.get()) {
                    if (!isDone()) {
                        compareAndSetDone(false, true);
                    }
                } else {
                    LockSupport.parkNanos(1000000000L);
                }
            }
        } finally {
            pool.shutdownNow();
        }
    }

    @Override
    public List<P> postProcessForExecute(Object result) {
        BlockingQueue<P> finishedProductions = (BlockingQueue<P>) result;
        List<P> finished = new LinkedList<>();
        P[] products = (P[]) finishedProductions.toArray();
        return Arrays.asList(products);
    }


    public void appendProduction(P production) {
        if (isClosed()) {
            return;
        }
        try {
            this.production.put(production);
            countOfProduction.incrementAndGet();
            if (isDone()) {
                compareAndSetDone(true, false);
            }
        } catch (InterruptedException e) {
            // todo
            e.printStackTrace();
        }
    }

    public AppendableLine appendWorker(Executor<P, P> worker) {
        // if the line has started, you can't add worker into the line.
        if (isStarted()) {
            return this;
        }
        // if this is the first time adding processor
        if (firstProcessor == null) {
            Process process = new Process();
            process.worker = new WorkerInfo(worker, 1);
            firstProcessor = process;
            lastProcessor = process;
            return this;
        }

        if (lastProcessor == null) {
            lastProcessor = firstProcessor;
        }

        Process process = new Process();
        process.worker = new WorkerInfo(worker, lastProcessor.worker.position + 1);
        // append processor
        lastProcessor.nextProcessor = process;
        // update tail processor
        lastProcessor = lastProcessor.nextProcessor;
        return this;
    }

    @Override
    public void close() {
        for (; ; ) {
            if (!isClosed() && isStarted()) {
                compareAndSetClosed(false, true);
                LockSupport.unpark(this.lineThread);
                for (Process p = headerProcessor; p != tailProcessor; p = p.nextProcessor) {
                    if (p.currentThread != null) {
                        // double check
                        if (!isClosed()) {
                            compareAndSetClosed(false, true);
                        }
                        p.currentThread.interrupt();
                    }
                }
                return;
            }
        }
    }

    public P get() {
        try {
            return tailProcessor.worker.productionCache.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return tailProcessor.worker.productionCache.poll();
    }

    /**
     *
     */
    private void createHeaderProcessor() {
        if (firstProcessor == null || headerProcessor != null) {
            return;
        }

        headerProcessor = new Process();
        headerProcessor.nextProcessor = firstProcessor;
        headerProcessor.worker = new WorkerInfo(new Executor<P, P>() {
            @Override
            protected Object exec() throws Exception {
                headerProcessor.currentThread = Thread.currentThread();

                for (; ; ) {
                    try {
                        if (isClosed() && isStarted() && AppendableLine.this.production.size() == 0) {
                            return null;
                        }
                        P production = AppendableLine.this.production.take();
                        if (headerProcessor.nextProcessor != null && headerProcessor.nextProcessor.worker != null) {
                            headerProcessor.nextProcessor.worker.addProdution(production);
                        }
                    } catch (InterruptedException e) {
                        // todo
                    }
                }
            }
        },
                0);
    }

    private void createTailProcessor() {
        if (lastProcessor == null || tailProcessor != null) {
            return;
        }

        tailProcessor = new Process();
        // the tail processor should have infinity production cache
        tailProcessor.worker = new WorkerInfo(null, lastProcessor.worker.position + 1, true);
        lastProcessor.nextProcessor = tailProcessor;
    }

    protected P injectProduction(Executor<P, P> worker, P production) {
        P originalProduction = worker.getProduction();
        worker.setProduction(production);
        return originalProduction;
    }

    public Boolean hasNext() {
        return !(isClosed() && isDone() && tailProcessor.worker.productionCache.isEmpty());
    }
}
