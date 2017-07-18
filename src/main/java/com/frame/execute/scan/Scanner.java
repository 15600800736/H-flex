package com.frame.execute.scan;

import com.frame.exceptions.ParseException;
import com.frame.execute.Executor;
import com.frame.info.Configuration;

/**
 * Created by fdh on 2017/6/17.
 */
public interface Scanner extends Executor{
    void scan(Configuration configuration) throws ParseException;
}
