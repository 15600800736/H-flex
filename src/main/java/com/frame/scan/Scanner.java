package com.frame.scan;

import com.frame.exceptions.ParseException;
import com.frame.info.Configuration;

/**
 * Created by fdh on 2017/6/17.
 */
@FunctionalInterface
public interface Scanner {
    void scan(Configuration configuration) throws ParseException;
}
