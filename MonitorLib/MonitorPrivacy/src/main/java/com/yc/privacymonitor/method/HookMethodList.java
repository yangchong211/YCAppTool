package com.yc.privacymonitor.method;

import com.yc.privacymonitor.bean.ClassMethodBean;
import com.yc.privacymonitor.bean.MethodBean;

import java.util.LinkedList;

public interface HookMethodList {
    LinkedList<MethodBean> getMethodList();
    LinkedList<ClassMethodBean> getAbsMethodList();
}
