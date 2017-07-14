package com.frame.annotations;

import com.frame.process.DefaultProcessor;
import com.frame.process.Processor;
import com.frame.transform.Transformer;

import java.lang.annotation.*;

@Target(value = {ElementType.FIELD,ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
@Repeatable(value = Actions.class)
public @interface Action {
    String alias() default "";
    Class<? extends Processor>[] processors() default DefaultProcessor.class;
}


