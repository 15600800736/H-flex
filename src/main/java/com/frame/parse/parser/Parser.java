
package com.frame.parse.parser;

import com.frame.exceptions.ParseException;


public interface Parser {
    Object parse() throws ParseException;
}