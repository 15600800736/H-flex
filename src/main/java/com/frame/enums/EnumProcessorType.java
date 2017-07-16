package com.frame.enums;

/**
 * Created by fdh on 2017/7/16.
 */
public enum EnumProcessorType {
    PRE_PROCESSOR(1,"pre-processor"),
    POST_PROCESSOR(2,"post-processor");

    EnumProcessorType(Integer id, String type) {
        this.id = id;
        this.type = type;
    }

    private Integer id;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getId() {

        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
