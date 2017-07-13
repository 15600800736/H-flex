package com.frame.annotations;

import java.lang.annotation.*;

@Target(value = {ElementType.PARAMETER, ElementType.METHOD, ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Action {
    String alias() default "";
}


