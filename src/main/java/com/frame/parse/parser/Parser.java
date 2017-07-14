
package com.frame.parse.parser;

import com.frame.exceptions.ParseException;
import com.frame.info.ActionInfoHolder;


@FunctionalInterface
public interface Parser {
    Object parse(ActionInfoHolder actionInfo) throws ParseException;
}