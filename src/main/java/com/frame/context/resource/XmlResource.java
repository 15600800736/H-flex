package com.frame.context.resource;

import com.frame.context.read.ConfigurationReader;
import com.frame.context.read.Reader;
import com.frame.exceptions.ScanException;

/**
 * Created by fdh on 2017/8/29.
 */
public class XmlResource implements Resource {

    private String xmlPath;

    public XmlResource(String xmlPath) {
        this.xmlPath = xmlPath;
    }

    @Override
    public Reader getReader() throws ScanException {
        return new ConfigurationReader(xmlPath);
    }
}
