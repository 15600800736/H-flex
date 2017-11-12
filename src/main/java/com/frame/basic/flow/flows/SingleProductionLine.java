package com.frame.basic.flow.flows;

import com.frame.execute.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by fdh on 2017/10/6.
 */
public class SingleProductionLine<P> extends Flow<P, P> {


    /**
     * <p>Represent if the line should stop</p>
     */
    private boolean isClosed;
    /**
     * <p>Used for interrupting</p>
     */
    private Thread currentThread;
    /**
     * <p>The executor's queue, each of the executors appended will be executed only once</p>
     */
    private BlockingQueue<Process> worker = new LinkedBlockingQueue<>();

    /**
     * <p>logger</p>
     */
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public SingleProductionLine(P production) {
        super(production);
        this.currentThread = Thread.currentThread();
    }

    protected class Process {
        /**
         * <p>The execute unit</p>
         */
        Executor<P, P> worker;

        P execute(P production) throws Exception {
            worker.setProduction(production);
            return worker.execute();
        }

    }


    @Override
    public void close() {

    }

    @Override
    protected Object exec() throws Exception {
        P production = this.production;
        while (!isClosed) {
            Process process = this.worker.take();
            process.execute(production);
        }
        return null;
    }

    public void appendWorker(Executor<P, P> worker) {
        Process process = new Process();
        process.worker = worker;
        // if some thread interrupt the line, then try put it again, means make it twice
        for (int i = 0; i < 2; i++) {
            try {
                this.worker.put(process);
                return;
            } catch (InterruptedException e) {
                if (isClosed) {
                    return;
                }
            }
        }
    }
}
