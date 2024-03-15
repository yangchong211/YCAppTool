# URI和URL工具库
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.其他问题说明



### 01.基础概念说明
#### 1.1 看一个案例
- 看看下面的链接，说一下哪些是uri，哪些是url
    - https://github.com/yangchong211/LifeHelper                    这个是url
    - file:///Users/yangchong/Documents/yc.jpg                      这个是uri
    - http://localhost:8080/index.html                              这个是url
    - http://xxxx.xx.com/xx.php?params1=value1&params2=value2       这个是url
    - ftp://ftp.is.co.za/rfc/rfc1808.txt                            这个是url


#### 1.2 URI说明
- 什么是uri
    - URI全称是Uniform Resource Identifier，也就是统一资源标识符，它是一种采用特定的语法标识一个资源的字符串表示。
    - URI所标识的资源可能是服务器上的一个文件，也可能是一个邮件地址、图书、主机名等。
- uri的格式
    - scheme:[//[user[:password]@]host[:port]][/path][?query][#fragment]


#### 1.3 URL说明
- 什么是url
    - URL全称是Uniform Resource Location，也就是统一资源位置。实际上，URL就是一种特殊的URI，它除了标识一个资源，还会为资源提供一个特定的网络位置，客户端可以通过它来获取URL对应的资源。
- URL（Uniform Resource Locator，统一资源定位符）的格式
    ```
    scheme://host[:port#]/path/.../[;url-params][?query-string][#anchor]
    > scheme //有我们很熟悉的http、https、ftp以及著名的ed2k，迅雷的thunder等。
    > host   //HTTP服务器的IP地址或者域名
    > port#  //HTTP服务器的默认端口是80，这种情况下端口号可以省略。如果使用了别的端口，必须指明，例如tomcat的默认端口是8080 http://localhost:8080/
    > path   //访问资源的路径
    > url-params  //所带参数 
    > query-string    //发送给http服务器的数据
    > anchor //锚点定位
    ```


#### 1.4 URI何URL联系
- URI和URL的关系：
    - URL是一种特殊的URI，是URI包括URL。
    - URI属于URL更高层次的抽象，一种字符串文本标准。就是说，URI属于父类，而URL属于URI的子类。URL是URI的一个子集。URI还有一个子类URN-统一资源名称。
- 二者的区别在于
    - URI 表示请求服务器的路径，定义这么一个资源。
    - URL 同时说明要如何访问这个资源（http://）。


### 02.常见思路和做法


### 03.Api调用说明



### 04.遇到的坑分析


### 05.其他问题说明




