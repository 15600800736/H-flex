package com.frame.enums.exceptions;

/**
 * Created by fdh on 2017/7/23.
 */
public enum ExtractorExceptionType {
    NOT_EXIST(1,"The information is not exist"),
    TYPE_ERROR(2,"The information's type isn't right");

    private Integer id;
    private String reason;

    ExtractorExceptionType(Integer id, String reason) {
        this.id = id;
        this.reason = reason;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
