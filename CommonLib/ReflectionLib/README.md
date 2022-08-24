# 反射封装库
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.其他问题说明



### 01.基础概念介绍
#### 1.1 反射基础原理
- 反射是为了能够动态的加载一个类。动态加载类，动态的调用一个方法，动态的访问一个属性等动态要求而设计的。
- 反射机制是在运行状态中。对于任意一个类，都能够知道这个类的所有属性和方法；对于任意一个对象，都能够调用它的任意一个方法和属性，这种动态获取的信息以及动态调用对象的方法的功能称为java语言的反射机制。



#### 1.2 反射基础元素
- 由于反射最终也必须有类参与，因此反射的组成一般有下面几个方面组成:
    - 1.java.lang.Class.java：类对象；
    - 2.java.lang.reflect.Constructor.java：类的构造器对象；
    - 3.java.lang.reflect.Method.java：类的方法对象；
    - 4.java.lang.reflect.Field.java：类的属性对象；
- 反射中类的加载过程
    - 根据虚拟机的工作原理，一般情况下，类需要经过：加载->验证->准备->解析->初始化->使用->卸载这个过程，如果需要反射的类没有在内存中，那么首先会经过加载这个过程，并在在内存中生成一个class对象，有了这个class对象的引用，就可以发挥开发者的想象力，做自己想做的事情了。


#### 1.3 Class与.class文档
- Java 在真正需要某个类时才会加载对应的.class文档。而非在程序启动时就加载所有类，因为大部分时候只需要用到应用程序部分资源，有选择地加载可以节省系统资源。
- Class 类没有公开的构造函数，实例是由 JVM 自动产生，每个 .class 文档加载时， JVM 会自动生成对应的 Class 对象。



### 02.常见思路和做法




### 03.Api调用说明
#### 3.2 获取class对象属性
- api调用如下所示，直接拿来用即可
    ``` java
    //获取class对象的public属性
    Field[] fields = FieldUtils.getFields(cl);
    //获取class对象的所有属性
    Field[] declaredFields = FieldUtils.getDeclaredFields(cl);
    //获取class指定的public属性
    Field height = FieldUtils.getField(cl, "height");
    //获取class指定属性
    Field age = FieldUtils.getDeclaredField(cl, "age");
    ```


#### 3.3 获取class对象的方法
- api调用如下所示，直接拿来用即可
    ``` java
    //获取class对象的所有public方法 包括父类的方法
    Method[] methods = MethodUtils.getMethods(cl);
    //获取class对象的所有声明方法
    Method[] declaredMethods = MethodUtils.getDeclaredMethods(cl);
    //返回次Class对象对应类的、带指定形参列表的public方法
    Method getName = MethodUtils.getMethod(cl, "getName");
    //返回次Class对象对应类的、带指定形参列表的方法
    Method setName = MethodUtils.getDeclaredMethod(cl, "setName", String.class);
    ```




### 04.遇到的坑分析
#### 4.1 如何防止反射攻击
- 第一种：使用枚举单例。
- 第二种：类的修饰abstract，所以没法实例化，反射也无能为力。
- 第三种：将类设置成final不可变


### 05.其他问题说明
- classLoader
    - https://github.com/Catherine22/ClassLoader





