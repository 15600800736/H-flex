package com.frame.parse.infoholder;

import com.frame.exceptions.ParseException;
import com.frame.parse.parser.ConfigurationParser;
import com.frame.parse.parser.Parser;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;

public class ConfigurationReader {
    private Document document;

    public ConfigurationReader(String path) throws ParseException {
        SAXReader reader = new SAXReader();
        try {
            this.document = reader.read(new File(path));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        if(document == null) {
            throw new ParseException("G:\\test.xml","无法打开配置文件");
        }
        Element root = document.getRootElement();
        XmlConfiguration xmlConfiguration = new XmlConfiguration(root);
        Parser parser = new ConfigurationParser(root, xmlConfiguration);
        try {
            parser.parse();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
