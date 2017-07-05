
package com.frame.injector;

import com.frame.exceptions.InjectException;

public interface ParameterInjector {
	void inject(boolean multiSource,Object o1,Object...o2) throws InjectException;
	void inject(Object targetObject,Object sourceObject) throws InjectException;
	void inject(Object[] targetObjects,Object[] sourceObjects) throws InjectException;
}