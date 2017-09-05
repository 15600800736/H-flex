package com.frame.context.info.StringInformation;

import java.util.List;
import java.util.Set;

/**
 * Created by fdh on 2017/7/22.
 */
public class ExecutionInfo {

    public static class Execution {
        /**
         * <p>The alias of the method</p>
         */
        public String alias;

        /**
         *
         */
        public String returnType;

        /**
         * <p>The processors</p>
         */
        public List<ProcessorInfo> processors;

    }
    /**
     * <p></p>
     */
    public String actionClass;

    /**
     * <p>The field combined with the method</p>
     */
    public String fieldName;
    /**
     *
     */
    public Set<Execution> executions;

}
