# 反射封装库
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.其他问题说明



### 01.基础概念介绍
#### 1.1 反射基础原理
- 反射是为了能够动态的加载一个类。动态加载类，动态的调用一个方法，动态的访问一个属性等动态要求而设计的。用在运行阶段。
- 由于反射最终也必须有类参与，因此反射的组成一般有下面几个方面组成:
    - 1.java.lang.Class.java：类对象；
    - 2.java.lang.reflect.Constructor.java：类的构造器对象；
    - 3.java.lang.reflect.Method.java：类的方法对象；
    - 4.java.lang.reflect.Field.java：类的属性对象；
- 反射中类的加载过程
    - 根据虚拟机的工作原理，一般情况下，类需要经过：加载->验证->准备->解析->初始化->使用->卸载这个过程，如果需要反射的类没有在内存中，那么首先会经过加载这个过程，并在在内存中生成一个class对象，有了这个class对象的引用，就可以做自己想做的事情。



#### 1.2 获得Class对象方式
- 每个类被加载之后，系统就会为该类生成一个对应的Class对象。通过该Class对象就可以访问到JVM中的这个类。
    ``` java
    //第一种方式 通过Class类的静态方法——forName()来实现
    class1 = Class.forName("com.yc.common.reflect.Student");
    //第二种方式 通过类的class属性
    class1 = Student.class;
    //第三种方式 通过对象getClass方法
    Student student = new Student();
    Class<?> class1 = student.getClass();

    //最后都可以通过调用newInstance()方法让加载完的类在内存中创建对应的实例，并把实例赋值给s
    Student s = (Student) class1.newInstance();
    //获取Class父类对象
    Class<?> superclass = class1.getSuperclass();
    ```
- 没有无参构造如何实现反射？
    - 这个时候，只能选择第一个方式。然后通过class.getConstructor获取有参数构造函数，然后



#### 1.3 Class与.class文档
- Java 在真正需要某个类时才会加载对应的.class文档。而非在程序启动时就加载所有类，因为大部分时候只需要用到应用程序部分资源，有选择地加载可以节省系统资源。
- Class 类没有公开的构造函数，实例是由 JVM 自动产生，每个 .class 文档加载时， JVM 会自动生成对应的 Class 对象。



#### 1.4 Java反射的应用
- 1.逆向代码，例如反编译；2.与注解相结合的框架 例如Retrofit；3.单纯的反射机制应用框架 例如EventBus；4.动态生成类框架 例如Gson
- 需要访问隐藏属性或者调用方法改变程序原来的逻辑，这个在开发中很常见的，由于一些原因，系统并没有开放一些接口出来，这个时候利用反射是一个有效的解决方法
- 自定义注解，注解就是在运行时利用反射机制来获取的。
- 在开发中动态加载类，比如在Android中的动态加载解决65k问题等等，模块化和插件化都离不开反射，离开了反射寸步难行。



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



#### 4.2 生成类的实例对象
- 1.使用Class对象的newInstance()方法来创建该Class对象对应类的实例。这种方式要求该Class对象的对应类有默认构造器。
- 2.先使用Class对象获取指定的Constructor对象，再调用Constructor对象的newInstance()方法来创建该Class对象对应类的实例。通过这种方式可以选择使用指定的构造器来创建实例。
    ``` java
    //第一种方式 Class对象调用newInstance()方法生成
    Object obj = class1.newInstance();
    //第二种方式 对象获得对应的Constructor对象，再通过该Constructor对象的newInstance()方法生成
    Constructor<?> constructor = class1.getDeclaredConstructor(String.class);//获取指定声明构造函数
    obj = constructor.newInstance("hello");
    ```


### 05.其他问题说明
- Class.forName() 和ClassLoader.loadClass()区别？问到的是反射，但是在底层涉及到了虚拟机的类加载知识。
    - Class.forName() 默认执行类加载过程中的连接与初始化动作，一旦执行初始化动作，静态变量就会被初始化为程序员设置的值，如果有静态代码块，静态代码块也会被执行
    - ClassLoader.loadClass() 默认只执行类加载过程中的加载动作，后面的动作都不会执行。


- classLoader
    - https://github.com/Catherine22/ClassLoader





