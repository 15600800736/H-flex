package com.frame.execute.scan;

import com.frame.context.resource.Resource;
import com.frame.enums.ConfigurationStringPool;
import com.frame.context.info.StringInfomation.Configuration;
import com.frame.context.info.Node;

/**
 * Created by fdh on 2017/7/26.
 */
public class TypeAliasScanner extends Scanner {
    public TypeAliasScanner(Configuration configuration) {
        super(configuration);
    }

    @Override
    public Boolean exec() throws Exception {
        Node root = configuration.getRoot();
        Node typeAlias = root == null ? null : root.getChild(ConfigurationStringPool.TYPE_ALIAS);
        if(typeAlias == null) {
            return false;

        }
        return true;
    }
}
