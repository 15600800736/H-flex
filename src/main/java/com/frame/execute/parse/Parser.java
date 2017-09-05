
package com.frame.execute.parse;

import com.frame.context.ParserContext;
import com.frame.context.info.StringInformation.Configuration;
import com.frame.execute.Executor;


public abstract class Parser extends Executor<ParserContext, ParserContext> {
    protected Configuration configuration;

    public Parser(Configuration configuration) {
        this.configuration = configuration;
    }

    public Parser(ParserContext production, Configuration configuration) {
        super(production);
        this.configuration = configuration;
    }
}