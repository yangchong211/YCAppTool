package com.yc.appgrpc.proto;

public class PhoneNumber {
    enum PhoneType {
        MOBILE,
        HOME,
        WORK;
    }

    private String number;
    private PhoneType type;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public PhoneType getType() {
        return type;
    }

    public void setType(PhoneType type) {
        this.type = type;
    }
}
