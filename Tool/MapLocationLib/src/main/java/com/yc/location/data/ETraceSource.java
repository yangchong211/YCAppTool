package com.yc.location.data;

public enum ETraceSource {
    didi("didi"),
    didiwifi("didi-wifi"),
    didicell("didi-cell"),
    gps("gps"),
    cache("cache"),
    tencent("tencent"),
    nlp("nlp"),
    googleflp("googleflp"),
    ios("ios"),
    err("err");

    private String mName;
    ETraceSource(String name) {
        mName = name;
    }

    @Override
    public String toString() {
        return mName;
    }
}
