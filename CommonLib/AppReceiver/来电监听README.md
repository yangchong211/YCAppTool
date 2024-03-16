# 监听来电和去电
#### 目录介绍
- 01.基础概念
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.其他问题说明



### 01.基础概念
#### 1.1 掌握基础概念
- 业务场景说明
    - 在App进行音视频聊天的时，这个时候来电了，电话接通后，需要关闭音视频聊天。这个时候就需要监听电话来电和去电状态。
- 实现手机电话状态的监听
    - 主要依靠两个类：TelephoneManger和PhoneStateListener。


#### 1.2 监听来电去电
- 监听来电去电能干什么
    - 第一：能够针对那些特殊的电话进行自动挂断，避免打扰到用户。
    - 第二：在业务中，针对来电和去电接通后，需要关闭音视频通话。


### 02.常见思路和做法
#### 2.1 如何实现电话监听
- 如何实现电话状态的监听呢？
    - Android在电话状态改变是会发送action为android.intent.action.PHONE_STATE的广播，而拨打电话时会发送action为android.intent.action.NEW_OUTGOING_CALL的广播。
    - 看了下开发文档，暂时没发现有来电时的广播。通过自定义广播接收器，接受上述两个广播便可。


#### 2.2 如何来电监听
- 如何监听来电
    - 来电监听是使用PhoneStateListener类，使用方式是，将PhoneStateListener对象注册到系统电话管理服务中去（TelephonyManager）
    - 然后通过PhoneStateListener的回调方法onCallStateChanged(int state, String incomingNumber) 实现来电的监听


#### 2.3 如何去电监听
- 如何去电监听【静态注册】
    - 这个时候通过广播来实现。需要在清单文件中注册。
    ``` xml
    <receiver android:name=".PhoneReceiver">
        <intent-filter>
            <action android:name="android.intent.action.PHONE_STATE"/>
            <action android:name="android.intent.action.NEW_OUTGOING_CALL" />
        </intent-filter>
    </receiver>
    ```



### 03.Api调用说明
#### 3.1 如何依赖
- 直接在maven中依赖如下所示：
    ``` java
    
    ```


#### 3.2 如何使用Api
- api调用如下所示，直接拿来用即可
    ``` java
    PhoneManager.getInstance().setOnPhoneListener(new OnPhoneListener() {
        @Override
        public void callIdle() {
            ToastUtils.showRoundRectToast("挂断");
        }

        @Override
        public void callOffHook() {
            ToastUtils.showRoundRectToast("接听");
        }

        @Override
        public void callRunning() {
            ToastUtils.showRoundRectToast("响铃");
        }
    });
    PhoneManager.getInstance().registerPhoneStateListener(this);
    ```



### 04.遇到的坑分析
#### 4.1 注意权限问题
- 监听电话来电和去电权限
    - 记得在清单文件添加：<uses-permission android:name="android.permission.CALL_PHONE" />




### 05.其他问题说明
#### 5.1 问题思考一下
- 安卓监听打电话（去电）
    - https://www.jianshu.com/p/b1f5b9d85d6d
    - https://m.xp.cn/b.php/105580.html

