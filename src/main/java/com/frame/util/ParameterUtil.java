
package com.frame.util;

import java.lang.reflect.Method;

import com.frame.parameter.Parameter;
import com.frame.parameter.ParameterFactory;
import com.frame.parameter.ParameterTransformer;

public  class ParameterUtil {
	public Parameter<Integer> stringToInt(Parameter<String> stringParam) {
		return ParameterFactory.createInteger(stringParam.getParameterName(), 
					Integer.parseInt(stringParam.getValue()),
					stringParam.getMethod(),
					stringParam.getIndex());
	}
	public Parameter<String> intToString(Parameter<Integer> intParam) {
		return ParameterFactory.createString(intParam.getParameterName(), 
					String.valueOf(intParam),
					intParam.getMethod(),
					intParam.getIndex());
	}
	public <NewType,OldType>NewType transform(OldType instance,ParameterTransformer<OldType, NewType> transformer) {
		return transformer.transform(instance);
	}
	public Parameter<?> transFromRefelct(java.lang.reflect.Parameter parameter,Integer index) {
		String paramName = parameter.getName();
		Class<?> clazz = parameter.getType(); 
		return ParameterFactory.createParameter(paramName, clazz,index);
	}
	
	public Parameter<?>[]  getParameters(Method method) {
		java.lang.reflect.Parameter[] parameters = method.getParameters();
		Class<?> clazzes = null;
		Parameter<?>[] myParameters = ParameterFactory.createArray(parameters.length);
		java.lang.reflect.Parameter[] parameter = method.getParameters();
		for(int i = 0;i < parameter.length;i++) {
			myParameters[i] = transFromRefelct(parameter[i],i);
		}
		return myParameters;
	}
}

