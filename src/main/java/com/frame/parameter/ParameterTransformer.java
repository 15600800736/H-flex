
package com.frame.parameter;

public interface ParameterTransformer<OldType,NewType> {
	NewType transform(OldType instance);
}