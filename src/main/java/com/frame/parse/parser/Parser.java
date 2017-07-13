
package com.frame.parse.parser;

import com.frame.exceptions.ParseException;
import com.frame.info.ActionInfoHolder;


public interface Parser {
    Object parse(ActionInfoHolder actionInfo) throws ParseException;
}