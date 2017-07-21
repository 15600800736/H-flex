package com.frame.info;

import com.frame.context.MapperResource;
import com.frame.context.Resource;

/**
 * Created by fdh on 2017/7/22.
 */
public class ActionGroupInfo extends MapperResource {
    @Override
    public <T extends Resource> Boolean split(T[] resources) {
        return null;
    }

    @Override
    public <T extends Resource> Integer join(T resource) {
        return null;
    }
}
