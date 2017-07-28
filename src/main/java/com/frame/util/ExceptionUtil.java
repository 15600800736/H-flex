package com.frame.util;

import com.sun.xml.internal.ws.api.model.CheckedException;

/**
 * Created by fdh on 2017/7/16.
 */
public class ExceptionUtil {

    public static void doThrow(Exception e) {
        ExceptionUtil.doThrowWith(e);
    }
    private static <E extends Exception> void doThrowWith(Exception e) throws E {
        throw (E)e;
    }

}
