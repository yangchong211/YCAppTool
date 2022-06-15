package com.yc.privacymonitor.method;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.LinkedList;
import java.util.Objects;

public final class ClassMethodGroup {
    //android.accounts.AbstractAccountAuthenticator$Transport
    private String targetClassName;
    //所有需要监测的风险函数
    private LinkedList<String> methodGroup = new LinkedList<>();

    public ClassMethodGroup(String className) {
        this.targetClassName = className;
    }

    public void addMethod(String name){
        methodGroup.add(name);
    }

    public String getTargetClassName() {
        return targetClassName;
    }

    public LinkedList<String> getMethodGroup() {
        return methodGroup;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClassMethodGroup that = (ClassMethodGroup) o;
        return Objects.equals(targetClassName, that.targetClassName);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(targetClassName);
    }
}

