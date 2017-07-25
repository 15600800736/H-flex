package com.frame.context.resource;

import com.frame.enums.exceptions.ExtractorExceptionType;
import com.frame.exceptions.CastException;
import com.frame.exceptions.ExtractorException;
import com.frame.execute.extract.Extractor;
import com.frame.info.Information;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by fdh on 2017/7/21.
 */


/**
 * <p>MapperResource is a implementation of {@link Resource} with data structure map,
 * It also implements {@link Extractor} which means it can extract information required
 * by itself.</p>
 * <p>The map resource specify the concrete resource and order into witch it can be split with
 * method {@code initResourceOrder}, and the sub-classes should be override this method to make sure your
 * own version.</p>
 * <p>Also, the {@code initInformationRequired} will define what information is required when extract from others</p>
 */
public abstract class MapperResource
        implements Resource,Extractor {
    // map the information to its name: name -> information
    protected Map<String, Information> infoMapper = new HashMap<>(256);
    // setter lock
    protected final Lock mapperLock = new ReentrantLock();
    // ensure the sub-resource's order
    protected final Map<Class<?>,Integer> resourcesOrder = new HashMap<>(16);
    // declaration the name of information required
    protected final Map<String, Class<?>> informationRequired = new HashMap<>(32);
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * the method is used for init specific sub-resource order
     */
    protected abstract void initResourceOrder();

    /**
     * the method is used for init what information is required
     */
    protected abstract void initInformationRequired();
    @Override
    public Information getInformation(String name) {
        return infoMapper.get(name);
    }

    @Override
    public <T> T getInformation(String name, Class<T> infoClass) {
        Information information = infoMapper.get(name);
        if (information == null || information.getValue() == null) {
            return null;
        }
        Class<?> storedClass = information.getValueClass();
        // if the class given equals the class registered, cast the information's type
        Boolean canCast = storedClass != null && (storedClass == infoClass || infoClass.isAssignableFrom(storedClass));
        if (canCast) {
            return cast(information.getValue(), infoClass);
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
        Information information = infoMapper.get(name);
        if (information != null) {
            if (logger.isWarnEnabled()) {
                logger.warn("the info named " + name + " is replaced");
            }
            // clear class
            information.setValueClass(null);
            // set value
            information.setValue(value);
        } else  {
            infoMapper.put(name, Information.createInformation(name, value));
        }

        mapperLock.unlock();
    }

    @Override
    public <T> void setInformation(String name, Object value, Class<T> infoClass) {
        mapperLock.lock();
        Information information = infoMapper.get(name);
        if (information != null) {
            if (logger.isWarnEnabled()) {
                logger.warn("the info named " + name + " is replaced");
            }
            information.setValue(value);
            information.setValueClass(infoClass);
        } else {
            infoMapper.put(name, Information.createInformation(name,value,infoClass));
        }
        mapperLock.unlock();
    }

    @Override
    public Boolean hasInformation(String name) {
        return infoMapper.get(name) != null;
    }

    @Override
    public String getType() {
        return this.getClass().getTypeName();
    }

    @Override
    public <T extends Resource> Boolean split(T[] resources) {
        return false;
    }

    @Override
    public <T extends Resource> Integer join(T resource) {
        return 0;
    }

    @Override
    public Resource[] transformResourcesToSpecificOrder(Resource... resources) {
        return resources;
    }

    @Override
    public Boolean extract(Resource resource) {
        informationRequired.entrySet().forEach(ir -> {
            String infoName = ir.getKey();
            Class<?> infoClass = ir.getValue();
            Information information = resource.getInformation(infoName);
            if(information == null) {
                throw new ExtractorException(infoName, resource.getClass(), ExtractorExceptionType.NOT_EXIST);
            }
            if(infoClass == null) {
                infoMapper.put(infoName, information);
            } else  {
                if(infoClass == information.getValueClass()){
                    infoMapper.put(infoName,information);
                } else {
                    throw new ExtractorException(infoName, resource.getClass(), ExtractorExceptionType.TYPE_ERROR);
                }
            }
        });
        return true;
    }

    @Override
    public void prepareForExecute() {

    }

    @Override
    public void postProcessForExccute() {

    }


}
