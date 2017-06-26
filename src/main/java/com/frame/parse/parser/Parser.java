
package com.frame.parse.parser;

import com.frame.exceptions.ParseException;


public interface Parser {
    Object parse(Object... objects) throws ParseException;
}