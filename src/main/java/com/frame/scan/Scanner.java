package com.frame.scan;

import org.dom4j.Element;

/**
 * Created by fdh on 2017/6/17.
 */
public interface Scanner<T> {
    T scan(Element node);
}
