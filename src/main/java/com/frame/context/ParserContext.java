package com.frame.context;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by fdh on 2017/9/3.
 */
public class ParserContext {

    /**
     *
     */
    private Map<String, Object> proxies = new ConcurrentHashMap<>(64);

    /**
     *
     */
    private Map<String, Method> actions = new ConcurrentHashMap<>(256);

    /**
     *
     */
    private Map<String, Class> actionsClazz = new ConcurrentHashMap<>(64);


    /**
     *
     * @param clazzName
     * @param proxy
     * @return
     */
    public Object appendProxy(String clazzName, Object proxy) {
        return this.proxies.putIfAbsent(clazzName, proxy);
    }

    /**
     *
     * @param methodId
     * @param action
     * @return
     */
    public Method appendAction(String methodId, Method action) {
        return this.actions.putIfAbsent(methodId, action);
    }

    /**
     *
     * @param clazzName
     * @return
     */
    public Class<?> appendActionClazz(String clazzName, Class<?> clazz) {
        return this.actionsClazz.putIfAbsent(clazzName, clazz);
    }

    public Class<?> getActionClazz(String clazzName) {
        return this.actionsClazz.get(clazzName);
    }

    public Method getActions(String methodId) {
        return this.actions.get(methodId);
    }







    public Map<String, Object> getProxies() {
        return proxies;
    }

    public Map<String, Method> getActions() {
        return actions;
    }

    public Map<String, Class> getActionsClazz() {
        return actionsClazz;
    }
}
