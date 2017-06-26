
package com.frame.parameter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

public interface Parameter<T> {
	String getParameterName();
	void setParameterName(String parameterName);
	Class<T> getParameterClass();
	Boolean isArray();
	void setValue(T value);
	T getValue();
	Method getMethod();
	void setMethod(Method method);
	PassedParameter<T> passByValue();
	PassedParameter<T> passByReference();
	List<? extends Annotation> getAnnotations();
	void setAnnotations(List<Annotation> annotations);
	Parameter<T> copy();
	void setIndex(Integer index);
	Integer getIndex();
}