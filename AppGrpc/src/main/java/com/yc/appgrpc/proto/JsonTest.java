package com.yc.appgrpc.proto;

import android.util.Log;

import com.google.gson.Gson;

public class JsonTest  {

    private static final String TAG = "JsonTest 效率测试：";

    public void gson(){
        AddressBook addressBook = getObject();
        long l = System.currentTimeMillis();
        Gson gson = new Gson();
        String data = gson.toJson(addressBook);
        byte[] bytes = data.getBytes();
        Log.e(TAG, "Gson 序列化耗时：" + (System.currentTimeMillis() - l));
        Log.e(TAG, "Gson 序列化数据大小：" + bytes.length);
        l = System.currentTimeMillis();
        AddressBook addressBook1 = gson.fromJson(new String(bytes), AddressBook.class);
        Log.e(TAG, "Gson 反序列化耗时：" + (System.currentTimeMillis() - l));
    }
    
    private AddressBook getObject(){
        AddressBook addressBook = new AddressBook();
        PhoneNumber p_110 = new PhoneNumber();
        p_110.setNumber("110");
        p_110.setType(PhoneNumber.PhoneType.HOME);
        Person zs = new Person();
        zs.setId(1);
        zs.setName("张三");
        zs.addPhones(p_110);
        addressBook.addPersons(zs);

        PhoneNumber p_120 = new PhoneNumber();
        p_120.setNumber("120");
        p_120.setType(PhoneNumber.PhoneType.MOBILE);
        Person ls = new Person();
        ls.setId(2);
        ls.setName("李四");
        ls.addPhones(p_120);
        addressBook.addPersons(ls);
        return addressBook;
    }
}
