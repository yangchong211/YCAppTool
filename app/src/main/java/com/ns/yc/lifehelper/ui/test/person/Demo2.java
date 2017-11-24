package com.ns.yc.lifehelper.ui.test.person;

import android.util.Log;


public class Demo2 extends PersonCloth{


    public Demo2(Person person) {
        super(person);
    }

    @Override
    public void dressed() {
        super.dressed();
        //可以许多操作
        dress1();
    }


    private void dress1() {
        Log.i("boy","穿了睡衣");
    }
}
