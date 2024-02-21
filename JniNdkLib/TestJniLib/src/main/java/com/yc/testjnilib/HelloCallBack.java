package com.yc.testjnilib;

import android.util.Log;

public class HelloCallBack {

    String name = "HelloCallBack";

    void updateName(String name){
        this.name = name;
        Log.d("TestJni","你成功调用了HelloCallBack的有参数方法：" + name);
    }

    void testCall(){
        Log.d("TestJni","你成功调用了HelloCallBack的无参数方法：");
    }


}
