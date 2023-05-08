package com.yc.appgrpc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.protobuf.InvalidProtocolBufferException;
import com.yc.appgrpc.proto.JsonTest;
import com.yc.roundcorner.view.RoundTextView;
import com.yc.toastutils.ToastUtils;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity 效率测试：";
    private RoundTextView tvRpc1;
    private RoundTextView tvRpc2;
    private RoundTextView tvRpc3;
    private RoundTextView tvRpc4;
    private RoundTextView tvRpc5;
    private RoundTextView tvRpc6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rpc_main);
        tvRpc1 = findViewById(R.id.tv_rpc_1);
        tvRpc2 = findViewById(R.id.tv_rpc_2);
        tvRpc3 = findViewById(R.id.tv_rpc_3);
        tvRpc4 = findViewById(R.id.tv_rpc_4);
        tvRpc5 = findViewById(R.id.tv_rpc_5);
        tvRpc6 = findViewById(R.id.tv_rpc_6);
        tvRpc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelloWorldActivity.startActivity(MainActivity.this);
            }
        });
        tvRpc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WanAndroidActivity.startActivity(MainActivity.this);
            }
        });
        tvRpc3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouteGuideActivity.startActivity(MainActivity.this);
            }
        });
        tvRpc4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showRoundRectToast("效率测试");
                new JsonTest().gson();
                protoTest();
            }
        });
    }

    private void protoTest() {
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
        AddressBookProto.AddressBook addressBook = AddressBookProto.AddressBook.newBuilder()
                .addPeople(zs)
                .addPeople(ls).build();

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