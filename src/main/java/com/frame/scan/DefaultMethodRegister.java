package com.frame.scan;

import com.frame.exceptions.ScanException;
import com.frame.validor.ResourceValidor;
import com.frame.validor.Validor;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fdh on 2017/6/19.
 */

public class DefaultMethodRegister {

    //  资源路径
    private String path;
    //  日志
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public DefaultMethodRegister(String path) throws ScanException {
        this.path = path;
        initScanXml();
    }

    private void initScanXml() throws ScanException {

    }


}
