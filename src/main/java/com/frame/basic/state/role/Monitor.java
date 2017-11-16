package com.frame.basic.state.role;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by fdh on 2017/11/14.
 */
public class Monitor extends Role {

    public Monitor() {
        super("monitor");
    }

    public Map<String, List<Object>> users = new ConcurrentHashMap<>(/*todo how many*/);

    public <C> void registerUser(String name, Object user, C initialState) {

    }


}
