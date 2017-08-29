package com.frame.context.resource;

import com.frame.context.info.Information;
import com.frame.context.read.Reader;
import com.frame.exceptions.ScanException;

/**
 * Created by fdh on 2017/7/17.
 */

/**
 * Represents all of the structure that can give another class information
 * Resource can be merged into another resource, or split into multi resources
 *
 */
public interface Resource {
    Reader getReader() throws ScanException;
}
