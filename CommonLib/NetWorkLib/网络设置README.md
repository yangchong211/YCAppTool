# 设置默认网络库
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.其他问题说明



### 01.基础概念说明
#### 1.1 监听默认的网络
- Android 应用都有一个由系统确定的默认网络
    - 系统通常首选不按流量计费的网络而非按流量计费的网络，首选网速较快的网络而非网速较慢的网络。
    - 当应用发出网络请求（例如使用 HttpsURLConnection）时，系统会使用默认网络满足该请求。应用也可以通过其他网络发送流量。
- 举一个例子：Wi-Fi，移动流量共存的场景下
    - 系统会默认选择Wi-Fi，你玩App或者看视频，会默认走Wi-Fi流量，而非是移动流量。网络优先级，是根据不同网络评分机制而来的。
    - 当Wi-Fi信号不好，系统会切换到流量上来。当Wi-Fi信号好了，系统又回切换到Wi-Fi上来，系统在某一个时刻，只会用某一种流量！
- 设置为默认网络的网络可能随时发生变化
    - 典型的例子是设备处于一个已知活跃、不按流量计费、速度快于移动网络的 Wi-Fi 接入点的覆盖范围内。
    - 设备会连接到此接入点，并将所有应用的默认网络切换到新的 Wi-Fi 网络。
    - 当新网络成为默认网络时，应用打开的任何新连接都会使用此网络。一段时间后，上一个默认网络上的所有剩余连接都将被强制终止。
- 那么如何监听系统默认的网络
    - 如果知道默认网络发生变化的时间对应用很重要，它会按这种方式注册默认网络回调：registerDefaultNetworkCallback
    - 新网络成为默认网络后，应用会收到对新网络的 onAvailable(Network) 的调用。


#### 1.2 设置默认网络
- 如何设置默认的网络
    - 虽然默认网络是大多数应用的唯一相关网络，但某些应用可能希望使用其他可用网络。为了查找这些网络，应用会构建与其需求匹配的 NetworkRequest。
    - 然后调用 ConnectivityManager.registerNetworkCallback(NetworkRequest, NetworkCallback)。
- 一些注意事项的说明
    - 尽管在任何给定时间只能有一个默认网络应用于某个应用，但此方法可让您的应用同时看到所有可用网络。
    - 因此，调用 onLost(Network) 表示网络已永久断开连接，而非表示它不再是默认网络。


#### 1.3 设置默认网络影响
- 思考一个问题
    - 设置默认网络，这个会影响到其他App吗？
    - 举个例子，比如Wi-Fi和移动流量共存，系统优先使用Wi-Fi，这个时候设置默认网络为移动流量，那么会影响其他App使用的流量方式吗？


#### 1.4 数据传输限制
- 如何理解网络对数据传输的限制
    - 能够通过网络回调看到某个网络并不意味着您的应用可以使用该网络进行数据传输。某些网络不提供互联网连接，而有些网络可能仅限特权应用使用。
    - 后台网络的使用也要经过权限检查。如果您的应用需要使用后台网络，则需要 CHANGE_NETWORK_STATE 权限。
- 例如在设备连接到 Wi-Fi 网络的情况下尝试启动移动网络
    - 此类应用会调用 ConnectivityManager.requestNetwork(NetworkRequest, NetworkCallback)，在网络启动后会调用 NetworkCallback。



### 02.常见思路和做法
#### 2.1 多个网络连接
- Android 5.0 提供了新的多网络 API，允许您的应用动态扫描具有特定能力的可用网络，并与它们建立连接。
    - 当您的应用需要 SUPL、彩信或运营商计费网络等专业化网络时，或者您想使用特定类型的传输协议发送数据时，就可以使用此功能。
- 要从您的应用以动态方式选择并连接网络，请执行以下步骤：
    - 1.创建一个 ConnectivityManager。
    - 2.使用 NetworkRequest.Builder 类创建一个 NetworkRequest 对象，并指定您的应用感兴趣的网络功能和传输类型。
    - 3.要扫描合适的网络，请调用 requestNetwork() 或 registerNetworkCallback()，并传入 NetworkRequest 对象和 ConnectivityManager.NetworkCallback 的实现。
        - 如果您想在检测到合适的网络时主动切换到该网络，请使用 requestNetwork() 方法；
        - 如果只是接收已扫描网络的通知而不需要主动切换，请改用 registerNetworkCallback() 方法。
- 当系统检测到合适的网络时，它会连接到该网络并调用 onAvailable() 回调。
    - 您可以使用回调中的 Network 对象来获取有关网络的更多信息，或者引导通信使用所选网络。



#### 2.2 案例测试介绍
- 案例说明一下
    - 在此示例中，设备会连接到稳定的 Wi-Fi 接入点，然后断开连接。该示例还假定设备已启用始终开启移动数据设置。
    - 然后看一下，具有移动网络连接的设备上同时注册默认回调和常规回调时可能会获得的回调序列。


#### 2.3 测试回调顺序
- 1.当应用调用 registerNetworkCallback() 时
    - 回调会立即收到来自移动网络的 onAvailable()、onNetworkCapabilitiesChanged() 和 onLinkPropertiesChanged() 的调用，因为只有该网络可用。
    - 如果有其他网络可用，应用还会收到另一个网络的回调。
- 2.然后，应用调用 registerDefaultNetworkCallback()。
    - 默认网络回调开始接收对移动网络的 onAvailable()、onNetworkCapabilitiesChanged() 和 onLinkPropertiesChanged() 的调用，因为移动网络是默认网络。
    - 如果还有另一个非默认网络在运行，应用无法接收对该非默认网络的调用。
- 3.之后，设备会连接到（不按流量计费的）Wi-Fi 网络
    - 常规网络回调会收到对 Wi-Fi 网络的 onAvailable()、onNetworkCapabilitiesChanged() 和 onLinkPropertiesChanged() 的调用。
- 4.此时，Wi-Fi 网络可能需要花一些时间进行验证
    - 在本例中，常规网络回调的 onNetworkCapabilitiesChanged() 调用不包括 NET_CAPABILITY_VALIDATED 功能。
    - 很快，它会收到对 onNetworkCapabilitiesChanged() 的调用，其中的新功能将包含 NET_CAPABILITY_VALIDATED。在大多数情况下，验证都非常快。
    - 当 Wi-Fi 网络通过验证后，系统会优先选择 Wi-Fi 网络而非移动网络，主要因为前者不按流量计费。
    - Wi-Fi 网络成为默认网络，因此默认网络回调会收到对 Wi-Fi 网络的 onAvailable()、onNetworkCapabilitiesChanged() 和 onLinkPropertiesChanged() 的调用。
    - 移动网络进入后台，常规网络回调会收到对移动网络的 onLosing() 的调用。
    - 由于本示例假设该设备始终开启移动数据，因此移动网络不会断开。如果关闭此设置，一段时间后移动网络就会断开连接，常规网络回调将收到对 onLost() 的调用。
- 5.之后，设备仍会突然断开与 Wi-Fi 的连接，因为它超出了该网络的覆盖范围
    - 由于 Wi-Fi 已断开连接，因此常规网络回调会收到对 Wi-Fi 的 onLost() 的调用。
    - 由于移动网络是新的默认网络，因此默认网络回调会收到对移动网络的 onAvailable()、onNetworkCapabilitiesChanged() 和 onLinkPropertiesChanged() 的调用。




### 03.Api调用说明
#### 3.1 监听默认网络
- 监听默认网络api如下所示：
    ```
    NetRequestHelper.getInstance().registerNetStatusListener(new DefaultNetCallback() {
        @Override
        public void onDefaultChange(boolean available, String netType) {
            AppLogUtils.d("NetWork-Default: " + netType + " , " + available);
        }
    });
    NetRequestHelper.getInstance().registerDefaultNetworkCallback();
    ```
- 监听默认网络注意事项
    - 新网络成为默认网络后，应用会收到对新网络的 onAvailable(Network) 的调用。
    - 测试1: Wi-Fi和移动流量共存，关闭Wi-Fi，只剩下移动流量。此时 onAvailable 会回调默认网络为移动网络；
    - 测试2: 接着步骤1，看到日志，立马开始打开Wi-Fi，此时Wi-Fi和移动流量并存，onAvailable 会回调默认网络为Wi-Fi；
    - 测试3: 接着步骤2，看到日志，立马关闭Wi-Fi，如此往复操作，可以看到如下日志；
    - 通过日志时间可知，关闭或者打开Wi-Fi，默认网络的回调会有3-5秒的时间间隔。
    ```
    2023-08-17 11:31:08.071 26447-29864 D  NetWork-Default: MOBILE , true
    2023-08-17 11:31:11.936 26447-29864 D  NetWork-Default: WIFI , true
    2023-08-17 11:31:15.778 26447-29864 D  NetWork-Default: MOBILE , true
    2023-08-17 11:31:20.367 26447-29864 D  NetWork-Default: WIFI , true
    ```



### 04.遇到的坑分析


### 05.其他问题说明




