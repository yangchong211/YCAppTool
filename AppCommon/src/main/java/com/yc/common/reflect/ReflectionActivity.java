package com.yc.common.reflect;

import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.yc.common.R;
import com.yc.library.base.mvp.BaseActivity;
import com.yc.reflectionlib.ConstructorUtils;
import com.yc.reflectionlib.FieldUtils;
import com.yc.reflectionlib.MethodUtils;
import com.yc.roundcorner.view.RoundTextView;
import com.yc.toolutils.AppLogUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;

public class ReflectionActivity extends BaseActivity {

    private RoundTextView tvReflect1;
    private RoundTextView tvReflect2;
    private RoundTextView tvReflect3;
    private RoundTextView tvReflect4;
    private RoundTextView tvReflect5;
    private RoundTextView tvReflect6;

    @Override
    public int getContentView() {
        return R.layout.activity_reflect_main;
    }

    @Override
    public void initView() {
        tvReflect1 = findViewById(R.id.tv_reflect_1);
        tvReflect2 = findViewById(R.id.tv_reflect_2);
        tvReflect3 = findViewById(R.id.tv_reflect_3);
        tvReflect4 = findViewById(R.id.tv_reflect_4);
        tvReflect5 = findViewById(R.id.tv_reflect_5);
        tvReflect6 = findViewById(R.id.tv_reflect_6);
    }

    @Override
    public void initListener() {
        tvReflect1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test1();
            }
        });
        tvReflect2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test2();
            }
        });
        tvReflect3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test3();
            }
        });
        tvReflect4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    test4();
                }
            }
        });
        tvReflect5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test5();
            }
        });
        tvReflect6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test6();
            }
        });
    }

    @Override
    public void initData() {

    }

    private void test1(){
        Class cl = Student.class;
        System.out.println("类名称:"+cl.getName());
        System.out.println("简单类名称:"+cl.getSimpleName());
        System.out.println("包名:"+cl.getPackage());
        System.out.println("是否为接口:"+cl.isInterface());
        System.out.println("是否为基本类型:"+cl.isPrimitive());
        System.out.println("是否为数组对象:"+cl.isArray());
        System.out.println("父类名称:"+cl.getSuperclass().getName());
        //System.out: 类名称:com.yc.common.reflect.Student
        //System.out: 简单类名称:Student
        //System.out: 包名:package com.yc.common.reflect, Unknown, version 0.0
        //System.out: 是否为接口:false
        //System.out: 是否为基本类型:false
        //System.out: 是否为数组对象:false
        //System.out: 父类名称:java.lang.Object
    }

    /**
     * 获取对象的变量，目前只有这四个方法
     */
    private void test2() {
        Student student = new Student();
        Class<? extends Student> cl = student.getClass();
        //获取class对象的public属性
        Field[] fields = FieldUtils.getFields(cl);
        for (int i=0 ; i<fields.length ; i++){
            Field met = fields[i];
            String name = met.getName();
            Annotation[] declaredAnnotations = met.getDeclaredAnnotations();
            AppLogUtils.i("获取class对象的public属性:"+name+"----"+declaredAnnotations.length);
        }
        //获取class对象的所有属性
        Field[] declaredFields = FieldUtils.getDeclaredFields(cl);
        for (int i=0 ; i<declaredFields.length ; i++){
            Field met = declaredFields[i];
            String name = met.getName();
            Annotation[] declaredAnnotations = met.getDeclaredAnnotations();
            AppLogUtils.i("获取class对象的所有属性:"+name+"----"+declaredAnnotations.length);
        }

        try {
            //获取class指定属性
            Field ageField = cl.getDeclaredField("age");
            //获取class指定的public属性
            Field heightField = cl.getField("height");
            AppLogUtils.d("class file ageField : " + ageField);
            AppLogUtils.d("class file heightField : " + heightField);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        //获取class指定的public属性
        Field height = FieldUtils.getField(cl, "height");
        //获取class指定属性
        Field age = FieldUtils.getDeclaredField(cl, "age");
        AppLogUtils.d("class file utils age : " + age);
        AppLogUtils.d("class file utils height : " + height);
    }

    /**
     * 获取class对象的方法
     */
    private void test3() {
        Student student = new Student();
        Class<? extends Student> cl = student.getClass();
        //Method[] methods = cl.getMethods();
        //获取class对象的所有public方法 包括父类的方法
        Method[] methods = MethodUtils.getMethods(cl);
        for (int i=0 ; i<methods.length ; i++){
            Method met = methods[i];
            String name = met.getName();
            Annotation[] declaredAnnotations = met.getDeclaredAnnotations();
            int modifiers = met.getModifiers();
            AppLogUtils.i("获取class对象的所有public方法，包括父类:"+name+"----"+declaredAnnotations.length);
        }
        //Method[] declaredMethods = cl.getDeclaredMethods();
        //获取class对象的所有声明方法
        Method[] declaredMethods = MethodUtils.getDeclaredMethods(cl);
        for (int i=0 ; i<declaredMethods.length ; i++){
            Method met = declaredMethods[i];
            String name = met.getName();
            Annotation[] declaredAnnotations = met.getDeclaredAnnotations();
            int modifiers = met.getModifiers();
            AppLogUtils.i("获取class对象的所有声明方法:"+name+"----"+declaredAnnotations.length);
        }

        try {
            //返回次Class对象对应类的、带指定形参列表的public方法
            Method getName = cl.getMethod("getName");
            //返回次Class对象对应类的、带指定形参列表的方法
            Method setName = cl.getDeclaredMethod("setName",String.class);
            AppLogUtils.d("class method getName : " + getName);
            AppLogUtils.d("class method setName : " + setName);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        try {
            //返回次Class对象对应类的、带指定形参列表的public方法
            Method getName = MethodUtils.getMethod(cl, "getName");
            //返回次Class对象对应类的、带指定形参列表的方法
            Method setName = MethodUtils.getDeclaredMethod(cl, "setName", String.class);
            AppLogUtils.d("class method utils getName : " + getName);
            AppLogUtils.d("class method utils setName : " + setName);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取class对象的构造函数
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void test4() {
        try {
            Class<?> cl = Class.forName("com.yc.common.reflect.Student");
            //获取class对象public构造函数
            //Constructor<?>[] constructors = cl.getConstructors();
            Constructor<?>[] constructors = ConstructorUtils.getConstructors(cl);
            Constructor<?> constructors1 = ConstructorUtils.getConstructor(cl, String.class);
            Constructor<?> constructors2 = ConstructorUtils.getConstructor(cl, String.class, Integer.class);
            //Constructor<?> constructors3 = ConstructorUtils.getConstructor(cl, Integer.class);
            for (int i=0 ; i<constructors.length ; i++){
                Constructor con = constructors[i];
                String name = con.getName();
                TypeVariable[] typeParameters = con.getTypeParameters();
                Annotation[] declaredAnnotations = con.getDeclaredAnnotations();
                AppLogUtils.i("获取class对象public构造函数:"+name+"----"+con.toString());
            }
            AppLogUtils.i("获取class对象public构造函数1:"+constructors1.toString());
            AppLogUtils.i("获取class对象public构造函数2:"+constructors2.toString());
            //AppLogUtils.i("获取class对象public构造函数3:"+constructors3.toString());

            //获取class对象的所有声明构造函数
            Constructor<?>[] declaredConstructors = ConstructorUtils.getDeclaredConstructors(cl);
            Constructor<?> declaredConstructor = ConstructorUtils.getDeclaredConstructor(cl, Integer.class);
            for (int i=0 ; i<declaredConstructors.length ; i++){
                AppLogUtils.i("获取class对象的所有声明构造函数:"+declaredConstructors[i].getName() + "  " + declaredConstructors[i].toString());
            }
            AppLogUtils.i("获取class对象的所有声明构造函数1:"+declaredConstructor.toString());
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


    private void test5() {
        //第一种方式 通过Class类的静态方法——forName()来实现
        try {
            Class<?> class1 = Class.forName("com.yc.common.reflect.Student");
            Student s = (Student) class1.newInstance();
            //首先需要获得与该方法对应的Method对象
            Method setAge = class1.getDeclaredMethod("setAge", Integer.class);
            //调用指定的函数并传递参数
            setAge.invoke(s, 28);
            String student1 = s.toString();
            AppLogUtils.i("反射调用类的方法1:"+student1);

            Method setAgeAndName = class1.getDeclaredMethod("setAgeAndName", Integer.class , String.class);
            setAgeAndName.setAccessible(true);
            setAgeAndName.invoke(s,30,"doubi");
            String student2 = s.toString();
            AppLogUtils.i("反射调用类的方法2:"+student2);

            Object[] args1 = {32};
            Class<?>[] parameterTypes1 = {Integer.class};
            MethodUtils.invokeMethod(s, "setAge",args1,parameterTypes1);
            AppLogUtils.i("反射调用类的方法3:"+s);
            Object[] args2 = {33,"小样"};
            Class<?>[] parameterTypes2 = {Integer.class, String.class};
            MethodUtils.invokeMethod(s, "setAgeAndName", args2, parameterTypes2);
            AppLogUtils.i("反射调用类的方法4:"+s);

            MethodUtils.invokeMethod(s, "setAge",35);
            AppLogUtils.i("反射调用类的方法5:"+s);
            MethodUtils.invokeMethod(s, "setAgeAndName",40,"哈哈");
            AppLogUtils.i("反射调用类的方法6:"+s);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void test6() {
        //第一种方式 通过Class类的静态方法——forName()来实现
        try {
            Class<?> class1 = Class.forName("com.yc.common.reflect.Student");
            Student obj = (Student) class1.newInstance();

            int age = obj.getAge();
            AppLogUtils.i("反射访问成员变量值1:"+age);
            //获取age成员变量
            //Field field = cl.getField("age");
            Field field = class1.getDeclaredField("age");
            //设置权限
            field.setAccessible(true);
            //将obj对象的age的值设置为10
            field.setInt(obj, 10);
            //获取obj对象的age的值
            int anInt = field.getInt(obj);
            AppLogUtils.i("反射访问成员变量值2:"+anInt);

            //反射修改私有变量
            // 获取声明的 code 字段，这里要注意 getField 和 getDeclaredField 的区别
            Field gradeField = class1.getDeclaredField("name");
            // 如果是 private 或者 package 权限的，一定要赋予其访问权限
            gradeField.setAccessible(true);
            // 修改 student 对象中的 Grade 字段值
            gradeField.set(obj, "逗比");
            Object o = gradeField.get(obj);
            AppLogUtils.i("反射访问成员变量值3:"+o.toString());


            Object name4 = FieldUtils.readField(obj, "name", true);
            AppLogUtils.i("反射访问成员变量值4:"+name4.toString());
            Object name5 = FieldUtils.readField(obj, "name");
            AppLogUtils.i("反射访问成员变量值5:"+name5.toString());

            FieldUtils.writeField(obj,"name","逗比2");
            Object name6 = FieldUtils.readField(obj, "name", true);
            AppLogUtils.i("反射访问成员变量值6:"+name6.toString());

            FieldUtils.writeField(obj,"name","傻逼",true);
            Object name7 = FieldUtils.readField(obj, "name", true);
            AppLogUtils.i("反射访问成员变量值7:"+name7.toString());

            FieldUtils.writeDeclaredField(obj,"name","傻逼2");
            Object name8 = FieldUtils.readField(obj, "name", true);
            AppLogUtils.i("反射访问成员变量值7:"+name8.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private void test(){
        //第一种方式 通过Class类的静态方法——forName()来实现
        try {
            Class<?> class1 = Class.forName("com.yc.common.reflect.Student");
            Student p= (Student) class1.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        //第二种方式 通过类的class属性
        Class<?> class2 = Student.class;
        Class<?> superclass = class2.getSuperclass();
        try {
            Student p= (Student) class2.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        //第三种方式 通过对象getClass方法
        Student student = new Student();
        Class<?> class3 = student.getClass();
    }
}
