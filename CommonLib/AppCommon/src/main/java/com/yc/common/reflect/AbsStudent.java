package com.yc.common.reflect;

public abstract class AbsStudent {

    private String name = "杨充";
    private int age = 29;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public abstract void teach();

}
