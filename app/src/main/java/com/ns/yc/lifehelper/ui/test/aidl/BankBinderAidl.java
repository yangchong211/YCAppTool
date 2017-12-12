package com.ns.yc.lifehelper.ui.test.aidl;

import com.ns.yc.lifehelper.api.IBankAIDL;

import java.util.UUID;


public class BankBinderAidl extends IBankAIDL.Stub {

    @Override
    public String OpenAccount(String name, String password) {
        return name+"开户成功"+ UUID.randomUUID().toString();
    }

    @Override
    public String saveMoney(int money, String account) {
        return "账户:"+account+"存入"+money+"人民币";
    }

    @Override
    public String tackMoney(int money, String account, String password) {
        return "账户:"+account+"取出"+money+"人民币";
    }

    @Override
    public String closeAccount(String account, String password) {
        return "账户:"+account+"销户";
    }

}
