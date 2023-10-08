package com.yc.testjnilib;

import android.util.Log;

public class HelloCallBack {

    String name = "HelloCallBack";

    void updateName(String name){
        this.name = name;
        Log.d("TestJni","你成功调用了HelloCallBack的方法：" + name);
    }

}
