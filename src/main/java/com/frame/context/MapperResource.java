package com.frame.context;

import com.frame.exceptions.CastException;
import com.frame.exceptions.ParseException;
import com.frame.info.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by fdh on 2017/7/21.
 */
public abstract class MapperResource
        implements Resource {
    protected Map<String, Object> infoMapper = new HashMap<>();
    protected Map<String, Class<?>> classMapper = new HashMap<>();
    protected Lock mapperLock = new ReentrantLock();
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Object getInformation(String name) {
        return infoMapper.get(name);
    }

    @Override
    public <T> T getInformation(String name, Class<T> infoClass) {
        Object value = infoMapper.get(name);
        if (value == null) {
            return null;
        }
        Class<?> storedClass = classMapper.get(name);
        // if the class given equals the class registered, cast the information's type
        Boolean canCast = storedClass != null && (storedClass == infoClass || infoClass.isAssignableFrom(storedClass));
        if (canCast) {
            return cast(value, infoClass);
        } else {
            throw new CastException(infoClass, storedClass);
        }
    }

    protected <T> T cast(Object value, Class<T> infoClass) {
        return infoClass.cast(value);
    }

    @Override
    public void setInformation(String name, Object value) {
        mapperLock.lock();
        Object existValue = infoMapper.get(name);
        if (existValue != null) {
            if (logger.isWarnEnabled()) {
                logger.warn("the info named " + name + " is replaced");
            }

            // clear class
            Class<?> existClass = classMapper.get(name);
            if(existClass != null) {
                classMapper.put(name, null);
            }
        }
        infoMapper.put(name, value);
        mapperLock.unlock();
    }

    @Override
    public <T> void setInformation(String name, Object value, Class<T> infoClass) {
        mapperLock.lock();
        Object existValue = infoMapper.get(name);
        if (existValue != null) {
            if (logger.isWarnEnabled()) {
                logger.warn("the info named " + name + " is replaced");
            }
        }
        infoMapper.put(name,value);
        classMapper.put(name, infoClass);
        mapperLock.unlock();
    }

    @Override
    public Boolean hasInformation(String name) {
        return infoMapper.get(name) != null;
    }
}
