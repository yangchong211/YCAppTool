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
    - 这个时候，只能选择第一个方式。然后通过class.getConstructor获取有参数构造函数，然后在构造
    ``` java
    Constructor<?> constructor = stuClass.getConstructor(String.class,int.class);
    Object student = constructor.newInstance("张三",10);
    Method show = stuClass.getDeclaredMethod("show");
    show.invoke(student);
    ```


#### 1.3 Java反射的应用
- 1.逆向代码，例如反编译；2.与注解相结合的框架 例如Retrofit；3.单纯的反射机制应用框架 例如EventBus；4.动态生成类框架 例如Gson
- 需要访问隐藏属性或者调用方法改变程序原来的逻辑，这个在开发中很常见的，由于一些原因，系统并没有开放一些接口出来，这个时候利用反射是一个有效的解决方法
- 自定义注解，注解就是在运行时利用反射机制来获取的。
- 在开发中动态加载类，比如在Android中的动态加载解决65k问题等等，模块化和插件化都离不开反射，离开了反射寸步难行。



### 02.常见思路和做法
#### 2.1 提高反射的效率
- 缓存重复用到的对象
    - 利用缓存，在平时项目中用到多次的对象也会进行缓存，谁也不会多次去创建。尤其是在循环时，缓存好实例，就能提高反射的效率，减少耗时。
- setAccessible(true)
    - 当遇到私有变量和方法的时候，会用到setAccessible(true)方法关闭安全检查。这个安全检查其实也是耗时的。
    - 在反射的过程中可以尽量调用setAccessible(true)来关闭安全检查，无论是否是私有的，这样也能提高反射的效率。
- 其他一些优化措施
    - 如果调用次数可知可以关闭 Inflation 机制，以及增加内联缓存记录的类型数目。



#### 2.2 反射获取内部类


#### 2.3 setAccessible暴力访问
- 一般情况下并不能对类的私有字段进行操作，利用反射也不例外
    - 如果非要修改，则就需要调用AccessibleObject上的setAccessible()方法来允许这种访问。
    - 由于反射类中的Field，Method和Constructor继承自AccessibleObject，因此，通过在这些类上调用setAccessible(true)方法，可以实现对这些字段的操作。
 



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
- 反射调用对象的方法
    ``` java
    //方法1，反射修改 void setName(String name)
    MethodUtils.invokeMethod(s, "setAge",35);
    //方法2，反射修改 void setName(String name)
    Object[] args1 = {32};
    Class<?>[] parameterTypes1 = {Integer.class};
    MethodUtils.invokeMethod(s, "setAge",args1,parameterTypes1);
    
    //方法1，反射修改 void setAgeAndName(Integer age , String name)
    MethodUtils.invokeMethod(s, "setAgeAndName",40,"哈哈");
    //方法2，反射修改 void setAgeAndName(Integer age , String name)
    Object[] args2 = {33,"小样"};
    Class<?>[] parameterTypes2 = {Integer.class, String.class};
    MethodUtils.invokeMethod(s, "setAgeAndName", args2, parameterTypes2);
    ```



### 04.遇到的坑分析
#### 4.1 如何防止反射攻击
- 第一种：使用枚举单例。
- 第二种：类的修饰abstract，所以没法实例化，反射也无能为力。
- 第三种：将类设置成final不可变。



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


#### 4.3 反射调用私有
- 反射调用私有方法。注意点是参数类型避免传错，
    ```
    // 指定方法名称来获取对应的私有的Method实例
    Method setName = cl.getDeclaredMethod("setName", String.class);
    setName.setAccessible(true);
    setName.invoke(object, "潇湘剑雨");
    ```


#### 4.4 反射偶发失效
- 



#### 4.5 反射注意的要点
- 在利用反射的时候，编译器是无法校验语法的正确性的！如果getMethod()方法调用的参数输错了，那么只有在运行期才能发现。要知道的是，寻找运行期Bug的难度要远远超过编译期的Bug。
    ```
    Method m = student.getClass().getMethod("setName", String.class);
    m.invoke(obj, "张三");
    ```




### 05.其他问题说明
#### 5.1 常见问题记录
- Class.forName() 和ClassLoader.loadClass()区别？问到的是反射，但是在底层涉及到了虚拟机的类加载知识。
    - Class.forName() 默认执行类加载过程中的连接与初始化动作，一旦执行初始化动作，静态变量就会被初始化为程序员设置的值，如果有静态代码块，静态代码块也会被执行
    - ClassLoader.loadClass() 默认只执行类加载过程中的加载动作，后面的动作都不会执行。



#### 5.2 反射调用开销
- 反射效率低，大概有这些原因
    - Method#invoke 方法会对参数做封装和解封操作。
    - 需要检查方法可见性。
    - 需要校验参数。
    - 反射方法难以内联。
    - JIT 无法优化。
- Class.forName
    - 调用本地方法，耗时
- Class.getMethod
    - 遍历该类的共有方法，匹配不到，遍历父类共有方法，耗时，getMethod会返回得到结果的拷贝，应避免getMethods和getDeclardMethods方法，减少不必要堆空间消耗。
- Method.invoke
    - method.invoke(null, i);将invoke的参数改变时，查看其中字节码，发现多了新建Object数据和int类型装箱的指令。
    - Method.invoke是一个变长参数方法，字节码层面它的最后一个参数是object数组，所以编译器会在方法调用处生成一个数据传入；Object数组不能存储基本类型，所以会自动装箱
    - 这两者都会带来性能开销，也会占用堆内存，加重gc负担。但是实际上述例子并不会触发gc，因为原本的反射调用被内联，其创建的对象被虚拟机认为“不会逃逸”，此时会将其优化为栈上分配（非堆上分配），不会触发GC。
    - 需要检查方法可见性，反射时每次调用都必须检查方法的可见性（在 Method.invoke 里）
    - 需要校验参数，反射时也必须检查每个实际参数与形式参数的类型匹配性（在NativeMethodAccessorImpl.invoke0 里或者生成的 Java 版 MethodAccessor.invoke 里）；
    - 反射方法难以内联，Method#invoke 就像是个独木桥一样，各处的反射调用都要挤过去，在调用点上收集到的类型信息就会很乱，影响内联程序的判断，使得 Method.invoke() 自身难以被内联到调用方。



#### 5.3 反射和代理使用



#### 5.4 反射的弊端
- 反射机制是一种程序自我分析的能力。用于获取一个类的类变量，构造函数，方法，修饰符。
    - 优点：运行期类型的判断，动态类加载，动态代理使用反射。缺点：性能是一个问题，反射相当于一系列解释操作，通知jvm要做的事情，性能比直接的java代码要慢很多。
- 反射的弊端有哪些？
    - 反射包括了一些动态类型，所以JVM无法对这些代码进行优化。因此，反射操作的效率要比那些非反射操作低得多。要避免在经常被执行的代码或对性能要求很高的程序中使用反射。
    - 由于反射允许代码执行一些在正常情况下不被允许的操作（比如访问私有的属性和方法），使用反射可能会导致意料之外的副作用－－代码有功能上的错误，降低可移植性。反射代码破坏了抽象性，因此当平台发生改变的时候，代码的行为就有可能也随着变化。


#### 5.5 反射修改final属性
- 先说结果：final我们应该都知道，修饰变量的时候代表是一个常量，不可修改。那利用反射能不能达到修改的效果呢？还是不可修改
- 然后再说一个论证：对于正常的对象变量即使被final修饰也是可以通过反射进行修改的，只是我们无法通过这个对象获取到修改后的值，所以理解为不可修改……
- 涉及到JVM的内联优化
    - 内联函数，编译器将指定的函数体插入并取代每一处调用该函数的地方（上下文），从而节省了每次调用函数带来的额外时间开支。
    - 简单的说，就是JVM在处理代码的时候会帮我们优化代码逻辑，比如上述的final变量，已知final修饰后不会被修改，所以获取这个变量的时候就直接帮你在编译阶段就给赋值了。




#### 5.9 其他参考说明
- classLoader
    - https://github.com/Catherine22/ClassLoader

