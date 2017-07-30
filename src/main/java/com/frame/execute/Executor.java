package com.frame.execute;

import com.frame.execute.structure.AppendableTask;

import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by fdh on 2017/7/17.
 */

/**
 * <p>Executor is a abstract class that represents a executor that can be a part of a {@link AppendableTask}, more like a worker.
 * The Executor receives a {@code production} and after analysing or processing the production, it returns its result,
 * maybe the production itself, maybe not. So, we declare two types P and T represent the production type and result type.
 * The Executor also implements Callable, means it can be invoked in an independent thread.
 * What's more, in {@code execute()}, the executor add pre and post process around the real logic implementation {@code exec()}, for {@code exec()}
 * returns the raw type {@link Object}, so you should transform its type in {@code postProcessForExecute()}</p> like its default implementation, while
 * the default pre-dealing is empty.
 * The executor also has a barrier, which is used for let other partners wait until all of the works are done, all the executor
 * will wait at the end of {@code postProcessForExecute()}, and of course, you can make it in your way.For the moment, the executor can only be executed
 * by one thread.</p>
 * @param <P> the production type
 * @param <T> the result type
 */
public abstract class Executor<P,T>
        implements Callable<T>,Executable<T> {

    /**
     * <p>The barrier which let other partners wait, threads will wait at the end of execute()</p>
     */
    protected CyclicBarrier barrier;

    /**
     * <p>The production that is passed into the executor, its the basic or target of what the executors do</p>
     */
    protected P production;

    /**
     * <p>Create an executor with nothing</p>
     */
    public Executor() {
    }

    /**
     * <p>Create an executor with a production</p>
     * @param production
     */
    public Executor(P production) {
        this.production = production;
    }

    /**
     * <p>Create an executor with production and barrier</p>
     * @param barrier
     * @param production
     */
    public Executor(CyclicBarrier barrier,P production) {
        this.barrier = barrier;
        this.production = production;
    }

    public void setBarrier(CyclicBarrier barrier) {
        this.barrier = barrier;
    }

    /**
     * <p>The main method in Executor, it will be called after {@code prepareForExecutor} and before {@code postProcessForExecute}.
     * Sub-classes should overwritten this method to implements your own logic</p>
     * @return
     * @throws Exception
     */
    protected abstract Object exec() throws Exception;

    /**
     * the entrance of executor, default implementation give you a chance to prepare and post-process
     * @return
     * @throws Exception
     */
    public final T execute() throws Exception {
        try {
            prepareForExecute();
            Object rawResult = exec();
            return postProcessForExecute(rawResult);
        } finally {
            if(this.barrier != null) {
                this.barrier.await();
            }
        }

    }
    /**
     * <p>Default call just run the execute{@code execute}</p>
     * @return
     * @throws Exception
     */
    @Override
    public T call() throws Exception {
        return execute();
    }

    /**
     * <p>Prepare the environment for execute, default implementation is empty</p>
     */
    public void prepareForExecute() {

    }

    /**
     * process the result of {@code execute}, default implementation is type-transforming.
     * @param result the result of {@code execute()}
     * @return
     */
    public T postProcessForExecute(Object result) {
        return (T)result;
    }

    public CyclicBarrier getBarrier() {
        return barrier;
    }

    public P getProduction() {
        return production;
    }

    public void setProduction(P production) {
        this.production = production;
    }
}
