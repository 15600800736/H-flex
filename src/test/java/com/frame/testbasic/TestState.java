package com.frame.testbasic;


import com.frame.basic.flow.flows.AppendableLine;
import com.frame.basic.state.State;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by fdh on 2017/11/9.
 */
public class TestState {
    private Logger logger = LoggerFactory.getLogger(TestState.class);
    @Test
    public void testRandomModeBasicFunction() {
        AppendableLine<Integer> appendableLine = new AppendableLine<>();
        State<Integer,AppendableLine<Integer>> state = State.createState(appendableLine, State.StateMode.RANDOM_MODE, Integer.class);
        state.registerState(0, "not initialed")
                .registerState(1, "initialed")
                .registerState(2, "running")
                .registerState(4, "exception")
                .registerState(5, "closed").initialState(0);
        state.transform(5);
        logger.info(String.valueOf(state.getCurrentState()));
    }

    @Test
    public void testRandomModeNotInitialedException() {
        AppendableLine<Integer> appendableLine = new AppendableLine<>();
        State<Integer,AppendableLine<Integer>> state = State.createState(appendableLine, State.StateMode.RANDOM_MODE, Integer.class)
                .registerState(0, "not initialed")
                .registerState(1, "initialed")
                .registerState(2, "running")
                .registerState(4, "exception")
                .registerState(5, "closed");

        state.transform(5);
        logger.info(String.valueOf(state.getCurrentState()));
    }

    @Test
    public void testRandomModeInitialedRegisteredOrder() {
        AppendableLine<Integer> appendableLine = new AppendableLine<>();
        State<Integer,AppendableLine<Integer>> state = State.createState(appendableLine, State.StateMode.RANDOM_MODE, Integer.class)
                .initialState(0)
                .registerState(0, "not initialed")
                .registerState(1, "initialed")
                .registerState(2, "running")
                .registerState(4, "exception")
                .registerState(5, "closed");
        state.transform(5);
        logger.info(String.valueOf(state.getCurrentState()));
    }
}
