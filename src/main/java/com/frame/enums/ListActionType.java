package com.frame.enums;

/**
 * Created by fdh on 2017/8/1.
 */
public enum ListActionType {
    ;

    ListActionType(Integer index, String action) {
        this.index = index;
        this.action = action;
    }

    /**
     *
     */
    private Integer index;
    /**
     *
     */
    private String action;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
