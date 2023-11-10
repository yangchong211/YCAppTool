#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析



### 01.基础概念介绍


### 02.常见思路和做法


### 03.Api调用说明
- 通用打印Intent对象内容到log日志栏中，支持普通intent和延迟PendingIntent。超级方便检查，可以打印action，category，data，flags，extras等等
    ```
    //打印intent所有的数据
    IntentLogger.print("intent test : ", intent)
    //打印intent中component
    IntentLogger.printComponentName("intent component : " , intent)
    //打印intent中extras参数
    IntentLogger.printExtras("intent test : ", intent)
    //打印intent中flags参数
    IntentLogger.printFlags("intent test : ", intent)
    
    //PendingIntent
    //打印intent所有的数据
    PendingIntentLogger.print("intent test : ", intent)
    //打印intent中content
    PendingIntentLogger.printContentIntent("intent content : " , intent)
    //打印intent的tag
    PendingIntentLogger.printTag("intent tag : " , intent)
    ```




### 04.遇到的坑分析






