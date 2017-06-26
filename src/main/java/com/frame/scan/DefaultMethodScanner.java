package com.frame.scan;

import com.frame.exceptions.ScanException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fdh on 2017/6/19.
 */

public class DefaultMethodScanner {

    //  资源路径
    private String path;

    public DefaultMethodScanner(String path) throws ScanException {
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
            throw new ScanException();
        }
        Element root = document.getRootElement();
        Element actionClasses = root.element("action-classes");
        Element annotationScan = root.element("annotation-scan");
        List<String> classPathList = null;
        if(annotationScan == null) {
            classPathList = new LinkedList<>();
            Element actionClass = actionClasses.element("action-class");
            List<Element> pathList = actionClass.elements("path");
            for (Element path : pathList) {
                String classPath = path.getText();
                classPathList.add(classPath);
            }
        } else {
            Element baseContent = actionClasses.element("base-content");
            List<String> contentList = baseContent.elements("path");
            classPathList = getClassPathListByScan(contentList);

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
            DefaultMethodScanner scanner = new DefaultMethodScanner("test.xml");
        } catch (ScanException e) {
            e.printStackTrace();
        }
    }


}
