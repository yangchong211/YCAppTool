# APM流量采集工具
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.其他问题说明



### 01.基础概念说明




### 02.常见思路和做法
#### 2.1 流量计算方案
- Android目前提供了两种流量计算方案
    - TrafficStats
    - NetworkStatsManager



#### 2.2 TrafficStats
- Android API8后提供的该类，可以获取设备重启以来的流量信息。
    ```
    public class TrafficStats {
      public static long getMobileRxBytes() //移动网络接受的总流量
      public static long getMobileTxBytes() //移动网络发送的总流量
      public static long getTotalTxBytes() //发送的总数据流量
      public static long getTotalRxBytes() //接收的总数据流量
      public static long getUidRxBytes(int uid) //指定uid接收的流量
      public static long getUidTxBytes(int uid) //指定uid发送的流量
    }
    ```
- 优点：
    - 调用方法简单，无需特别权限
- 缺点：
    - 无法获取某个时间段内的流量消耗
    - 统计的是设备启动以来的流量数据，无法判断是从哪个时间段开始的流量统计
    - 无法获取指定应用的wifi类型流量数据
    - 虽然可以通过监听网络变化去获取对应数据，实际可操作性不高


#### 2.3 NetworkStatsManager
- Android 6.0之后新增加的类，可以获取历史的流量信息，并且支持查询时间段的流量数据
    - 根据上述提供的方法，可以得到设备一直的流量数据，并且支持按照networkType区分和startTime~endTime获取指定时间段的流量数据。
    - 提供对网络使用历史和统计数据的访问，分为Summary queries（摘要查询）和 History queries（历史查询）
- 一些核心api方法调用
    ```
    查询网络使用统计摘要。（查询多个应用流量统计） 会返回多个桶 需要循环遍历 在根据uid 的到每一个的实时流量
    querySummary(int networkType, String subscriberId, long startTime, long endTime)
    查询网络使用统计摘要。结果是整个设备的汇总数据使用情况。结果是随着时间、状态、uid、标签、计量和漫游聚合的单个存储桶
    querySummaryForDevice(int networkType, String subscriberId, long startTime, long endTime)
    ```
- 优点：
    - 可以获取指定类型以及时间段的流量数据
- 缺点：
    - 需要申请特殊权限以及做权限适配
    - 使用较复杂





### 03.Api调用说明



### 04.遇到的坑分析


### 05.其他问题说明
#### 5.1 TrafficStats原理
- TrafficStats大概工作原理
    - 它的实现原理其实也非常简单，就是利用 Linux 内核的统计接口。具体来说，是下面两个 proc 接口。
    ```
    // stats接口提供各个uid在各个网络接口（wlan0, ppp0等）的流量信息
    /proc/net/xt_qtaguid/stats
    // iface_stat_fmt接口提供各个接口的汇总流量信息
    proc/net/xt_qtaguid/iface_stat_fmt
    ```
- TrafficStats 的工作原理是读取 proc
    - 将目标 UID 下面所有网络接口的流量相加。但如果我们不使用 TrafficStats 接口，而是自己解析 proc 文件呢？
    - 那我们可以得到不同网络接口下的流量，从而计算出 WiFi、2G/3G/4G、VPN、热点共享、WiFi P2P 等不同网络状态下的流量。
    - 不过非常遗憾的是，Android 7.0 之后系统已经不让我们直接去读取 stats 文件，防止开发者可以拿到其他应用的流量信息，因此只能通过 TrafficStats 拿到自己应用的流量信息。




### 参考博客
- https://sniffer.site/2020/04/01/%E8%AF%B4%E8%AF%B4android%E4%B8%AD%E7%9A%84%E6%B5%81%E9%87%8F%E7%BB%9F%E8%AE%A1/
- 网络优化（下）：大数据下网络该如何监控？
  - https://blog.yorek.xyz/android/paid/master/network_3/

