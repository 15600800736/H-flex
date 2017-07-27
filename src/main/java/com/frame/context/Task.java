package com.frame.context;

import com.frame.execute.EndExecutor;
import com.frame.execute.Executor;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by fdh on 2017/7/26.
 */

/**
 * <p>Task is a chain of executor, it has a {@link Executor} queue in it and will execute them by their order,
 * task will take executors util it meets the {@link EndExecutor}, so all of executors behind an {@link EndExecutor} will be ignore
 * because the task has been closed, you can call {@code isClosed()} to check if you can put a new {@link Executor} in it.
 * When all of the executor finish their work, the task will atomically change its {@code isClosed} to true, which means you
 * cannot put a new {@link Executor}. On the other hand, the field {@code isDone()} shows if the task has done what it should do,
 * in other words, if {@code isDone()} returns true, you can get the result as you expect, but if not, something may be wrong when task executes.
 * So there are three states in the task, they are
 * <ol>
 *     <li>finished rightly  {@code isDone() == true && isClosed() == true}</li>
 *     <li>finished wrongly {@code isDone() == false && isClosed() == true}</li>
 *     <li>not finished yet {@code isClosed() == false}</li>
 * </ol>
 * You needn't worry about {@code isClosed() == false && isDone() == true} , that won't happen</p>
 *
 */
public class Task {
    /**
     * <p>The capacity of blocking queue,when it is full, the thread that tries to put executors in it will be hang up. </p>
     */
    private int maxExecutor = 10;
    /**
     * <p>The single thread pool to execute the {@link Executor}, single to guarantee the executor's order</p>
     */
    private final ExecutorService pool = Executors.newSingleThreadExecutor();
    /**
     * <p>Task's executor, it should be well-initialization with resource, means you can call {@code executor.execute()
     * directly, the task doesn't take charge of initializing}</p>
     */
    private BlockingQueue<Executor<?>> executors = new LinkedBlockingQueue<>(maxExecutor);

    /**
     * <p>The field is to record if the task has been finished rightly, This 'finished' means all of the executors return with no wrong and exceptions.
     * Even if there is one executor throws exception when {@code execute()}, the field will stay false.</p>
     */
    private volatile AtomicBoolean isDone = new AtomicBoolean(false);

    /**
     * <p>The field represents if the task is alive, alive means you can dynamically add executors into it, and will it meets
     * {@link EndExecutor}, or a executor throws exception, it will be closed.</p>
     */
    private volatile AtomicBoolean isClosed = new AtomicBoolean(false);

    /**
     * <p>Return if the task has finished, if it's in main thread, it is always true,
     * if it is in another thread, it will return true when the thread is returned-safely,
     * and if the thread get exceptions and exit, or is handling work it will return false means
     * you can't trust this task's result and the task will recover itself.</p>
     *
     * @return
     */
    public Boolean isDone() {
        return isDone.get();
    }

    /**
     * @return
     */
    public void execute() {
        try {
            for (; ; ) {
                Executor<?> executor = executors.take();
                if (executor instanceof EndExecutor) {
                    while(compareAndSetIsDone(false,true) && compareAndSetIsClosed(false,true));
                    return;
                }
                Future<?> future = pool.submit(executor);
                future.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            while(compareAndSetIsDone(true,false) && compareAndSetIsClosed(false, true));
            e.printStackTrace();

        } finally {
            pool.shutdown();
        }
    }

    /**
     * <p>Dynamically append a executor in an task, if the queue is full, the thread will be hang up.</p>
     * @param executor
     * @return
     */
    public Boolean appendExecutor(Executor<?> executor) {
        return executors.offer(executor);
    }

    /**
     * <p>Atomically update the isDone field</p>
     * @param expect the expect value, if the current value equals it, the field will be update
     * @param update the new value.
     * @return if update is success
     */
    private Boolean compareAndSetIsDone(Boolean expect, Boolean update) {
        return isDone.compareAndSet(expect, update);
    }

    /**
     * <p>Atomically update the isDone field</p>
     * @param expect the expect value, if the current value equals it, the field will be update
     * @param update the new value.
     * @return if update is success
     */
    private Boolean compareAndSetIsClosed(Boolean expect, Boolean update) {
        return isDone.compareAndSet(expect, update);
    }

    public void setMaxExecutor(int maxExecutor) {
        this.maxExecutor = maxExecutor;
    }
}
