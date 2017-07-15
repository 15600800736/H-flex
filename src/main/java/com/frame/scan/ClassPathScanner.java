package com.frame.scan;

import com.frame.exceptions.ParseException;
import com.frame.info.ConfigurationNode;
import com.frame.info.XmlConfiguration;
import com.frame.util.ConfigurationReader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fdh on 2017/7/13.
 */
public class ClassPathScanner implements Scanner {


    private final String ACTION_CLASS = "action-class";
    private final String PATH = "path";


    @Override
    public void scan(XmlConfiguration configuration) throws ParseException {
        List<String> classesPath = configuration.getClassesPath();
        List<String> methodsPath = new ArrayList<>(128);
        classesPath.forEach(cp ->{
            Class<?> methodClass = null;
            try {
                methodClass = Class.forName(cp);
            } catch (ClassNotFoundException e) {
                // how to do
            }
            if(methodClass != null) {

            }
        });
    }
}
