package com.frame.annotations;

import com.frame.enums.TrueOrFalse;
import com.frame.execute.process.DefaultProcessor;
import com.frame.execute.process.Processor;

import java.lang.annotation.*;

@Target(value = {ElementType.FIELD,ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
@Repeatable(value = Actions.class)
public @interface Action {
    String alias() default "";
    TrueOrFalse overload() default TrueOrFalse.FALSE;
    Class<? extends Processor>[] processors() default {};
}


