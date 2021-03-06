package com.frame.state;

import com.frame.exceptions.invalid.InvalidArgumentsException;
import com.frame.exceptions.invalid.InvalidStateException;
import com.frame.exceptions.invalid.RepeatException;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by fdh on 2017/11/4.
 */
public class State<C, T> {

    //todo exceptions
    public enum StateMode {
        SEQUENCE_MODE, RANDOM_MODE;
    }
    /**
     * the current state, update atomically
     */
    private AtomicReference<C> currentState = new AtomicReference<>();

    /**
     * map the state's code and description
     */
    private Map<C, String> states = new ConcurrentHashMap<>(8);

    /**
     * the user of this state
     */
    private T instance;

    /**
     * for record the order of the state, only not null when SEQUENCE_MODE
     */
    private List<C> stateChain;

    /**
     * use for visiting the next state, only not null when SEQUENCE_MODE
     */
    private Iterator<C> itr;

    /**
     * 0-for default value
     * 1-for starte create
     * 2-for complete create
     * 3-for starting state registering
     * 4-for registering completely, starting initialization
     * 5-for initialization completely
     */
    private AtomicInteger stage = new AtomicInteger();

    private final Map<Integer, String> stageDescription = new ConcurrentHashMap<>(16);
    {
        stageDescription.put(0, "default value, nothing done");
        stageDescription.put(1, "start create");
        stageDescription.put(2, "complete create");
        stageDescription.put(3, "starting state registering");
        stageDescription.put(4, "registering completely, starting initialization");
        stageDescription.put(5, "initialization completely");
    }

    private final StateMode mode;

    private State(StateMode mode) {
        this.mode = mode;
    }

    public static <C, T> State<C, T> createState(T instance, Class<C> codeClass) {
        return createState(instance, StateMode.RANDOM_MODE, codeClass);
    }

    public static <C, T> State<C, T> createState(T instance, StateMode mode, Class<C> codeClass) {
        State<C, T> state = new State<>(mode);
        if (state.stage.compareAndSet(0, 1)) {
            if (state.getMode() == StateMode.SEQUENCE_MODE) {
                state.stateChain = new LinkedList<>();
            }
            state.instance = instance;
            if (state.states == null) {
                state.states = new ConcurrentHashMap<>(8);
            }
        }
        state.stage.compareAndSet(1, 2);
        return state;
    }

    public State<C, T> registerState(C state, String description) throws RepeatException {
        this.stage.compareAndSet(2, 3);
        if (stage.get() == 3) {
            String old = this.states.putIfAbsent(state, description);
            if (old != null) {
                throw new RepeatException("state", state.toString());
            }
            if (this.mode == StateMode.SEQUENCE_MODE) {
                this.stateChain.add(state);
            }
            return this;
        } else {
            throw new InvalidStateException(stageDescription.get(3), stageDescription.get(stage), this.instance.getClass(), null);
        }
    }

    public State<C, T> initialState(C state) {
        this.stage.compareAndSet(3, 4);
        if (this.stage.get() == 4) {
            if (this.mode == StateMode.RANDOM_MODE) {
                if (this.states.get(state) == null) {
                    throw new InvalidArgumentsException("state", state.toString(), "state " + state + " unregistered");
                }
                currentState.compareAndSet(null, state);
                if (currentState != null) {
                    this.stage.compareAndSet(4, 5);
                }
            } else {
                throw new InvalidArgumentsException("state", state.toString(), "initialState("
                        + (currentState != null && currentState.get() != null ? currentState.get().getClass().getName() : "C") + " state) can't be apply to a SEQUENCE mode state");
            }
        } else  {
            List<String> solutions = new LinkedList<>();
            solutions.add("invoke register(" + (this.currentState != null && this.currentState.get() != null ?
                                this.currentState.get().getClass() :
                                "C") + " state)");
            throw new InvalidStateException(stageDescription.get(4), stageDescription.get(stage.get()), this.instance.getClass(), solutions);
        }
        return this;
    }


    public State<C, T> initialState() {
        this.stage.compareAndSet(3,4);
        if (stage.get() == 4) {
            if (this.mode == StateMode.SEQUENCE_MODE) {
                if (this.states.isEmpty()) {
                    List<String> solutions = new LinkedList<>();
                    solutions.add("invoke register(" + (this.currentState != null && this.currentState.get() != null ? this.currentState.get().getClass() : "C")
                            + " state)");
                    throw new InvalidStateException("At least one state registered", "no state registered", this.instance.getClass(), solutions);
                }
                if (itr == null) {
                    itr = stateChain.iterator();
                }
                this.currentState.compareAndSet(null, itr.next());
                if (currentState.get() != null) {
                    this.stage.compareAndSet(4, 5);
                }
            } else {
                throw new InvalidArgumentsException("empty argument", "empty", "initialState() can't be apply to RANDOM mode state");
            }
        } else {
            List<String> solutions = new LinkedList<>();
            solutions.add("invoke register(" + (this.currentState != null && this.currentState.get() != null ?
                    this.currentState.get().getClass() :
                    "C") + " state)");
            //todo
            throw new InvalidStateException(stageDescription.get(4), stageDescription.get(stage.get()), this.instance.getClass(), solutions);
        }
        return this;
    }

    public void transform() {
        C cs = currentState.get();
        if (cs == null) {
            List<String> list = new LinkedList<>();
            list.add("invoke initialState() or initialState(C state)");
            throw new InvalidStateException("initialized",
                                        "not initialized",
                                        this.instance.getClass(),
                                        list);
        }
        if (mode != StateMode.SEQUENCE_MODE) {
            throw new InvalidArgumentsException(
                    "empty argument",
                    "null",
                    "transform() need a LINK mode state, you should use transform(" +
                            (currentState != null && currentState.get() != null ? currentState.get().getClass().getName() : "C")
                            + " code)"
            );
        }
        currentState.compareAndSet(cs, itr.next());
    }

    public void transform(C nextState) {
        C cs = currentState.get();
        if (cs == null) {
            List<String> list = new LinkedList<>();
            list.add("invoke initialState() or initialState(C state)");
            throw new InvalidStateException("initialized",
                    "not initialized",
                    State.class,
                    list);
        }
        if (mode != StateMode.RANDOM_MODE) {
            throw new InvalidArgumentsException("nextState",
                    "a value of state",
                    "transform(" + currentState != null && currentState.get() != null ? currentState.get().getClass().getName() : "C" +
                            " code) need a RANDOM_MODE mode state, you should use transform())");
        }
        currentState.compareAndSet(cs, nextState);
    }

    public void transform(C from, C to) {
        if (!this.currentState.compareAndSet(from, to)) {

        }
    }

    public C getCurrentState() {
        return currentState.get();
    }

    public StateMode getMode() {
        return mode;
    }

}
