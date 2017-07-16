package com.frame.info;

import com.frame.enums.EnumProcessorType;

/**
 * Created by fdh on 2017/7/16.
 */
public class ProcessorInfo {

    // 处理器类
    private String processorsClass;

    // 处理器类型
    private EnumProcessorType processorType;

    public String getProcessorsClass() {
        return processorsClass;
    }

    public void setProcessorsClass(String processorsClass) {
        this.processorsClass = processorsClass;
    }

    public EnumProcessorType getProcessorType() {
        return processorType;
    }

    public void setProcessorType(EnumProcessorType processorType) {
        this.processorType = processorType;
    }
}
