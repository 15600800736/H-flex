package com.frame.scan;

import com.frame.exceptions.ParseException;
import com.frame.info.XmlConfiguration;

/**
 * Created by fdh on 2017/6/17.
 */
public interface Scanner {
    void scan(XmlConfiguration configuration) throws ParseException;
}
