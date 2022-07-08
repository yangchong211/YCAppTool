#### 目录介绍



### 07.App重新启动库
- 第一种方式，开启一个新的服务KillSelfService，用来重启本APP。
    ``` java
    RestartAppHelper.restartApp(this,RestartFactory.SERVICE);
    ```
- 第二种方式，使用闹钟延时，使用PendingIntent延迟意图，然后重启app
    ``` java
    RestartAppHelper.restartApp(this,RestartFactory.ALARM);
    ```
- 第三种方式，检索获取项目中LauncherActivity，然后设置该activity的flag和component启动app
    ``` java
    RestartAppHelper.restartApp(this,RestartFactory.MAINIFEST);
    ```




