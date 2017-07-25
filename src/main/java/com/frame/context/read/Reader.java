package com.frame.context.read;

import com.frame.context.resource.Resource;
import com.frame.info.Node;

/**
 * Created by fdh on 2017/7/25.
 */
public interface Reader {
    void fromResource(Resource resource);
    Node getRoot();
}
