
package com.frame.injector;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.frame.enums.exceptions.ExceptionType;
import com.frame.exceptions.InjectException;
import com.frame.annotations.Param;
import com.frame.proxy.Proxy;
import net.sf.cglib.proxy.Enhancer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultParameterInjector implements ParameterInjector {
    private Map<String, Object> offeredValue = new HashMap<>();
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public void inject(Object targetObject, Object sourceObject) throws InjectException {
        getFromSourceObjct(sourceObject);
        injectTargetObject(targetObject);
    }

    @Override
    public void inject(Object[] targetObjects, Object[] sourceObjects) throws InjectException {
        for (Object sourceObject : sourceObjects) {
            getFromSourceObjct(sourceObject);
        }
        for (Object targetObject : targetObjects) {
            injectTargetObject(targetObject);
        }
    }

    @Override
    public void inject(boolean multiSource, Object o1, Object... o2) throws InjectException {
        if (multiSource) {
            for (Object sourceObject : o2) {
                getFromSourceObjct(sourceObject);
                injectTargetObject(o1);
            }
        } else {
            getFromSourceObjct(o1);
            for (Object targetObject : o2) {
                injectTargetObject(targetObject);
            }
        }
    }

    public Map<String, Object> getOfferedValue() {
        return offeredValue;
    }


    private void getFromSourceObjct(Object sourceObject) throws InjectException {
        // 源对象的域
        Field[] sourceFields = sourceObject.getClass().getDeclaredFields();
        for (Field sourceField : sourceFields) {
            Param paramAnnotation = null;
            boolean needInject = false;
            Annotation[] annotations = sourceField.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof Param) {
                    paramAnnotation = (Param) annotation;
                    needInject = true;
                    break;
                }
            }
            String paramName = sourceField.getName();
            String getterName = "get" +
                    paramName.substring(0, 1).toUpperCase() + paramName.substring(1);
            Method getter = null;
            try {
                getter = sourceObject.getClass().getMethod(getterName, new Class[]{});
            } catch (NoSuchMethodException e) {
                throw new InjectException(ExceptionType.NoPublicGetter,
                        ExceptionType.NoPublicGetter.getName(), e,
                        sourceObject.getClass(), null);
            } catch (SecurityException e) {
                // how to do it?
            }
            Object value = null;
            if (needInject == true) {
                if (getter != null) {
                    try {
                        value = getter.invoke(sourceObject, null);
                        offeredValue.put(paramAnnotation.paramName(), value);
                    } catch (IllegalAccessException e) {
                        // TODO 自动生成的 catch 块
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        // TODO 自动生成的 catch 块
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        // TODO 自动生成的 catch 块
                        e.printStackTrace();
                    }
                }
            } else {
                if (getter != null) {
                    try {
                        value = getter.invoke(sourceObject, null);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();

                    }
                    getFromSourceObjct(value);
                }
            }
        }
    }

    private void injectTargetObject(Object targetObject) throws InjectException {
        // 目标对象的域
        Field[] targetFields = targetObject.getClass().getDeclaredFields();
        for (Field targetField : targetFields) {
            Param paramAnnotation = null;
            boolean needInject = false;
            Annotation[] annotations = targetField.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof Param) {
                    paramAnnotation = (Param) annotation;
                    needInject = true;
                    break;
                }
            }
            if (needInject == true) {
                String originalName = targetField.getName();
                String name = paramAnnotation.paramName();
                String setterName = new StringBuilder("set")
                        .append(originalName.substring(0, 1).toUpperCase() + originalName.substring(1)).toString();
                Method setter = null;
                Object value = offeredValue.get(name);
                if (value != null) {
                    try {
                        setter = targetObject.getClass().getMethod(setterName, paramAnnotation.paramType());
                        if (setter != null) {
                            setter.invoke(targetObject, paramAnnotation.paramType().cast(value));
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        throw new InjectException(ExceptionType.NoPublicSetter, ExceptionType.NoPublicSetter.getName(),
                                e, null, targetObject.getClass());
                    }
                }
            }
        }
    }
    public static void main(String... strings) {
        Proxy proxy = new Proxy();
        Enhancer enhancer = new Enhancer();
        enhancer.setCallback(proxy);
        enhancer.setSuperclass(DefaultParameterInjector.class);
        DefaultParameterInjector defaultParameterInjector = (DefaultParameterInjector) enhancer.create();
        defaultParameterInjector.test("a",1);
    }

    public void test(String a,Integer b) {
    }

}