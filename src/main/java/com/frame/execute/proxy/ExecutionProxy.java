package com.frame.execute.proxy;

import com.frame.annotations.Execution;
import com.frame.context.info.StringInformation.ActionInfo;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private final Map<String, String> typeAlias;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public ExecutionProxy(Map<String, ActionInfo> actions,
                          Map<String, String> actionAliases,
                          Map<String, Method> actionCache,
                          Map<String, Class> actionClazz,
                          Map<String, String> actionClazzPath,
                          Map<String, String> typeAlias) {
        this.actions = actions;
        this.actionAliases = actionAliases;
        this.actionCache = actionCache;
        this.actionClazz = actionClazz;
        this.actionClazzPath = actionClazzPath;
        this.typeAlias = null;
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
        return getAppropriateMethod(executions, getter, field.getName());
    }

    private Method getAppropriateMethod(Execution[] executions, Method getter, String fieldName) {
        if (getter == null) {
            return null;
        }
        // get params from getter
        Class<?>[] parameters = getter.getParameterTypes();
        for (Execution execution : executions) {
            Method action = null;
            String actionAlias = execution.actionAlias();
            if (actionAlias == null) {
                return null;
            }
            String methodId = this.actionAliases.get(actionAlias);
            // first get from cache
            action = this.actionCache.get(methodId);
            // if there are no such actions in cache
            if (action == null) {
                // get info
                ActionInfo actionInfo = this.actions.get(methodId);
                String actionClazzName = actionInfo.getActionClass();
                // first get from cache
                Class<?> actionClazz = this.actionClazz.get(actionClazzName);
                // reflect the class
                if (actionClazz == null) {
                    // get the absolute path of class
                    String clazzPath = this.actionClazzPath.get(actionClazzName);
                    if (clazzPath != null) {
                        try {
                            actionClazz = Class.forName(clazzPath);
                        } catch (ClassNotFoundException e) {
                            return null;
                        }
                    }
                    if (actionClazz == null) {
                        return null;
                    }
                }
                String methodName = actionInfo.getName();
                String[] params = actionInfo.getParam();
                transferTypeAliasToRealType(params);
                Class<?>[] paramTypes = getClassesFromString(params);
                try {
                    // use parameters from getter to get the method.
                    action = actionClazz.getDeclaredMethod(methodName, paramTypes);
                    return action;
                } catch (NoSuchMethodException e) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Mismatch actions");
                    }
                }
            } else {

            }
        }
        return null;

    }

    private void transferTypeAliasToRealType(String[] typeAlias) {
        for (int i = 0; i < typeAlias.length; i++) {
            String realType = this.typeAlias.get(typeAlias[i]);
            if (realType != null) {
                typeAlias[i] = realType;
            }
        }
    }
    private Class<?>[] getClassesFromString(String[] pathes) {

        for (int i = 0; i < pathes.length; i++) {

        }

        return null;
    }
}
