package com.frame.mapper.method;

import java.util.List;

/**
 * Created by fdh on 2017/7/2.
 */
public class MethodPackageMapper {
    // 包名称
    private String packageName;
    // 子包
    private List<MethodPackageMapper> childPackage;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<MethodPackageMapper> getChildPackage() {

        return childPackage;
    }

    public void setChildPackage(List<MethodPackageMapper> childPackage) {
        this.childPackage = childPackage;
    }
}
