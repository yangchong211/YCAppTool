package com.yc.appgrpc.proto;

import android.util.Log;

import com.google.gson.Gson;
import com.google.protobuf.InvalidProtocolBufferException;
import com.yc.appgrpc.AddressBookProto;
import com.yc.appgrpc.MsgProto;

public class JsonProtoTest {

    private static final String TAG = "Test 效率测试：";

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

        AddressBook.PhoneNumber p_110 = new AddressBook.PhoneNumber();
        p_110.setNumber("110");
        p_110.setType(AddressBook.PhoneNumber.PhoneType.HOME);
        AddressBook.Person zs = new AddressBook.Person();
        zs.setId(1);
        zs.setName("张三");
        zs.addPhones(p_110);
        addressBook.addPersons(zs);

        AddressBook.PhoneNumber p_120 = new AddressBook.PhoneNumber();
        p_120.setNumber("120");
        p_120.setType(AddressBook.PhoneNumber.PhoneType.MOBILE);
        AddressBook.Person ls = new AddressBook.Person();
        ls.setId(2);
        ls.setName("李四");
        ls.addPhones(p_120);

        AddressBook.PhoneNumber p_130 = new AddressBook.PhoneNumber();
        p_120.setNumber("130");
        p_120.setType(AddressBook.PhoneNumber.PhoneType.MOBILE);
        AddressBook.Person ys = new AddressBook.Person();
        ys.setId(3);
        ys.setName("王五");
        ys.addPhones(p_130);
        addressBook.addPersons(ys);

        return addressBook;
    }


    public void protoTest() {
        MsgProto.Msg msg = MsgProto.Msg.newBuilder().setText("杨充").build();
        AddressBookProto.Person.PhoneNumber.Builder builder
                = AddressBookProto.Person.PhoneNumber.newBuilder().setNumber("110")
                .setType(AddressBookProto.Person.PhoneType.HOME);
        AddressBookProto.Person.Builder zs = AddressBookProto.Person.newBuilder()
                .setName("张三")
                .setId(1)
                .addPhones(builder);
        AddressBookProto.Person.PhoneNumber.Builder builder1
                = AddressBookProto.Person.PhoneNumber.newBuilder().setNumber("120")
                .setType(AddressBookProto.Person.PhoneType.MOBILE);
        AddressBookProto.Person.Builder ls = AddressBookProto.Person.newBuilder()
                .setName("李四")
                .setId(2)
                .addPhones(builder1);
        AddressBookProto.Person.PhoneNumber.Builder builder2 = AddressBookProto.Person.PhoneNumber.newBuilder()
                .setNumber("130").setType(AddressBookProto.Person.PhoneType.MOBILE);
        AddressBookProto.Person.Builder ys = AddressBookProto.Person.newBuilder().setName("王五").setId(3).addPhones(builder2);

        AddressBookProto.AddressBook addressBook = AddressBookProto.AddressBook.newBuilder()
                .addPeople(zs)
                .addPeople(ls)
                .build();

        long l = System.currentTimeMillis();
        byte[] bytes = addressBook.toByteArray();
        Log.e(TAG, "protobuf 序列化耗时：" + (System.currentTimeMillis() - l));
        Log.e(TAG, "protobuf 序列化数据大小：" + bytes.length);
        try {
            l = System.currentTimeMillis();
            AddressBookProto.AddressBook.parseFrom(bytes);
            Log.e(TAG, "protobuf 反序列化耗时：" + (System.currentTimeMillis() - l));
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            Log.e(TAG, "protobuf 反序列化异常");
        }
    }

}
