package com.frame.util;

import com.frame.exceptions.ScanException;
import com.frame.info.Configuration;
import com.frame.info.ConfigurationNode;
import com.frame.info.Node;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.File;

public class ConfigurationReader {
    private Document document;

    public ConfigurationReader(String path) throws ScanException {
        init(path);
    }

    public void init(String path) throws ScanException {
        SAXReader reader = new SAXReader();
        try {
            this.document = reader.read(new File(path));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        if(document == null) {
            throw new ScanException("G:\\test.xml","无法打开配置文件");
        }
    }

    public Node getRoot() {
        return new ConfigurationNode(document.getRootElement());
    }
    public Configuration createConfiguration() {
        Configuration configuration = new Configuration();
        configuration.setRoot(getRoot());
        // todo
        configuration.setAnnotationScan(true);
        return configuration;
    }
}
