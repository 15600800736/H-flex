package com.frame.execute.parse;

import com.frame.context.ParserContext;
import com.frame.context.info.StringInfomation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by fdh on 2017/9/3.
 */
public class ActionClassParser extends Parser {

    private String clazzName;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public ActionClassParser(Configuration configuration) {
        super(configuration);
    }

    public ActionClassParser(ParserContext production, Configuration configuration) {
        super(production, configuration);
    }

    public ActionClassParser(ParserContext production, Configuration configuration, String clazzName) {
        super(production, configuration);
        this.clazzName = clazzName;
    }

    @Override
    protected Object exec() throws Exception {
        if (clazzName == null) {
            return null;
        }
        Class<?> clazz = this.production.getActionClazz(clazzName);
        // if there is no class in cache.
        if (clazz == null) {
            clazz = initializeClazz(clazzName, configuration.getClassesPathMapper());
            if (clazz == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("initialize action class field");
                } else {
                    System.out.println("initialize action class field");
                }
                return false;
            }
        }
        Class<?> previousClazz = this.production.appendActionClazz(clazzName, clazz);
        if (previousClazz != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("other thread has append the action class " + clazzName + " we will use the class " + previousClazz.getName());
            } else {
                System.out.println("other thread has append the action class " + clazzName + " we will use the class " + previousClazz.getName());
            }
        }
        return true;
    }

    private Class<?> initializeClazz(String clazzName, Map<String, String> actionClasses) {
        if (clazzName == null || actionClasses == null) {
            return null;
        }
        String clazzPath = actionClasses.get(clazzName);
        if (clazzPath == null) {
            return null;
        }
        Class<?> clazz;
        try {
            clazz = Class.forName(clazzPath);
        } catch (ClassNotFoundException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Action named " + clazzName + " has wrong path. There is no such an class with path" + clazzPath);
            } else {
                System.out.println("Action named " + clazzName + " has wrong path. There is no such an class with path" + clazzPath);
            }
            return null;
        }
        return clazz;
    }

    public String getClazzName() {
        return clazzName;
    }

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }
}
