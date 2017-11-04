package com.frame.util.structure;

import com.frame.exceptions.invalid.InvalidArgumentsException;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by fdh on 2017/11/4.
 */
public class State<C, T> {

    //todo synchronized
    public enum StateMode {
        LINK_MODE, RANDOM_MODE;
    }

    private C currentState;

    private Map<C, String> states = new ConcurrentHashMap<>(8);

    private T instance;

    private List<C> stateChain;

    private Iterator<C> itr;

    private AtomicInteger stage = new AtomicInteger(0);
    final private StateMode mode;

    private State(StateMode mode) {
        this.mode = mode;
    }

    public static <T> State createState(T instance) {
        return createState(instance, StateMode.RANDOM_MODE);
    }

    public static <C, T> State createState(T instance, StateMode mode) {
        State<C, T> state = new State(mode);
        if (state.getMode() == StateMode.LINK_MODE) {
            state.stateChain = new LinkedList<>();
            state.itr = state.stateChain.iterator();
        }
        state.instance = instance;
        if (state.states == null) {
            state.states = new ConcurrentHashMap<>(8);
        }
        return state;
    }

    public State registerState(C code, String description) {
        String old = this.states.putIfAbsent(code, description);
        if (old != null) {
            throw new InvalidArgumentsException("code", code.toString(), "repeated state code.");
        }
        return this;
    }

    public State initialState(C code) {
        if (this.mode == StateMode.RANDOM_MODE) {
            currentState = code;
        } else {
            throw new InvalidArgumentsException("code", code.toString(), "initialState("
                    + currentState != null ? currentState.getClass().getName() : "C" + " code) can't be apply to a LINK mode state");
        }
        return this;
    }

    public State initialState() {
        if (this.mode == StateMode.LINK_MODE) {
            currentState = itr.next();
        }

        return this;
    }
    public void transform() {
        if (mode != StateMode.LINK_MODE) {
            throw new InvalidArgumentsException("empty argument", "null", "transform() need a LINK mode state, you should use transform(" +
                    currentState != null ? currentState.getClass().getName() : "C" + " code)");
        }
        currentState = itr.next();
    }

    public C getCurrentState() {
        return currentState;
    }

    public StateMode getMode() {
        return mode;
    }

}
