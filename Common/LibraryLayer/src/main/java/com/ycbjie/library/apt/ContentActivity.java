package com.ycbjie.library.apt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

@SuppressLint("Registered")
public class ContentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注解解析
        //遍历所有的子类
        for (Class c = this.getClass(); c != Context.class; c = c.getSuperclass()) {
            assert c != null;
            //找到修饰了注解ContentView的类
            ContentView annotation = (ContentView) c.getAnnotation(ContentView.class);
            if (annotation != null) {
                try {
                    //获取ContentView的属性值
                    int value = annotation.value();
                    //调用setContentView方法设置view
                    this.setContentView(value);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
                return;
            }
        }
    }
}