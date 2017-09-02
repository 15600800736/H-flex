package com.frame.context;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fdh on 2017/9/3.
 */
public class ParserContext {

    /**
     *
     */
    private Map<String, Object> proxies = new HashMap<>(64);

    /**
     *
     */
    private Map<String, Method> actions = new HashMap<>(256);


}
