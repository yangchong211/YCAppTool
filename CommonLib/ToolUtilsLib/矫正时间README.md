# 校正系统时间方案
#### 目录介绍
- 01.基础概念
- 02.常见思路和做法
- 03.Api调用说明
- 04.测试case罗列
- 05.遇到的坑分析



### 01.基础概念
- 先说下业务场景
    - 在开发过程中我们常常需要获取系统时间。Android系统的自动确认时间，是由系统通过访问厂家的NTP服务器的时间，然后修改后得到的。
    - 所以当没有网络或者在内网环境下的时候，系统无法访问到NTP服务器，便会造成系统时间错误。所以这个时候我们就需要程序去修改系统的时间，或者获取一个正确的时间来代替系统时间。
- NTP服务器
    - 用来使计算机时间同步化的一种协议，它可以使计算机对其服务器或时钟源（如石英钟，GPS等等)做同步化，它可以提供高精准度的时间校正（LAN上与标准间差小于1毫秒，WAN上几十毫秒）且可介由加密确认的方式来防止恶毒的协议攻击。
    - 时间按NTP服务器的等级传播。按照离外部UTC源的远近把所有服务器归入不同的Stratum（层）中。



### 02.常见思路和做法
- 根据不同的情况，实现了如下两种解决方案：
- 获取NTP服务器时间代替系统时间。
    - 优点：无需Root，适用于任何手机及系统。缺点：需要可以访问外部网络，内网环境下则需要一台自己的NTP服务器。
- 获取网页时间代替系统时间。
    - 优点：无需Root，适用于任何手机及系统，适用于任何网络环境。缺点：需要一条额外的线程，去维护时间准确，容易造成误差。



### 03.Api调用说明
- 注意两点：
    - 设置时区为北京时间：GMT-8为北京时间；中国科学技术大学NTP服务器：time.ustc.edu.cn
- 初始化Api说明
    ```
    try {
        NtpGetTime.build()
                .withNtpHost("time.ustc.edu.cn")
                .withSharedPreferencesCache(MainApplication.getInstance())
                .withConnectionTimeout(30000)
                .initialize();
    } catch (IOException e) {
        e.printStackTrace();
        AppLogUtils.e("something went wrong when trying to initialize NtpGetTime "+ e);
    }
    ```
- 如何获取校对后的真实时间
    ```
    if (!NtpGetTime.isInitialized()) {
        return;
    }
    //获取校对后时间
    Date trueTime = NtpGetTime.now();
    //获取当前设备时间
    Date deviceTime = new Date();
    ```


### 04.测试case罗列
- 修改手机或者硬件设备时间信息，然后调用该库获取校对时间，查看是否准确获取最新北京时间。


### 05.遇到的坑分析




