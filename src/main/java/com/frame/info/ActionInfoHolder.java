package com.frame.info;

import com.frame.context.MapperResource;
import com.frame.context.Resource;
import com.frame.exceptions.ScanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by fdh on 2017/7/17.
 */
public class ActionInfoHolder extends MapperResource {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private Map<String,ActionInfo> actionMapper = new HashMap<>();
    private Lock actionMapperLock = new ReentrantLock();
    public void addAction(String id, ActionInfo actionInfo) throws ScanException {
        actionMapperLock.lock();
        ActionInfo exitAction = actionMapper.get(id);
        if (exitAction == null) {
            actionMapper.put(id, actionInfo);
        } else {
            throw new ScanException("<action id='" + id + "'></action>", "id为" + id + "的action重复定义，请检查配置");
        }
        actionMapperLock.unlock();
    }

    @Override
    public Boolean extract(Resource resource) {
        return null;
    }

    @Override
    protected void initResourceOrder() {

    }

    @Override
    protected void initInformationRequired() {

    }
}
