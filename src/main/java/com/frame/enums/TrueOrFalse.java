package com.frame.enums;

/**
 * Created by fdh on 2017/7/27.
 */
public enum TrueOrFalse {
    TRUE(true),FALSE(false);

    private Boolean bool;

    TrueOrFalse(Boolean bool) {
        this.bool = bool;
    }
}