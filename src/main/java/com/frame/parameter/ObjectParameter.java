

package com.frame.parameter;

import java.util.List;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
public class ObjectParameter<T> 
	implements Parameter<T> {
	private String parameterName;
	private Class<T> parameterClass;
	private Boolean array;
	private T value;
	private Method method;
	private Integer index;
	private List<Annotation> annotations;
	
	protected ObjectParameter(String paramName,Class<T> clazz,Integer index) {
		this(paramName, null,null,clazz,index);
	}
	
	@SuppressWarnings("unchecked")
	protected ObjectParameter(String paramName, T value,Integer index) {
		this(paramName,value,null,(Class<T>)value.getClass(),index);
	}
	
	@SuppressWarnings("unchecked")
	protected ObjectParameter(String paramName, T value, Method method,Integer index) {
		this(paramName,value,method,(Class<T>)value.getClass(),index);
	}
	protected ObjectParameter(String paramName, Method method,Class<T> clazz,Integer index) {
		this(paramName,null,method,clazz,index);
	}
	
	protected ObjectParameter(String paramName, T value, Method method, Class<T> clazz,Integer index) {
		if(paramName == null) {
			throw new IllegalArgumentException("parameter's name can't be null");
		}
		if(clazz  == null) {
			throw new IllegalArgumentException("parameter's class can't be null");
		}
		this.parameterName = paramName;
		this.value = value;
		this.parameterClass = clazz;
		this.array = parameterClass.isArray();
		this.method= method;
		this.index = index;
	
	}
	@Override
	public String getParameterName() {
		return parameterName;
	}
	@Override
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	@Override
	public Class<T> getParameterClass() {
		return parameterClass;
	}
	@Override
	public Boolean isArray() {
		return array;
	}
	@Override
	public void setValue(T value) {
		this.value = value;
	}
	@Override
	public T getValue() {
		return value;
	}
	@Override
	public Method getMethod() {
		return method;
	}
	@Override
	public void setMethod(Method method) {
		this.method = method;
	}
	@Override
	public Parameter<T> copy() {
		return new ObjectParameter<>(parameterName,value,method,parameterClass,index);
	}
	
	@Override
	public PassedParameter<T> passByValue() {
		return new PassedParameter<>(copy());
	}
	
	@Override
	public PassedParameter<T> passByReference() {
		return new PassedParameter<>(this);
	}

	@Override
	public List<? extends Annotation> getAnnotations() {
		return annotations;
	}

	@Override
	public void setIndex(Integer index) {
		this.index = index;
	}

	@Override
	public Integer getIndex() {
		return this.index;
	}

	@Override
	public void setAnnotations(List<Annotation> annotations) {
		this.annotations =  annotations;
	}
	
	public <U extends Annotation>ObjectParameter<T> addAnnotations(U  annotation) {
		annotations.add(annotation);
		return this;
	}
}