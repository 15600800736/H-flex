package com.frame.testbasic;


import com.frame.basic.flow.flows.AppendableLine;
import com.frame.basic.state.State;
import com.frame.exceptions.invalid.RepeatException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by fdh on 2017/11/9.
 */
public class TestState {
    private Logger logger = LoggerFactory.getLogger(TestState.class);

    // Test single thread
    @Test
    public void testRandomModeBasicFunction() {
        AppendableLine<Integer> appendableLine = new AppendableLine<>();
        State<Integer, AppendableLine<Integer>> state = State.createState(appendableLine, State.StateMode.RANDOM_MODE, Integer.class);
        try {
            state.registerState(0, "not initialed")
                    .registerState(1, "initialed")
                    .registerState(2, "running")
                    .registerState(4, "exception")
                    .registerState(5, "closed").initialState(0);
        } catch (RepeatException e) {
            e.printStackTrace();
        }
        state.transform(5);
        logger.info(String.valueOf(state.getCurrentState()));
    }

    @Test
    public void testRandomModeNotInitialedException() {
        AppendableLine<Integer> appendableLine = new AppendableLine<>();
        State<Integer, AppendableLine<Integer>> state = null;
        try {
            state = State.createState(appendableLine, State.StateMode.RANDOM_MODE, Integer.class)
                    .registerState(0, "not initialed")
                    .registerState(1, "initialed")
                    .registerState(2, "running")
                    .registerState(4, "exception")
                    .registerState(5, "closed");
        } catch (RepeatException e) {
            e.printStackTrace();
        }

        state.transform(5);
        logger.info(String.valueOf(state.getCurrentState()));
    }

    @Test
    public void testRandomModeInitialedRegisteredOrderException() {
        AppendableLine<Integer> appendableLine = new AppendableLine<>();
        State<Integer, AppendableLine<Integer>> state = null;
        try {
            state = State.createState(appendableLine, State.StateMode.RANDOM_MODE, Integer.class)
                    .initialState(0)
                    .registerState(0, "not initialed")
                    .registerState(1, "initialed")
                    .registerState(2, "running")
                    .registerState(4, "exception")
                    .registerState(5, "closed");
        } catch (RepeatException e) {
            e.printStackTrace();
        }
        state.transform(5);
        logger.info(String.valueOf(state.getCurrentState()));
    }

    @Test
    public void testRandomModeInitialedWithoutArgumentException() {
        AppendableLine<Integer> appendableLine = new AppendableLine<>();
        State<Integer, AppendableLine<Integer>> state = null;
        try {
            state = State.createState(appendableLine, State.StateMode.RANDOM_MODE, Integer.class)
                    .registerState(0, "not initialed")
                    .registerState(1, "initialed")
                    .registerState(2, "running")
                    .registerState(4, "exception")
                    .registerState(5, "closed")
                    .initialState();
        } catch (RepeatException e) {
            e.printStackTrace();
        }
        state.transform(5);
        logger.info(String.valueOf(state.getCurrentState()));
    }

    @Test
    public void testSequenceModeBasicFunction() {
        AppendableLine<Integer> appendableLine = new AppendableLine<>();
        State<Integer, AppendableLine<Integer>> state = State.createState(appendableLine, State.StateMode.SEQUENCE_MODE, Integer.class);
        try {
            state.registerState(0, "not initialed")
                    .registerState(1, "initialed")
                    .registerState(2, "running")
                    .registerState(4, "exception")
                    .registerState(5, "closed").initialState();
        } catch (RepeatException e) {
            e.printStackTrace();
        }
        state.transform();
        logger.info(String.valueOf(state.getCurrentState()));
    }

    @Test
    public void testSequenModeNotInitialedException() {
        AppendableLine<Integer> appendableLine = new AppendableLine<>();
        State<Integer, AppendableLine<Integer>> state = null;
        try {
            state = State.createState(appendableLine, State.StateMode.SEQUENCE_MODE, Integer.class)
                    .registerState(0, "not initialed")
                    .registerState(1, "initialed")
                    .registerState(2, "running")
                    .registerState(4, "exception")
                    .registerState(5, "closed");
        } catch (RepeatException e) {
            e.printStackTrace();
        }

        state.transform(5);
        logger.info(String.valueOf(state.getCurrentState()));
    }

    @Test
    public void testSequenceModeInitialedRegisteredOrderException() {
        AppendableLine<Integer> appendableLine = new AppendableLine<>();
        State<Integer, AppendableLine<Integer>> state = null;
        try {
            state = State.createState(appendableLine, State.StateMode.SEQUENCE_MODE, Integer.class)
                    .initialState()
                    .registerState(0, "not initialed")
                    .registerState(1, "initialed")
                    .registerState(2, "running")
                    .registerState(4, "exception")
                    .registerState(5, "closed");
        } catch (RepeatException e) {
            e.printStackTrace();
        }
        state.transform();
        logger.info(String.valueOf(state.getCurrentState()));


    }

    @Test
    public void testSequenceModeInitialedWithArgumentException() {
        AppendableLine<Integer> appendableLine = new AppendableLine<>();
        State<Integer, AppendableLine<Integer>> state = null;
        try {
            state = State.createState(appendableLine, State.StateMode.SEQUENCE_MODE, Integer.class)
                    .registerState(0, "not initialed")
                    .registerState(1, "initialed")
                    .registerState(2, "running")
                    .registerState(4, "exception")
                    .registerState(5, "closed")
                    .initialState(0);
        } catch (RepeatException e) {
            e.printStackTrace();
        }
        state.transform(5);
        logger.info(String.valueOf(state.getCurrentState()));
    }

    // Test multi thread
    @Test
    public void testRandomModeMultiThreadRegisterState() {
        AppendableLine<Integer> appendableLine = new AppendableLine<>();
        State<Integer, AppendableLine<Integer>> state = State.createState(appendableLine, State.StateMode.RANDOM_MODE, Integer.class);
        Thread t1 = new Thread(() -> {
            try {
                state.registerState(0, "not initialed");
            } catch (RepeatException e) {
                e.printStackTrace();
            }
        });
        Thread t2 = new Thread(() -> {
            try {
                state.registerState(0, "default value");
            } catch (RepeatException e) {
                e.printStackTrace();
            }
        });
        t1.start();
        t2.start();
    }

}
