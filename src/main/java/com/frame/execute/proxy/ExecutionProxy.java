package com.frame.execute.proxy;

import com.frame.annotations.Execution;
import com.frame.context.info.StringInformation.ActionInfo;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by fdh on 2017/9/1.
 */
public class ExecutionProxy implements MethodInterceptor {

    private final String GETTER_PREFIX = "get";

    private final Map<String, ActionInfo> actions;

    private final Map<String, String> actionAliases;

    private final Map<String, Method> actionCache;

    private final Map<String, Class> actionClazz;

    private final Map<String, String> actionClazzPath;

    public ExecutionProxy(Map<String, ActionInfo> actions, Map<String, String> actionAliases, Map<String, Method> actionCache, Map<String, Class> actionClazz, Map<String, String> actionClazzPath) {
        this.actions = actions;
        this.actionAliases = actionAliases;
        this.actionCache = actionCache;
        this.actionClazz = actionClazz;
        this.actionClazzPath = actionClazzPath;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        if (method == null) {
            return null;
        }
        String methodName = method.getName();
        Field field = getField(methodName, method.getDeclaringClass());
        if (field == null) {
            return methodProxy.invokeSuper(o, objects);
        }
        Method action = getAction(field, method, actions);

        if (action == null) {

        }
        return null;
    }

    private Field getField(String methodName, Class<?> clazz) {
        if (!methodName.startsWith(GETTER_PREFIX)) {
            return null;
        }
        String fieldName = Character.toLowerCase(methodName.charAt(GETTER_PREFIX.length()))
                + methodName.substring(GETTER_PREFIX.length() + 1);
        Field field;
        try {
            field = clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return null;
        }
        if (!field.isAnnotationPresent(Execution.class)) {
            return null;
        }
        return field;

    }

    private Method getAction(Field field, Method getter, Map<String, ActionInfo> actions) {
        if (field == null || getter == null || actions == null) {
            return null;
        }
        Execution[] executions = field.getAnnotationsByType(Execution.class);
        Method action = getAppropriateMethod(executions, getter);
        return action;
    }

    private Method getAppropriateMethod(Execution[] executions, Method getter) {
        if (getter == null) {
            return null;
        }
        Class<?>[] parameters = getter.getParameterTypes();
        for (Execution execution : executions) {
            Method action = null;
            String actionAlias = execution.actionAlias();
            if (actionAlias == null) {
                return null;
            }
            String methodId = this.actionAliases.get(actionAlias);
            action = this.actionCache.get(methodId);
            if (action == null) {
                ActionInfo actionInfo = this.actions.get(methodId);
                String actionClazzName = actionInfo.getActionClass();
                Class<?> actionClazz = this.actionClazz.get(actionClazzName);
                if (actionClazz == null) {
                    String clazzPath = this.actionClazzPath.get(actionClazzName);
                    if (clazzPath == null) {
                        try {
                            actionClazz = Class.forName(clazzPath);
                        } catch (ClassNotFoundException e) {
                            return null;
                        }
                    }
                }


            }
        }
        return null;

    }

}
