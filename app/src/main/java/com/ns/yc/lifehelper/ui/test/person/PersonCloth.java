package com.ns.yc.lifehelper.ui.test.person;

/**
 * Created by PC on 2017/11/22.
 * 作者：PC
 */

public abstract class PersonCloth extends Person {

    //保持Person的引用
    private Person mPerson;

    public PersonCloth(Person person){
        this.mPerson = person;
    }

    @Override
    public void dressed() {
        //调用Person类中dressed方法
        mPerson.dressed();
    }
}
