package com.frame.scan;

import com.frame.exceptions.ScanException;
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
        initParseXml();
    }

    private void initParseXml() throws ScanException {
        Boolean isValid = isPathValid();
        if(!isValid) {
            throw new ScanException();
        }
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(new File(path));
        } catch (DocumentException e) {
            System.out.println(e);
            throw new ScanException();
        }
        Element root = document.getRootElement();
        Element actionClasses = root.element("action-classes");
        Element annotationScan = root.element("annotation-scan");
        if(annotationScan != null) {
            if(logger.isDebugEnabled()) {
                logger.debug("annotation scan is enabled");
            }

        } else {
            if(logger.isDebugEnabled()) {
                logger.debug("annotation scan is disabled");
            }
        }
    }

    private Boolean isPathValid() {
        Pattern pattern = Pattern.compile("^([A-Z]:\\\\)?[^\\\\/:*?\"<>|]+?(\\\\[^\\\\/:*?\"<>|]+?)*\\.xml");
        Matcher matcher = pattern.matcher(path);
        return matcher.matches();
    }

    private List<String> getClassPathListByScan(List<String> pathList) {
        return null;
    }

    public static void main(String...strings) {
        try {
            DefaultMethodRegister register = new DefaultMethodRegister("G:\\test.xml");
        } catch (ScanException e) {
            e.printStackTrace();
        }
    }


}
