package com.frame.annotations;

import java.lang.annotation.*;

/**
 * Created by fdh on 2017/8/28.
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ExecutionSet.class)
public @interface Execution {
    String actionAlias();
    String returnType() default "";
    Processor[] processors() default {};
}
