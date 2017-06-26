
package com.frame.parameter;

import java.lang.reflect.Method;
import java.util.Date;

public class ParameterFactory {
	public static <T> Parameter<T> createParameter(String paramName,Class<T> clazz,Integer index) {
		return new ObjectParameter<>(paramName, clazz,index);
	}
	public static<T> Parameter<T> createParameter(String paramName,T value,Integer index) {
		return new ObjectParameter<>(paramName, value,index);
	}
	public static<T> Parameter<T> createParameter(String paramName, T value, Method method,Integer index) {
		return new ObjectParameter<T>(paramName, value, method,index);
	}
	public static Parameter<Integer> createInteger(String paramName, Integer  value,Integer index) {
		return createParameter(paramName, value,index);
	}
	public static Parameter<Integer> createInteger(String paramName,Integer value,Method method,Integer index) {
		return createParameter(paramName, value,method,index);
	}
	public static Parameter<String> createString(String paramName, String  value,Integer index) {
		return createParameter(paramName, value,index);
	}
	public static Parameter<String> createString(String paramName,String value,Method method,Integer index) {
		return createParameter(paramName, value,method,index);
	}
	public static Parameter<Boolean> createBoolean(String paramName, Boolean  value,Integer index) {
		return createParameter(paramName, value, index);
	}
	public static Parameter<Boolean> createBoolean(String paramName,Boolean value,Method method,Integer index) {
		return createParameter(paramName, value,method,index);
	}
	public static Parameter<Double> createDouble(String paramName, Double  value,Integer index) {
		return createParameter(paramName, value,index);
	}
	public static Parameter<Double> createDouble(String paramName,Double value,Method method,Integer index) {
		return createParameter(paramName, value,method,index);
	}
	public static Parameter<Float> createFloat(String paramName, Float  value,Integer index) {
		return createParameter(paramName, value,index);
	}
	public static Parameter<Float> createFloat(String paramName,Float value,Method method,Integer index) {
		return createParameter(paramName, value,method,index);
	}
	public static Parameter<Character> createCharacter(String paramName, Character  value,Integer index) {
		return createParameter(paramName, value,index);
	}
	public static Parameter<Character> createCharacter(String paramName,Character value,Method method,Integer index) {
		return createParameter(paramName, value,method,index);
	}
	public static Parameter<Long> createLong(String paramName, Long  value,Integer index) {
		return createParameter(paramName, value,index);
	}
	public static Parameter<Long> createLong(String paramName, Long value,Method method,Integer index) {
		return createParameter(paramName, value,method,index);
	}
	public static Parameter<Short> createShort(String paramName, Short  value,Integer index) {
		return createParameter(paramName, value,index);
	}
	public static Parameter<Short> createShort(String paramName, Short value,Method method,Integer index) {
		return createParameter(paramName, value,method,index);
	}
	public static Parameter<Byte> createByte(String paramName, Byte  value,Integer index) {
		return createParameter(paramName, value,index);
	}
	public static Parameter<Byte> createByte(String paramName,Byte value,Method method,Integer index) {
		return createParameter(paramName, value,method,index);
	}
	public static Parameter<Date> createDate(String paramName, Date  value,Integer index) {
		return createParameter(paramName, value,index);
	}
	public static Parameter<Date> createDate(String paramName,Date value,Method method,Integer index) {
		return createParameter(paramName, value,method,index);
	}
	public static  Parameter<?>[] createArray(int length) {
		return new ObjectParameter<?>[length];
	}
	
}