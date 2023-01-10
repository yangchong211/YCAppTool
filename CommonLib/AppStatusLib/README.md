#### 目录介绍
- 01.基础概念
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析



### 01.基础概念



### 02.常见思路和做法



### 03.Api调用说明
- api调用如下所示，直接拿来用即可
    ``` kotlin
    String cachePath = AppFileUtils.getCacheFilePath(MainApplication.getInstance(),"cache");
    String path = cachePath + File.separator + "status";
    File file = new File(path);
    AppStatusManager manager = new AppStatusManager.Builder()
            .context(MainApplication.getInstance())
            .file(file)
            .networkSwitchOn(true)
            .wifiSwitchOn(false)
            .bluetoothSwitchOn(false)
            .screenSwitchOn(false)
            .threadSwitchOn(false)
            .batterySwitchOn(false)
            .builder();
    manager.registerAppStatusListener(new BaseStatusListener() {
        @Override
        public void wifiStatusChange(boolean isWifiOn) {
            super.wifiStatusChange(isWifiOn);
            if (isWifiOn){
                AppLogUtils.i("app status Wifi 打开");
            } else {
                AppLogUtils.i("app status Wifi 关闭");
            }
        }
    
        @Override
        public void gpsStatusChange(boolean isGpsOn) {
            super.gpsStatusChange(isGpsOn);
            if (isGpsOn){
                AppLogUtils.i("app status Gps 打开");
            } else {
                AppLogUtils.i("app status Gps 关闭");
            }
        }
    
        @Override
        public void networkStatusChange(boolean isConnect) {
            super.networkStatusChange(isConnect);
            if (isConnect){
                AppLogUtils.i("app status Network 打开");
            } else {
                AppLogUtils.i("app status Network 关闭");
            }
        }
    
        @Override
        public void screenStatusChange(boolean isScreenOn) {
            super.screenStatusChange(isScreenOn);
            if (isScreenOn){
                AppLogUtils.i("app status Screen 打开");
            } else {
                AppLogUtils.i("app status Screen 关闭");
            }
        }
    
        @Override
        public void screenUserPresent() {
            super.screenUserPresent();
            AppLogUtils.i("app status Screen 使用了");
        }
    
        @Override
        public void bluetoothStatusChange(boolean isBluetoothOn) {
            super.bluetoothStatusChange(isBluetoothOn);
            if (isBluetoothOn){
                AppLogUtils.i("app status 蓝牙 打开");
            } else {
                AppLogUtils.i("app status 蓝牙 关闭");
            }
        }
    
        @Override
        public void batteryStatusChange(AppBatteryInfo batteryInfo) {
            super.batteryStatusChange(batteryInfo);
            AppLogUtils.i("app status 电量 " + batteryInfo.toStringInfo());
        }
    
        @Override
        public void appThreadStatusChange(AppThreadInfo threadInfo) {
            super.appThreadStatusChange(threadInfo);
            AppLogUtils.i("app status 所有线程数量 " + threadInfo.getThreadCount());
            AppLogUtils.i("app status run线程数量 " + threadInfo.getRunningThreadCount().size());
            AppLogUtils.i("app status wait线程数量 " + threadInfo.getWaitingThreadCount().size());
            AppLogUtils.i("app status block线程数量 " + threadInfo.getBlockThreadCount().size());
            AppLogUtils.i("app status timewait线程数量 " + threadInfo.getTimeWaitingThreadCount().size());
        }
    });
    ```



### 04.遇到的坑分析






