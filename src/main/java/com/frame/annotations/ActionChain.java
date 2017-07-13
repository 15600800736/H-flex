package com.frame.annotations;

import java.lang.annotation.*;

/**
 * Created by fdh on 2017/7/13.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(value = ActionChains.class)
public @interface ActionChain {
    String actionName();
}
