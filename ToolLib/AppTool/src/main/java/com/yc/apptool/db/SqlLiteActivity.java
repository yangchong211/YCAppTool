package com.yc.apptool.db;

import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SqlLiteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout linearLayout = new LinearLayout(this);
        setContentView(linearLayout);
        initView();
    }

    public void initView() {
        Context context = this.getApplicationContext();
        Student student1 = new Student();
        student1.setName("yc");
        student1.setAge(1);
        student1.setGrade("1");
        student1.setSex("男");
        DBTestDao.getInstance(context).insert(student1);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        ArrayList<Student> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Student student2 = new Student();
            student2.setName("yc" + i);
            student2.setAge(1 + i);
            student2.setGrade("1" + i);
            student2.setSex("男" + i);
            list.add(student2);
        }
        DBTestDao.getInstance(context).insertList(list);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        DBTestDao.getInstance(context).get("yc3");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        DBTestDao.getInstance(context).getAllData();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Student student3 = new Student();
        student3.setName("逗比");
        student3.setAge(100);
        student3.setGrade("100");
        student3.setSex("男");
        DBTestDao.getInstance(context).update("yc100", student3);

        DBTestDao.getInstance(context).getAllData();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
