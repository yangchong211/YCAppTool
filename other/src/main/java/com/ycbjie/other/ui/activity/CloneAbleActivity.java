package com.ycbjie.other.ui.activity;

import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.other.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/11/2
 *     desc  : 深拷贝，浅拷贝
 *     revise: https://github.com/yangchong211/YCBanner
 * </pre>
 */
public class CloneAbleActivity extends BaseActivity {
    @Override
    public int getContentView() {
        return R.layout.activity_mixture_text;
    }

    @Override
    public void initView() {
        //test1();
        //test2();
        test3();
    }


    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    private void test1(){
        // 原始对象
        Student stud = new Student("杨充", "潇湘剑雨");
        System.out.println("原始对象: " + stud.getName() + " - " + stud.getSubj().getName());

        // 拷贝对象
        Student clonedStud = (Student) stud.clone();
        System.out.println("拷贝对象: " + clonedStud.getName() + " - " + clonedStud.getSubj().getName());

        // 原始对象和拷贝对象是否一样：
        System.out.println("原始对象和拷贝对象是否一样: " + (stud == clonedStud));
        // 原始对象和拷贝对象的name属性是否一样
        System.out.println("原始对象和拷贝对象的name属性是否一样: " + (stud.getName() == clonedStud.getName()));
        // 原始对象和拷贝对象的subj属性是否一样
        System.out.println("原始对象和拷贝对象的subj属性是否一样: " + (stud.getSubj() == clonedStud.getSubj()));

        stud.setName("小杨逗比");
        stud.getSubj().setName("潇湘剑雨大侠");
        System.out.println("更新后的原始对象: " + stud.getName() + " - " + stud.getSubj().getName());
        System.out.println("更新原始对象后的克隆对象: " + clonedStud.getName() + " - " + clonedStud.getSubj().getName());
    }


    private void test2(){

    }

    public class Subject {
        private String name;
        public Subject(String s) {
            name = s;
        }

        public String getName() {
            return name;
        }

        public void setName(String s) {
            name = s;
        }
    }

    public class Student implements Cloneable {
        /**
         * 对象引用
         */
        private Subject subj;
        private String name;

        public Student(String s, String sub) {
            name = s;
            subj = new Subject(sub);
        }

        public Subject getSubj() {
            return subj;
        }

        public String getName() {
            return name;
        }

        public void setName(String s) {
            name = s;
        }

        /**
         * 浅拷贝
         *  重写clone()方法
         */
        /*@Override
        public Object clone() {
            //浅拷贝
            try {
                // 直接调用父类的clone()方法
                return super.clone();
            } catch (CloneNotSupportedException e) {
                return null;
            }
        }*/

        /**
         * 深拷贝
         * 重写clone()方法
         *
         * @return
         */
        @Override
        public Object clone() {
            // 深拷贝，创建拷贝类的一个新对象，这样就和原始对象相互独立
            Student s = new Student(name, subj.getName());
            return s;
        }
    }

    private void test3() {
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try {
            // 创建原始的可序列化对象
            DouBi c1 = new DouBi(100, 100);
            System.out.println("原始的对象 = " + c1);
            DouBi c2 = null;
            // 通过序列化实现深拷贝
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            // 序列化以及传递这个对象
            oos.writeObject(c1);
            oos.flush();
            ByteArrayInputStream bin = new ByteArrayInputStream(bos.toByteArray());
            ois = new ObjectInputStream(bin);
            // 返回新的对象
            c2 = (DouBi) ois.readObject();
            // 校验内容是否相同
            System.out.println("复制后的对象   = " + c2);
            // 改变原始对象的内容
            c1.setX(200);
            c1.setY(200);
            // 查看每一个现在的内容
            System.out.println("查看原始的对象 = " + c1);
            System.out.println("查看复制的对象 = " + c2);
        } catch (IOException e) {
            System.out.println("Exception in main = " + e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public class DouBi implements Serializable {

        private int x;
        private int y;

        public DouBi(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        @Override
        public String toString() {
            return "x=" + x + ", y=" + y;
        }
    }

}
