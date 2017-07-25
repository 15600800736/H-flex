package com.frame.execute.extract;

import com.frame.context.resource.Resource;
import com.frame.execute.Executor;

/**
 * Created by fdh on 2017/7/22.
 */

/**
 * Extractor is a executor that can extract information which it requires
 * from given resource, extract successfully or not can be seen in the value returned.
 */
public interface Extractor extends Executor{
    Boolean extract(Resource resource);
}
