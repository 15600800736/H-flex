
package com.frame.parameter;

import java.util.List;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class PassedParameter<T> 
	implements Parameter<T> {
	private Parameter<T> parameter;

	protected PassedParameter(Parameter<T> parameter) {
		this.parameter = parameter;
	}

	@Override
	public String getParameterName() {
		return this.parameter.getParameterName();
	}

	@Override
	public void setParameterName(String parameterName) {
		this.parameter.setParameterName(parameterName);
	}

	@Override
	public Class<T> getParameterClass() {
		return this.parameter.getParameterClass();
	}

	@Override
	public Boolean isArray() {
		return this.parameter.isArray();
	}

	@Override
	public void setValue(T value) {
		this.parameter.setValue(value);
	}

	@Override
	public T getValue() {
		return this.parameter.getValue();
	}

	@Override
	public Method getMethod() {
		return this.parameter.getMethod();
	}

	@Override
	public void setMethod(Method method) {
		this.parameter.setMethod(method);
	}

	@Override
	public PassedParameter<T> passByValue() {
		return new PassedParameter<>(copy());
	}

	@Override
	public PassedParameter<T> passByReference() {
		return this;
	}

	@Override
	public Parameter<T> copy() {
		return this.parameter.copy();
	}

	@Override
	public List<? extends Annotation> getAnnotations() {
		return this.parameter.getAnnotations();
	}

	@Override
	public void setIndex(Integer index) {
		this.parameter.setIndex(index);
	}

	@Override
	public Integer getIndex() {
		return this.parameter.getIndex();
	}

	@Override
	public void setAnnotations(List<Annotation> annotations) {
		this.parameter.setAnnotations(annotations);
		
	}
	
}