package com.frame.annotations;

import com.frame.enums.EnumProcessorType;

/**
 * Created by fdh on 2017/8/31.
 */
public @interface Processor {
    EnumProcessorType type();

    String path();
}
