# File文件工具库
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.其他问题说明


### 01.基础概念介绍
#### 1.1 IO流基础概念
- IO流概述：IO流用来处理设备之间的数据传输，Java对数据的操作是通过流的方式。
- 按照数据流向分为两类：输入流(读入数据)；输出流(写出数据)。输入时是流从数据源流向程序，输出时是流从程序传向数据源，而数据源可以是内存，文件，网络或程序等。
- 流所处理的数据类型分为两类：字节流：用于处理字节数据。（InputStream，OutputStream）；字符流：用于处理字符数据，Unicode字符数据。（Reader，Writer）



#### 1.2 字节流&字符流
- 字节流和字符流的区别：  
    - 读写单位不同：字节流以字节（8bit）为单位，字符流以字符为单位，根据码表映射字符，一次可能读多个字节。  
    - 处理对象不同：字节流能处理所有类型的数据（如图片、avi等），而字符流只能处理字符类型的数据，比如文本内容。



#### 1.3 节点流和处理流
- 可以从/向一个特定的IO设备（如磁盘、网络）读/写数据的流，称为节点流，节点流也被成为低级流。处理流是对一个已存在的流进行连接或封装，通过封装后的流来实现数据读/写功能，处理流也被称为高级流。
    ``` java
    //节点流，直接传入的参数是IO设备
    FileInputStream fis = new FileInputStream("test.txt");
    //处理流，直接传入的参数是流对象
    BufferedInputStream bis = new BufferedInputStream(fis);
    ```

#### 1.4 输出流和输入流
- 输出流：从内存读出到文件。只能进行写操作。
- 输入流：从文件读入到内存。只能进行读操作。
- 注意：这里的出和入，都是相对于系统内存而言的。


#### 1.5 File概念说明
- File类是java.io包下代表与平台无关的文件和目录，也就是说，如果希望在程序中操作**文件和目录**，都可以通过File类来完成。
- 创建与删除方法
    ``` java
    boolean createNewFile();//如果文件存在返回false，否则返回true并且创建文件 
    boolean mkdir();        //创建一个File对象所对应的目录，成功返回true，否则false。且File对象必须为路径而不是文件。只会创建最后一级目录，如果上级目录不存在就抛异常。
    boolean mkdirs();       //创建一个File对象所对应的目录，成功返回true，否则false。且File对象必须为路径而不是文件。创建多级目录，创建路径中所有不存在的目录
    boolean delete();       //如果文件存在返回true并且删除文件，否则返回false
    void deleteOnExit();    //在虚拟机终止时，删除File对象所表示的文件或目录。
    ```
- File判断方法
    ``` java
    boolean canExecute();   //判断文件是否可执行
    boolean canRead();      //判断文件是否可读
    boolean canWrite();     //判断文件是否可写
    boolean exists();       //判断文件是否存在
    boolean isDirectory();  //判断是否是目录
    boolean isFile();       //判断是否是文件
    boolean isHidden();     //判断是否是隐藏文件或隐藏目录
    boolean isAbsolute();   //判断是否是绝对路径 文件不存在也能判断
    ```
- File获取方法
    ``` java
    String getName();           //返回文件或者是目录的名称
    String getPath();           //返回路径
    String getAbsolutePath();   //返回绝对路径
    String getParent();         //返回父目录，如果没有父目录则返回null
    long lastModified();        //返回最后一次修改的时间
    long length();              //返回文件的长度
    File[] listRoots();         // 列出所有的根目录（Window中就是所有系统的盘符）
    String[] list() ;           //返回一个字符串数组，给定路径下的文件或目录名称字符串
    String[] list(FilenameFilter filter);       //返回满足过滤器要求的一个字符串数组
    File[]  listFiles();                        //返回一个文件对象数组，给定路径下文件或目录
    File[] listFiles(FilenameFilter filter);    //返回满足过滤器要求的一个文件对象数组
    ```


### 02.常见思路和做法
##### 2.1 字节流读写
- 字节流的抽象基类：InputStream ，OutputStream。
- 字节流：以字节为单位，每次次读入或读出是8位数据。可以读任何类型数据。
- InputStream 是所有的输入字节流的父类，它是一个抽象类，主要包含三个方法：
    ``` java
    //读取一个字节并以整数的形式返回(0~255),如果返回-1已到输入流的末尾。 
    int read() ； 
    //读取一系列字节并存储到一个数组buffer，返回实际读取的字节数，如果读取前已到输入流的末尾返回-1。 
    int read(byte[] buffer) ； 
    //读取length个字节并存储到一个字节数组buffer，从off位置开始存,最多len， 返回实际读取的字节数，如果读取前以到输入流的末尾返回-1。 
    int read(byte[] buffer, int off, int len) ；
    ```
- OutputStream 是所有的输出字节流的父类，它是一个抽象类，主要包含如下四个方法：
    ``` java
    //向输出流中写入一个字节数据,该字节数据为参数b的低8位。 
    void write(int b) ; 
    //将一个字节类型的数组中的数据写入输出流。 
    void write(byte[] b); 
    //将一个字节类型的数组中的从指定位置（off）开始的,len个字节写入到输出流。 
    void write(byte[] b, int off, int len); 
    //将输出流中缓冲的数据全部写出到目的地。 
    void flush();
    ```



#### 2.2 字符流读写
- 字符流的抽象基类：Reader ， Writer。
- 字符流：以字符为单位，每次次读入或读出是16位数据。其只能读取字符类型数据。
- Reader 是所有的输入字符流的父类，它是一个抽象类，主要包含三个方法：
    ``` java
    //读取一个字符并以整数的形式返回(0~255),如果返回-1已到输入流的末尾。 
    int read() ； 
    //读取一系列字符并存储到一个数组buffer，返回实际读取的字符数，如果读取前已到输入流的末尾返回-1。 
    int read(char[] cbuf) ； 
    //读取length个字符,并存储到一个数组buffer，从off位置开始存,最多读取len，返回实际读取的字符数，如果读取前以到输入流的末尾返回-1。 
    int read(char[] cbuf, int off, int len)
    ```
- Writer 是所有的输出字符流的父类，它是一个抽象类,主要包含如下六个方法：
    ``` java
    //向输出流中写入一个字符数据,该字节数据为参数b的低16位。 
    void write(int c); 
    //将一个字符类型的数组中的数据写入输出流， 
    void write(char[] cbuf) 
    //将一个字符类型的数组中的从指定位置（offset）开始的,length个字符写入到输出流。 
    void write(char[] cbuf, int offset, int length); 
    //将一个字符串中的字符写入到输出流。 
    void write(String string); 
    //将一个字符串从offset开始的length个字符写入到输出流。 
    void write(String string, int offset, int length); 
    //将输出流中缓冲的数据全部写出到目的地。 
    void flush()
    ```


#### 2.3 高效流读写操作



### 03.Api调用说明
#### 3.1 从文件中读数据
- 从文件中读数据，使用普通字节流或者字符流方式，如下所示
    ``` java
    //字节流读取file文件，转化成字符串
    String file2String = FileIoUtils.readFile2String1(fileName);
    //字符流读取file文件，转化成字符串
    String file2String = FileIoUtils.readFile2String2(fileName);
    ```
- 从文件中读数据，使用高效流方式，如下所示
    ``` java
    //高效字节流读取file文件，转化成字符串
    String file2String = BufferIoUtils.readFile2String1(fileName);
    //高效字符流读取file文件，转化成字符串
    String file2String = BufferIoUtils.readFile2String2(fileName);
    ```


#### 3.2 将内容写入文件
- 从文件中读数据，使用普通字节流或者字符流方式，如下所示
    ``` java
    //使用字节流，写入字符串内容到文件中
    FileIoUtils.writeString2File1(content,fileName);
    //使用字符流，写入字符串内容到文件中
    FileIoUtils.writeString2File2(content,fileName);
    ```
- 从文件中读数据，使用高效流方式，如下所示
    ``` java
    //高效字节流写入字符串内容到文件中
    BufferIoUtils.writeString2File1(content,fileName);
    //高效字符流写入字符串内容到文件中
    BufferIoUtils.writeString2File2(content,fileName);
    ```


#### 3.3 文件复制操作



#### 3.4 读写效率优化



### 04.遇到的坑分析
#### 4.1 读取数据乱码
- 首先看一下数据编解码
    - 编码:把看得懂的变成看不懂的:	Object -- byte[]
    - 解码:把看不懂的变成看得懂的:	byte[] -- Object
- 解码的时候需要设定编码格式
    - 在 GB 2312 编码或 GBK 编码中，一个英文字母字符存储需要1个字节，一个汉字字符存储需要2个字节。
    - 在UTF-8编码中，一个英文字母字符存储需要1个字节，一个汉字字符储存需要3到4个字节。
    - 在UTF-16编码中，一个英文字母字符存储需要2个字节，一个汉字字符储存需要3到4个字节（Unicode扩展区的一些汉字存储需要4个字节）。
    - 在UTF-32编码中，世界上任何字符的存储都需要4个字节。
- 为何数据乱码
    - 有时候读取的数据是乱码，就是因为编码方式不一致，需要进行转换，然后再按照unicode进行编码。


### 05.其他问题说明
#### 5.1 为何要有处理流
- 使用处理流的一个明显好处是，只要使用相同的处理流，程序就可以采用完全相同的输入/输出代码来访问不同的数据源，随着处理流所包装节点流的变化，程序实际所访问的数据源也相应地发生变化。



#### 5.2 高效流原理










