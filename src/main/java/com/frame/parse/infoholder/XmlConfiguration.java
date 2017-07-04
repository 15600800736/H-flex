package com.frame.parse.infoholder;

import com.frame.exceptions.ParseException;
import org.dom4j.Element;

import java.util.List;

/**
 * Created by fdh on 2017/7/3.
 */
public class XmlConfiguration {
    // 根节点
    private Element root;
    // 是否开启了注解扫描
    private Boolean isAnnotationScan;
    // 扫描根目录列表
    private List<String> baseContents;
    // 待注册的类列表
    private List<Class<?>> actionClasses;

    public XmlConfiguration(Element root) {
        this.root = root;
    }

    public void setAnnotationScan(Boolean annotationScan) {
        isAnnotationScan = annotationScan;
    }

    public Boolean getAnnotationScan() {
        return isAnnotationScan;
    }

    public List<String> getBaseContents() {
        return baseContents;
    }

    public void addBaseContent(String baseContent) {
        if(baseContent != null) {
            baseContents.add(baseContent);
        }
    }

    public void addActionClass(String actionClassPath) throws ParseException {
        if(actionClassPath != null) {
            Class<?> actionClass = null;
            try {
                actionClass = Class.forName(actionClassPath);
            } catch (ClassNotFoundException e) {
                throw new ParseException("XXX.XXX.XXX","找不到" + actionClassPath + "类");
            }
            if(actionClass != null) {
                actionClasses.add(actionClass);
            }
        }
    }

    public List<Class<?>> getActionClasses() {
        return this.actionClasses;
    }
}
