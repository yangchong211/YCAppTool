package com.yc.common.reflect;

import com.yc.toolutils.system.AppLogUtils;

public class Student implements InterStudent{

    private String name = "杨充";
    private int age = 29;
    private static String tag = "Student";
    public int height = 172;

    public Student(){

    }

    public Student(String name){
        this.name = name;
    }

    private Student(Integer age) {
        this.age = age;
    }

    public Student(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    private void setAgeAndName(Integer age , String name){
        this.name = name;
        this.age = age;
    }

    public static void setMethod(){

    }

    public static String getMethod(){
        return "";
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", height=" + height +
                '}';
    }

    @Override
    public void play() {
        AppLogUtils.d("student play");
    }

    @Override
    public void teach() {
        AppLogUtils.d("student teach");
    }

}
