package com.ns.yc.lifehelper.ui.test.person;

import android.util.Log;


public class Demo extends PersonCloth{


    public Demo(Person person) {
        super(person);
    }

    @Override
    public void dressed() {
        super.dressed();
        //可以许多操作
        dress1();
        dress2();
        dress3();
    }

    private void dress3() {
        Log.i("boy","穿了一件短袖");
    }

    private void dress2() {
        Log.i("boy","穿了一件西装");
    }

    private void dress1() {
        Log.i("boy","穿了一件裤子");
    }
}
