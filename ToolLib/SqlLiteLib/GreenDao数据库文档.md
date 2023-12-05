# GreenDao数据库文档
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.原理和遇到的坑
- 05.数据库大比拼
- 06.其他问题说明



### 01.基础概念介绍
#### 1.1 GreenDao特点说明
- GreenDao特点
    - 易于使用的强大API，涵盖关系和连接；
    - 最小的内存消耗;
    - 小库大小（<100KB）以保持较低的构建时间并避免65k方法限制;
    - 数据库加密：greenDAO支持SQLCipher，以确保用户的数据安全;


#### 1.2 GreenDao的使用
- GreenDao的核心类有三个：分别是DaoMaster,DaoSession,XXXDao，这三个类都会自动创建，无需自己编写创建！
    - DaoMaster:：DaoMaster保存数据库对象（SQLiteDatabase）并管理特定模式的DAO类（而不是对象）。它有静态方法来创建表或删除它们。它的内部类OpenHelper和DevOpenHelper是SQLiteOpenHelper实现，它们在SQLite数据库中创建模式。
    - DaoSession：管理特定模式的所有可用DAO对象，您可以使用其中一个getter方法获取该对象。DaoSession还提供了一些通用的持久性方法，如实体的插入，加载，更新，刷新和删除。
    - XXXDao：数据访问对象（DAO）持久存在并查询实体。对于每个实体，greenDAO生成DAO。它具有比DaoSession更多的持久性方法，例如：count，loadAll和insertInTx。
    - Entities ：可持久化对象。通常, 实体对象代表一个数据库行使用标准 Java 属性(如一个POJO 或 JavaBean )。



#### 1.3 官方学习资料
- 官方学习文档资料
    - greenDao：https://greenrobot.org/greendao/
    - GitHub：https://github.com/greenrobot/greenDAO
    - 谷歌greenDao讨论：https://groups.google.com/g/greendao
    - greenDao加密数据：https://www.zetetic.net/sqlcipher/sqlcipher-for-android/
    - 学习文档：https://greenrobot.org/greendao/documentation/



### 02.常见思路和做法
#### 2.1 配置相关操作
- 第一步：导入插件。在项目build中导入：classpath 'org.greenrobot:greendao-gradle-plugin:3.2.2'
- 第二步：配置相关依赖。在module中添加依赖
    ```
    // 在module中添加依赖 build.gradle 文件中添加:
    apply plugin: 'org.greenrobot.greendao' // apply plugin
     
    dependencies {
        implementation 'org.greenrobot:greendao:3.2.2' // add library
    }
    ```
- 第三步：创建存储对象实体类。Gradle同步之后，会指定生成该实体类的set/get函数
    ```
    @Entity
    public class Student {
        @Unique
        int studentId;
        int age;
    }
    ```


#### 2.2 数据库初始化
- 在Application中维持一个全局的会话。我们在Applicaiton进行数据库的初始化操作：
    ```
    private void initGreenDao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, DB_NAME);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }
    ```


#### 2.3 如何插入数据
- 调用会话api插入数据，目前有两种，如下所示：
    ```
    //数据存在则替换，数据不存在则插入
    daoSession.insertOrReplace(palmEntity);
    //插入数据
    //daoSession.insert(palmEntity);
    ```
- 插入数据的一些常见API如下所示：
    ```
    //这是最简单的插入语句，新增一行数据，返回值为行号
    public long insert(T entity)
    
    //传递一个数组，新增多行数据
    public void insertInTx(T... entities)
    
    //传递一个集合，新增多行数据
    public void insertInTx(Iterable<T> entities)
    
    //传递一个集合，新增多行数据，setPrimaryKey：是否设置主键
    public void insertInTx(Iterable<T> entities, boolean setPrimaryKey)
    
    //将给定的实体插入数据库，若此实体类存在，则覆盖
    public long insertOrReplace(T entity)
    
    //使用事务操作，将给定的实体插入数据库，若此实体类存在，则覆盖
    public void insertOrReplaceInTx(T... entities)
    
    //使用事务操作，将给定的实体插入数据库，若此实体类存在，则覆盖
    public void insertOrReplaceInTx(Iterable<T> entities)
    
    //使用事务操作，将给定的实体插入数据库，若此实体类存在，则覆盖，并设置是否设定主键
    public void insertOrReplaceInTx(Iterable<T> entities, boolean setPrimaryKey)
    ```
- 关于insertOrReplace如何判断是否存在，核心原理是什么？



#### 2.4 如何查找数据
- 查询的方法有：
    - loadAll()：查询所有数据。
    - queryRaw()：根据条件查询。
    - queryBuilder() : 方便查询的创建，后面详细讲解。
- 如何根据条件进行数据查找
    ```
    List<PalmEntity> palmEntities = daoSession.queryRaw(PalmEntity.class, " where palm_id = ?", featureId);
    ```



#### 2.5 如何更新数据
- 通过update来进行修改：
    ```
    daoSession.update(palmEntity);
    ```


#### 2.6 如何删除数据
- 删除有两种方式：delete()和deleteAll()；分别表示删除单个和删除所有。
    ```
    daoSession.delete(palmEntity);
    daoSession.deleteAll(PalmEntity.class);
    ```



### 04.原理和遇到的坑
#### 4.1 自动生成代码
- Android中使用比较常见的注解处理器是APT，但是GreenDao使用的是JDT。
    - 发中常见的注解处理器有反射、APT、JDT等。反射在java开发中是比较常见的、apt是android开发经常使用的方式、JDT是eclipse开发工具使用的处理器。
    - GreenDao使用的代码生成模板为freemarker开源框架。





### 参考博客
- 博客：https://blog.csdn.net/ZhaiKun68/article/details/133472115
- https://www.jianshu.com/p/8f20fb1ee04a








