package com.frame.util;

import com.frame.exceptions.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by fdh on 2017/6/19.
 */

public class DefaultMethodRegister {

    //  资源路径
    private String path;
    //  日志
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public DefaultMethodRegister(String path) throws ParseException {
        this.path = path;
        initScanXml();
    }

    private void initScanXml() throws ParseException {

    }


}
