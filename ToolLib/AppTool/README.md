# 数据库开发文档说明
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.原理和遇到的坑
- 05.数据库大比拼
- 06.其他问题说明



### 01.基础概念介绍
#### 1.1 SQLite的特性
- 零配置数据库
    - 不需要在系统中配置。是一个进程内的库，实现了自给自足的、无服务器的、零配置的、事务性的 SQL 数据库引擎。
- 超级轻量化
    - SQLite不需要额外的配置以及轻量化是最大的优点。SQLite是直接访问其存储文件。


#### 1.2 SQLite适用场景
- 使用场景说明
    - 嵌入式设备：它可以在资源有限的设备上运行，不需要独立的服务器。
    - 测试和原型开发：SQLite可以快速创建和测试应用程序的原型，因为它不需要安装或配置服务器。
- Android中在什么场景使用
    - 1.如果数据量太大，SharedPreferences 不够用，可以考虑使用数据库，处理关系比较复杂。
    - 2.对于结构化的数据，一定要使用数据库，虽然会显得比较麻烦，但是后续的使用中会获益无穷。


#### 1.3 数据库核心类
- SQLiteDatabase数据库管理类
    - SQLiteDatabase是SQLite的数据库管理类，开发者可以在活动页面代码或任何能取到Context的地方获取数据库实例。
- SQLiteOpenHelper邦助器
    - 直接通过SQLiteDatabase进行操作数据库非常不方便，必须小心不能重复地打开数据库，处理数据库的升级也很不方便。
    - 因此Android提供了一个辅助工具—— SQLiteOpenHelper，我们可以通过SQLiteOpenHelper这个数据库帮助器来安全方便地打开、升级数据库。


#### 1.4 SQLiteDatabase使用
- 管理类，用于数据库层面的操作。
    - openDatabase：打开指定路径的数据库。
    - isOpen：判断数据库是否已打开。
    - close：关闭数据库。
    - getVersion：获取数据库的版本号。
    - setVersion：设置数据库的版本号。
- 事务类，用于事务层面的操作。
    - beginTransaction：开始事务。
    - setTransactionSuccessful：设置事务的成功标志。
    - endTransaction：结束事务。执行本方法时，系统会判断是否已执行setTransactionSuccessful，如果之前已设置就提交，如果没有设置就回滚。
- 数据处理类，用于数据表层面的操作。
    - execSQL：执行拼接好的SQL控制语句。一般用于建表、删表、变更表结构。
    - delete：删除符合条件的记录。
    - update：更新符合条件的记录。
    - insert：插入一条记录。
    - query：执行查询操作，返回结果集的游标。
    - rawQuery：执行拼接好的SQL查询语句，返回结果集的游标。


#### 1.5 ContentValues介绍
- SQLite直接使用的数据结构是ContentValues类
    - 类似于映射Map，提供put和get方法用来存取键值对。区别之处在于ContentValues的键只能是字符串”。
    - ContentValues主要用于记录增加和更新操作，即SQLiteDatabase的insert和update方法。
- 对于查询操作来说，使用的是另一个游标类Cursor
    - 调用SQLiteDatabase的query和rawQuery方法时，返回的都是Cursor对象，因此获取查询结果要根据游标的指示一条一条遍历结果集合。Cursor的常用方法：
- 1.游标控制类方法，用于指定游标的状态。
    - close：关闭游标。
    - isClosed：判断游标是否关闭。
    - isFirst：判断游标是否在开头。
    - isLast：判断游标是否在末尾。
- 2.游标移动类方法，把游标移动到指定位置。
    - moveToFirst：移动游标到开头。
    - moveToLast：移动游标到末尾。
    - moveToNext：移动游标到下一条记录。
    - moveToPrevious：移动游标到上一条记录。
    - move：往后移动游标若干条记录。
    - moveToPosition：移动游标到指定位置的记录。
- 3.获取记录类方法，可获取记录的数量、类型以及取值。
    - getCount：获取结果记录的数量。
    - getInt：获取指定字段的整型值。
    - getFloat：获取指定字段的浮点数值。
    - getString：获取指定字段的字符串值。
    - getType：获取指定字段的字段类型。



#### 1.6 SQLite数据类型
- 一般数据采用的固定的静态数据类型，而SQLite采用的是动态数据类型，会根据存入值自动判断。SQLite具有以下五种常用的数据类型：
    - NULL: 这个值为空值
    - VARCHAR(n)：长度不固定且其最大长度为 n 的字串，n不能超过 4000。
    - CHAR(n)：长度固定为n的字串，n不能超过 254。
    - INTEGER: 值被标识为整数,依据值的大小可以依次被存储为1,2,3,4,5,6,7,8.
    - REAL: 所有值都是浮动的数值,被存储为8字节的IEEE浮动标记序号.
    - TEXT: 值为文本字符串,使用数据库编码存储(TUTF-8, UTF-16BE or UTF-16-LE).
    - BLOB: 值是BLOB数据块，以输入的数据格式进行存储。如何输入就如何存储,不改 变格式。
    - DATA ：包含了 年份、月份、日期。
    - TIME：包含了 小时、分钟、秒。



#### 1.7 数据库SQL增删改查
- 查找
    - select column1,column2 from table_name
    - 或者：select * from table_name
- 插入
    - insert into table_name(column1,column2) values (value1,value2)
- 更新，更新已有表的数据：
    - update table_name set column1 = value1, column2 = value2 where +条件语句
- 向表中插入列：
    - alter table table_name add column column_name text(varchar(10)、char等)
- 删除
    - delete from table_name where +条件语句
- 注：sql语句不区分大小写







### 02.常见思路和做法
#### 2.1 数据库创建
- SQLiteOpenHelper数据库帮助器
    - 新建一个继承自SQLiteOpenHelper的数据库操作类，提示重写onCreate和onUpgrade两个方法。
    - onCreate方法只在第一次打开数据库时执行，在此可进行表结构创建的操作；
    - onUpgrade方法在数据库版本升高时执行，因此可以在onUpgrade函数内部根据新旧版本号进行表结构变更处理
    - 其中onCreate()是在数据库首次创建的时候调用，onUpgrade()是在数据库版本号DB_VERSION升级的时候才会调用
- 如何创建数据库
    - 继承SQLiteOpenHelper该类的构造方法中，会创建数据库。
- 如何打开数据库
    - 打开数据库连接：SQLite有锁机制，即读锁和写锁的处理；故而数据库连接也分两种。
    - 读连接可调用SQLiteOpenHelper的getReadableDatabase方法获得，写连接可调用getWritableDatabase获得。
- 如何关闭数据库
    - 关闭数据库连接：数据库操作完毕后，应当调用SQLiteDatabase对象的close方法关闭连接。



#### 2.2 数据表建立
- 一般在数据库首次调用的时候创建表
    - execSQL：执行拼接好的SQL控制语句。一般用于建表、删表、变更表结构。
    - db.execSQL(sql)，这个sql是数据表字符串
- 创建数据表的语法是什么
    - create table tab_name (id integer primary key autoincrement, name text , age integer)
- 那么代码是怎么创建数据表
    ```
    /**
     * 创建表结构
     */
    public static final String SQL_CREATE_TABLE = "create table " + TABLE_NAME + "(" +
            ID + " integer primary key autoincrement," +
            NAME + " text," +
            AGE + " integer," +
            SEX + " varchar(5)," +
            GRADE + " text" +
            ")";
    
    //继承SQLiteOpenHelper的onCreate中实现
    @Override
    public void onCreate(SQLiteDatabase db) {
        //初始化数据库
        db.execSQL(SQL_CREATE_TABLE);
    }
    ```




#### 2.3 如何插入数据
- 向表中插入数据，我们需要一行插入进表，这样数据写入更有效率。
- 使用SQLite的命令insert进行操作
    - table：代表想插入的数据的表明
    - nullColumnHack: 强行指定插入null值得数据列的列名。一般用不到，传入null即可。
    - contentValues对象：类似于Map，提供了一系列的put方法重载，其中key为数据列的列名。
    ```
    db.insert("user", null, values);
    ```
- 具体到代码的案例如下所示
    ```
    public void insert(Student student) {
        SQLiteDatabase db = dbHelper.getWriteDb();
        try {
            ContentValues cv = new ContentValues();
            cv.put(NAME, student.getName());
            cv.put(AGE, student.getAge());
            cv.put(SEX, student.getSex());
            cv.put(GRADE, student.getGrade());
            db.insert(TABLE_NAME, null, cv);
            AppLogUtils.d("insert插入数据到数据库 : " + cv);
        } finally {
            db.close();
        }
    }
    ```


#### 2.4 如何查找数据
- 使用SQLite的命令query进行操作
    - table:执行查询数据的表名。
    - columns：要查询出来的列名。相当于select 语句 select关键字后面的部分。
    - whereClause:查询条件子句，相当于 select 语句where 关键字后面的部分，在条件子句中允许使用占位符“?”。
    - selectionArgs:用于为 whereClause子句中的占位符传入参数值，值在数组中的位置与占位符在语句中的位置必须一致；否则就会有异常
    - groupBy:用于控制分组。相当于select 语句group by 关键字后面的部分
    - having: 用于对分组进行过滤。相当于 select 语句 having关键字后面的部分
    - orderBy:用于对记录进行排序。
    ```
    cursor = db.query(TABLE_NAME, null, KEY + "=?",
                    new String[]{key}, null, null, null);
    ```
- 具体到代码的案例如下所示



#### 2.5 如何更新数据
- 使用SQLite的命令update进行操作
    - table:代表想更新数据的表名
    - values:代表想更新数据的表名
    - whereClause:满足该whereClause子句的记录将会被更新
    - whereArgs:用于为whereClause子句传入参数
    ```
    db.update(TABLE_NAME, cv, NAME + "=?", new String[]{name});
    ```


#### 2.6 如何删除数据
- 使用SQLite的命令delete进行操作
    - table:代表想删除数据的表名。
    - whereClause:满足该whereClause子句的记录将会被删除。
    - whereArgs:用于为 whereClause 子句传入参数。
    ```
    db.delete(TABLE_NAME, name + "=?", new String[]{name});
    ```


#### 2.7 获取所有数据
- 使用SQLite的命令rawQuery进行操作
    ```
    String querySql = "select * from " + TABLE_NAME;
    cursor = db.rawQuery(querySql, null);
    ```


#### 2.8 如何修改表结构


### 03.Api调用说明




### 04.原理和遇到的坑
#### 4.1 多次打开数据库
- 解决办法
    - 获取单例对象：确保App运行时数据库只被打开一次，避免重复打开引起错误。


#### 4.2 理解SQLite索引
- 什么是SQLite索引
    - 简单地说，索引是一个指向表中数据的指针。一个数据库中的索引与一本书的索引目录是非常相识的。
    - 优点：大大加快了数据库检索的速度，包括对单表查询、连表查询、分组查询、排序查询。经常是一到两个数量级的性能提升，且随着数据数量级增长。
    - 缺点：索引的创建和维护存在消耗，索引会占用物理空间，且随着数据量的增加而增加。在对数据库进行增删改时需要维护索引，所以会对增删改的性能存在影响。
- 索引分类
    - 直接创建索引：使用 sql 语句创建，Android 中可以在 SQLiteOpenHelper 的 onCreate 或是 onUpgrade 中直接 execSQL 创建语句。
    ```
    create index id_index on user(id)
    ```
    - 唯一性索引：保证在索引列中的全部数据是唯一的，对聚簇索引和非聚簇索引都可以使用
    ```
    create unique index id_index on user(id);
    ```
- 单个或者多个索引
    - 单个索引：索引建立语句中仅包含单个字段，如上面的普通索引和唯一性索引创建示例
    - 复合索引：又叫组合索引，在索引建立语句中同时包含多个字段





### 05.其他问题说明
#### 5.1 为何用到事务
- 为什么需要是事务
    - 使用SQLiteDatabase的beginTransaction()方法可以开启一个事务，程序执行到endTransaction() 方法时会检查事务的标志是否为成功；
    - 如果程序执行到endTransaction()之前调用了setTransactionSuccessful() 方法设置事务的标志为成功则提交事务；
    - 如果没有调用setTransactionSuccessful() 方法则回滚事务。
- 事务处理应用
    - 很多时候我们需要批量的向Sqlite中插入大量数据时，单独的使用添加方法导致应用响应缓慢。
    - 因为sqlite插入数据的时候默认一条语句就是一个事务，有多少条数据就有多少次磁盘操作。
    - 如初始1000条记录也就是要1000次读写磁盘操作。同时也是为了保证数据的一致性，避免出现数据缺失等情况。
- 使用事务注意点
    - 千万不要忘记提交事务db.endTransaction();否则线程会有阻塞危险。事务只能用在一个线程中，不可跨线程。


#### 5.2 Sql优化实践



# 博客
- 数据库调试
    - https://github.com/amitshekhariitbhu/Android-Debug-Database
- Android端SQLCipher的攻与防新编
    - https://github.com/sqlcipher/android-database-sqlcipher
    - http://www.taodudu.cc/news/show-4201618.html?action=onClick
- 经典博客
    - https://zhuanlan.zhihu.com/p/561640485


