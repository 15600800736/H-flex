
package com.frame.execute.parse.parser;

import com.frame.exceptions.ParseException;
import com.frame.execute.Executor;
import com.frame.info.ActionInfoHolder;
import com.frame.info.Configuration;


public interface Parser extends Executor {
    Object parse(ActionInfoHolder actionInfo) throws ParseException;
    Configuration getConfiguration();
}