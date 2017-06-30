package com.frame.parse.parser;

import com.frame.annotations.Action;
import com.frame.exceptions.ParseException;
import com.frame.annotations.Param;
import com.frame.annotations.ParamObject;
import com.frame.mapper.method.MethodAliasMapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fdh on 2017/6/8.
 */
public class OverloadMethodParser implements Parser {

    MethodAliasMapper methodAliasMapper = MethodAliasMapper.getInstance();

    @Override
    public Object parse(Object... objects) throws ParseException {
        String source = (String) objects[0];
        String paramsName = (String) objects[1];
        Object[] args = Arrays.copyOfRange(objects, 3, objects.length);
        String[] params = paramsName.split(",");
        Map<String, Pair<Class, Object>> offeredValue = new HashMap<>();
        for (int i = 0; i < params.length; i++) {
            Class argClazz = args[i].getClass();
            if ("%Object".equals(params[i])) {
                getParametersValue(argClazz, args[i], offeredValue);
            } else {
                offeredValue.put(params[i], new Pair<>(argClazz, args[i]));
            }
        }

        Method targetMethod = methodAliasMapper.getMethod(source);
        // 无可奈何时，解析methodName为class与methodName
        String[] cells = source.split("\\.");
        Class<?> methodClass = getClazzBySource(cells);
        if (targetMethod == null) {

        }

        return null;
    }

    class Pair<T1, T2> {
        private T1 first;
        private T2 second;

        public Pair() {
        }

        public Pair(T1 first, T2 second) {
            this.first = first;
            this.second = second;
        }

        public T1 getFirst() {
            return first;
        }

        public void setFirst(T1 first) {
            this.first = first;
        }

        public T2 getSecond() {
            return second;
        }

        public void setSecond(T2 second) {
            this.second = second;
        }
    }

    private void getParametersValue(Class<?> argClazz, Object object, Map<String, Pair<Class, Object>> offeredValue) throws ParseException {
        Field[] fieldsOfArg = argClazz.getDeclaredFields();
        for (Field field : fieldsOfArg) {
            String originalName = field.getName();
            String getterName = "get" + originalName.substring(0, 1).toUpperCase() + originalName.substring(1);
            Method getter = null;
            try {
                getter = argClazz.getMethod(getterName);
            } catch (NoSuchMethodException e) {
                throw new ParseException("getXXXX()", argClazz.getName() + "类缺少" + originalName + "域的getter方法");
            }
            Object value = null;
            if (getter != null) {
                try {
                    value = getter.invoke(object);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            if (value != null) {
                if (field.isAnnotationPresent(Param.class)) {
                    Param param = field.getAnnotation(Param.class);
                    Class<?> paramType = param.paramType();
                    String paramName = param.paramName();
                    offeredValue.put(paramName, new Pair<>(paramType, value));
                } else if (field.isAnnotationPresent(ParamObject.class)) {
                    getParametersValue(value.getClass(), value, offeredValue);
                }
            }
        }
    }


    public static void main(String... strings) {
        Class<?> clazz = null;

        try {
            clazz = Class.forName("com.frame.parse.parser.OverloadMethodParser");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (clazz != null) {
            Method method = null;
            try {
                method = clazz.getMethod("test", String.class, Integer.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            Annotation[][] annotations = method.getParameterAnnotations();
            for (Annotation[] annotations1 : annotations) {
                for (Annotation annotation : annotations1) {
                    System.out.println(annotation.annotationType().getName());
                }
                System.out.println("______________________");
            }
        }
    }



    private Class<?> getClazzBySource(String[] cells) {
        StringBuilder clazzPath = new StringBuilder(cells[0]);
        if (cells.length > 1) {
            for (int i = 1; i < cells.length - 1; i++) {
                clazzPath.append(".").append(cells[i]);
            }
        }
        Class<?> methodClazz = null;
        try {
            methodClazz = Class.forName(clazzPath.toString());
        } catch (ClassNotFoundException e) {
            return null;
        }
        return null;
    }
}

