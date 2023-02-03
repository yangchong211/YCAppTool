# 加密和解密
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.该库性能分析



### 01.基础概念介绍
#### 1.1 加解密的基础
- 1）在对称加密算法中
    - 双方使用的密钥相同，要求解密方事先必须知道加密密钥。这类加密算法技术较为成熟，加密效率高。
- 2）在非对称加密算法中
    - 收发双方使用不同的密钥，发方使用公开密钥对消息进行加密，收发使用私有密钥机型解密，保密性更高，但效率更低。
- 3）单向加密算法
    - 输入明文后由算法直接加密成密文，密文无法解密，只有重新输入密文，并经过同样的加密算法处理，得到形同的密文并被系统重新识别后，才能真正的解密。



#### 1.2 MD5加密方式
- MD5（信息-摘要算法5）
    - 用于确保信息传输完整一致。是计算机广泛使用的杂凑算法之一（又译摘要算法、哈希算法），主流编程语言普遍已有MD5实现。
- MD5算法具有特点
    - 1、压缩性：任意长度的数据，算出的MD5值长度都是固定的。
    - 2、容易计算：从原数据计算出MD5值很容易。
    - 3、抗修改性：对原数据进行任何改动，哪怕只修改1个字节，所得到的MD5值都有很大区别。
    - 4、强抗碰撞：已知原数据和其MD5值，想找到一个具有相同MD5值的数据（即伪造数据）是非常困难的。
    - 5、MD5加密是不可逆的。
- MD5使用场景
    - 1.用户登录的时候，系统把用户输入的password计算成MD5值，然后保存该值，每次用户输入密码用md5对比即可知道密码是否正确。
    - 2.文件的完整性校验，在传递文件的过程中附带传递文件的md5值，接收端通过比较文件的md5值判断文件的完整性。
- 注意事项说明
    - 由于网络MD5数据库比较大，如果直接使用md5加密的话，可能会被破译出来，这是在加密的过程中”加盐”就可以大大避免直接被破译的危险。


#### 1.3 Base64加解密
- 为何很多对称和非对称加密解密还要对数据base64处理？
    - 各种数据加密方式，最终都会对加密后的二进制数据进行Base64编码，起到一种二次加密的效果，其实Base64从严格意义上来说的话不是一种加密算法，而是一种编码算法，为何要使用Base64编码呢？它解决了什么问题？
- 为何要有base64编码，解决什么问题？
    - 在计算机中任何数据都是按ascii码存储的，而ascii码的128～255之间的值是不可见字符。
    - 而在网络上交换数据时，比如说从A地传到B地，往往要经过多个路由设备，由于不同的设备对字符的处理方式有一些不同，这样那些不可见字符就有可能被处理错误，这是不利于传输的。
    - 所以就先把数据先做一个Base64编码，统统变成可见字符，这样出错的可能性就大降低了。
- Base64是网络上最常见的用于传输8Bit字节代码的编码方式之一
    - Base64并不是安全领域的加密算法，其实Base64只能算是一个编码算法，对数据内容进行编码来适合传输。准确说是把一些二进制数转成普通字符用于网络传输。
- **Base64编码本质上是一种将二进制数据转成文本数据的方案**
    - 对于非二进制数据，是先将其转换成二进制形式，然后每连续6比特（2的6次方=64）计算其十进制值，根据该值在A--Z,a--z,0--9,+,/ 这64个字符中找到对应的字符，最终得到一个文本字符串。
- 基本规则如下几点：
    - 标准Base64只有64个字符（英文大小写、数字和+、/）以及用作后缀等号；
    - Base64是把3个字节变成4个可打印字符，所以Base64编码后的字符串一定能被4整除（不算用作后缀的等号）；
    - 等号一定用作后缀，且数目一定是0个、1个或2个。这是因为如果原文长度不能被3整除，Base64要在后面添加\0凑齐3n位。为了正确还原，添加了几个\0就加上几个等号。显然添加等号的数目只能是0、1或2；
    - 严格来说Base64不能算是一种加密，只能说是编码转换。




#### 1.4 对称加密和解密
- **一句话概括：加密和解密都是用相同密钥**。
    - 密钥可以自己指定，只有一把密钥，如果密钥暴露，文件就会被暴露。
    - 特点是加密速度很快，但是缺点是安全性较低，因为只要密钥暴漏，数据就可以被解密了。
- 常见的对称加密和解密：
    - DES算法；AES算法


#### 1.5 非对称加密解密
- 加密和解密的规则是：
    - **公钥加密只能私钥解密，私钥加密只能公钥解密**。特点是加密速度慢些，但是安全系数很高
- 应用场景举例：
    - 在集成支付宝支付sdk时，需要生成私钥和公钥，公钥需要设置到支付宝网站的管理后台，在程序中调用支付接口的时候，使用我们自己的私钥进行加密，这样支付宝由于有公钥可以解密，其他人即时劫持了数据，但是没有公钥，也无法解密。
- 常见非对称加密：
    - RSA


#### 1.6 Hash算法实现
- 散列算法是什么
    - 是把任意长度的输入（又叫做预映射 pre-image ）通过算法变换成固定长度的输出。散列是信息的提炼，通常其长度要比信息小得多，且为一个固定长度。
- 特性：
    - 1.高效：可以快速计算出哈希值；2.不可逆：从哈希值不能反向推导出原始数据；3.输入敏感：原始数据只要有一点变动，得到的哈希值也会差别很大；4.冲突避免：对于不同的原始数据，哈希值相同的概率非常小。
- 使用场景：
    - 安全加密。 例如网络传输密码。
    - 唯一标识。例如在海量的图库中，搜索一张图是否存在。可以给每一个图片取一个唯一标识，或者说信息摘要，通过这个唯一标识来判定图片是否在图库中，这样就可以减少很多工作量
    - 数据校验。例如下载文件的完整性
    - 负载均衡。通过哈希算法，对客户端 IP 地址或者会话 ID 计算哈希值，将取得的哈希值与服务器个数进行取模运算，最终得到的值就是应该被路由到的服务器编号
- Hash算法常见案例(MD5、SHA1、SHA256)：
    - MD4 1990年，输出128位 （已经不安全）
    - MD5 1991年，输出128位 （已经不安全）
    - SHA-0 1993年，输出160位 （发布之后很快就被撤回，是SHA-1的前身）
    - SHA-1 1995年，输出160位 （已经不安全）
    - SHA-2 包括 SHA-224、SHA-256、SHA-384，和 SHA-512，分别输出 224、256、384、512位。 (目前安全)


#### 1.7 RC4加密算法
- RC4算法
    - RC4是一种对称密码算法，它属于对称密码算法中的序列密码(streamcipher,也称为流密码)，它是可变密钥长度，面向字节操作的流密码。
- RC4算法特点：
    - (1)、算法简洁易于软件实现，加密速度快，安全性比较高(在存储大量的信息时，RC4展现出它的实用性，面对大量的数据，运行简单，速度快)；
    - (2)、密钥长度可变，一般用256个字节。
- RC4工作规则
    - 第一步：输入密钥+需要加密的信息；第二步：通过密钥逐字节的对信息进行加密；第三步：接收方通过密钥解密数据



### 02.常见思路和做法
#### 2.1 MD5加密过程
- 第一步，将数据转化为字节byte数组
- 第二步，对明文进行处理产生一个128位（16字节）的散列值



#### 2.2 DES对称加密流程



#### 2.3 RSA非对称加密流程


#### 2.4 Base64编解码
  



### 03.Api调用说明
- 关于MD5加密Api如下所示
    ``` java
    //对字符串md5加密
    String md2 = Md5EncryptUtils.encryptMD5ToString(string);
    //对字符串md5加密，加盐处理
    String md3 = Md5EncryptUtils.encryptMD5ToString(string,"doubi");
    //对字节数据md5加密
    String md4 = Md5EncryptUtils.encryptMD5ToString(bytes);
    //对字节数据md5加密，加盐处理
    String md5 = Md5EncryptUtils.encryptMD5ToString(bytes,"doubi".getBytes());
    //对文件进行md5加密
    String md6 = Md5EncryptUtils.encryptMD5File1(txt);
    //对文件进行md5加密
    String md7 = Md5EncryptUtils.encryptMD5File2(new File(txt));
    ```
- 关于base64加密和解密的Api如下所示
    ``` java
    //字符Base64加密
    String strBase64_2 = Base64Utils.encodeToString(str);
    //字符Base64解密
    String strBase64_3 = Base64Utils.decodeToString(strBase64_2);
    ```
- 关于DES加密和解密的Api如下所示
    ``` java
    //DES加密字符串
    String encrypt1 = DesEncryptUtils.encrypt(string,password);
    //DES解密字符串
    String decrypt1 = DesEncryptUtils.decrypt(encrypt1 , password);
    //DES加密文件
    String encryptFile1 = DesEncryptUtils.encryptFile(password, fileName, destFile);
    //DES解密文件
    String decryptFile1 = DesEncryptUtils.decryptFile(password, destFile, destFile3);
    //DES 加密后转为 Base64 编码
    String encrypt2 = DesEncryptUtils.encrypt(string.getBytes(), password.getBytes());
    //DES解密字符串 Base64 解码
    String decrypt2 = DesEncryptUtils.decrypt(encrypt2.getBytes(), password.getBytes());
    ```
- 关于AES加密和解密的Api如下所示
    ``` java
    //AES加密字符串
    String encrypt1 = AesEncryptUtils.encrypt(string,password);
    //AES解密字符串
    String decrypt1 = AesEncryptUtils.decrypt(encrypt1 , password);
    ```
- 关于RC4加密和解密的Api如下所示
    ``` java
    //RC4加密
    String encrypt1 = Rc4EncryptUtils.encryptString(string, secretKey);
    //RC4解密
    String decrypt1 = Rc4EncryptUtils.decryptString(encrypt1, secretKey);
    //RC4加密base64编码数据
    String encrypt2 = Rc4EncryptUtils.encryptToBase64(bytes1, secretKey);
    //RC4解密base64解码数据
    byte[] bytes = Rc4EncryptUtils.decryptFromBase64(encrypt2, secretKey);
    ```



### 04.遇到的坑分析
#### 4.1 MD5破解说明
- MD5破解的方式有哪些？
    - 第一种：


#### 4.2 ASCII不可见字符
- ASCII码是什么东西
    - ASCII（美国信息交换标准代码）是基于拉丁字母的一套电脑编码系统，主要用于显示现代英语和其他西欧语言。
- 关于控制台复制文本被截断，不能全部复制出来的问题。
    - 这是由于ASCII编码字符的“不可见字符”导致，不可见字符为控制字符，例如换行，回车等等。
- 有时候也把不可见字符叫做转义字符
    - C语言中定义了一些字母前加""来表示常见的那些不能显示的ASCII字符，如代码用的\t、\n等，就称为转义字符，因为该字符都不是它本来的ASCII字符意思。


#### 4.4 RSA加解密问题
- 加密填充方式导致解密异常
    - 遇到Android这边加密过的数据，服务器端死活解密不了，原来android系统的RSA实现是"RSA/None/NoPadding"，而标准JDK实现是"RSA/None/PKCS1Padding"，这造成了在android机上加密后无法在服务器上解密的原因，所以在实现的时候这个一定要注意这个问题。
- RSA加密内容长度限制问题
    - 内容长度有限制，1024位key的最多只能加密127位数据，否则就会报错(javax.crypto.IllegalBlockSizeException: Data must not be longer than 117 bytes) 
    - RSA 算法规定：待加密的字节数不能超过密钥的长度值除以 8 再减去 11（即：KeySize / 8 - 11），而加密后得到密文的字节数，正好是密钥的长度值除以 8（即：KeySize / 8）。




### 05.该库性能分析
#### 5.1 加解密效率分析
- 对称加密和解密效率分析（加密&解密时间，数据内容增大后加密和解密时间）
- 非对称加密和解密效率分析（加密&解密时间，数据内容增大后加密和解密时间）


#### 5.2 非对称加密为何慢




- Android常见网络数据加密：https://www.jianshu.com/p/f1081e7a6e20
- Android数据加密概述及多种加密方式：https://www.cnblogs.com/zsychanpin/p/7308866.html
- Android传输数据时加密详解：https://cloud.tencent.com/developer/article/2086547
- 使用RSA分段加解密：https://blog.csdn.net/dongaddxing/article/details/116041314
- Android应用安全开发之浅谈加密算法的坑: https://www.lmlphp.com/user/58551/article/item/645568/






