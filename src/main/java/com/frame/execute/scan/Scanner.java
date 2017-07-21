package com.frame.execute.scan;

import com.frame.exceptions.ParseException;
import com.frame.execute.Executor;
import com.frame.info.Configuration;

/**
 * Created by fdh on 2017/6/17.
 */

/**
 * Scanner is the entrance for the frame, It will scan the input resource and inject the information that we need into
 * a Configuration that contains all of the information for the frame.
 * Scanner extends the interface Executor which allows you to do some pre-work or post-work like prepare the environment
 * or close the resource before and after you process the real work.
 * @see Configuration
 * @author Haug
 */
public interface Scanner extends Executor{
    void scan(Configuration configuration) throws ParseException;
}
