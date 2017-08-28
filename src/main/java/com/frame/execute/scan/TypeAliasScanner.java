package com.frame.execute.scan;

import com.frame.context.resource.Resource;
import com.frame.enums.ConfigurationStringPool;
import com.frame.context.info.StringInfomation.Configuration;
import com.frame.context.info.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by fdh on 2017/7/26.
 */
public class TypeAliasScanner extends Scanner {
    public TypeAliasScanner(Configuration production) {
        super(production);
    }


    public final String seperator = ",";


    @Override
    public Boolean exec() throws Exception {
        Node root = production.getRoot();
        Node typeAlias = root == null ? null : root.getChild(ConfigurationStringPool.TYPE_ALIAS);
        if(typeAlias == null) {
            return false;
        }
        List<Node> types = typeAlias.getChildren(ConfigurationStringPool.TYPE);
        types.forEach(t -> {
            String name = t.getAttributeText(ConfigurationStringPool.NAME_ATTRIBUTE);
            String alias = t.getAttributeText(ConfigurationStringPool.ALIAS_ATTRIBUTE);
            alias = alias.trim();
            String[] aliases = alias.split(seperator);
            for (String s : aliases) {
                this.production.appendTypeAlias(s, name);
            }
        });
        return true;
    }
}
