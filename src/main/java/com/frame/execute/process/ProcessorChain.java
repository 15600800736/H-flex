package com.frame.execute.process;

import com.frame.context.resource.Resource;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by fdh on 2017/7/28.
 */
public class ProcessorChain<T> extends Processor<T> {

    private BlockingQueue<Processor<?>> processorQueue;
    @Override
    public T exec() throws Exception {
        return null;
    }

}
