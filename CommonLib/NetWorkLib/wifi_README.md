# Wifi功能库组件
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.其他问题说明



### 01.基础概念说明
#### 1.0 业务场景说明
- Wi-Fi连接业务场景
    - 扫描二维码自动连接WiFi的功能。识别二维码解析数据，拿到Wi-Fi的ssid和pwd，然后连接网络。



#### 1.1 Wi-Fi相关文档
- Wi-Fi相关文档
    - https://source.android.com/docs/core/connect/wifi-overview?hl=zh-cn
- API相关文档
    - https://developer.android.com/reference/android/net/wifi/WifiManager.html
- 其他的一些文档
    * WifiManager : https://developer.android.com/reference/android/net/wifi/WifiManager.html
    * WifiConfiguration : https://developer.android.com/reference/android/net/wifi/WifiConfiguration.html
    * ScanResult : https://developer.android.com/reference/android/net/wifi/ScanResult.html
    * WifiInfo : https://developer.android.com/reference/android/net/wifi/WifiInfo.html



#### 1.2 WLAN基础功能
- Android Wi-Fi 框架可帮助用户连接到优质 Wi-Fi 网络（在有可用 Wi-Fi 网络且需要连接到这类网络的情况下）。Android 通过两种方式来实现这一功能：
    - 自动开启 Wi-Fi：当用户靠近之前保存的网络时重新开启 Wi-Fi
    - 附近有公共网络时发出通知：当有可用优质开放 Wi-Fi 网络时向用户发出通知
- 自动开启 Wi-Fi
    - 用户可能会出于各种原因停用 Wi-Fi（例如，所连接的网络不稳定），但在回家后可能忘记重新启用 Wi-Fi，从而导致体验不佳。
    - Android 9 中引入的“自动开启 Wi-Fi”功能解决了这一问题：只要设备靠近已保存（即用户过去明确连接过）且 RSSI 足够高的 Wi-Fi 网络，便会自动重新启用 Wi-Fi。
    - 如何设置自动开启Wi-Fi：设置 > 网络和互联网 > Wi-Fi > Wi-Fi 偏好设置 > 自动开启 Wi-Fi
- 附近有公共网络时发出通知。只要出现以下情况，“开放网络通知”功能便会向用户发出通知：
    - WLAN 已启用；设备未连接到 Wi-Fi 网络；有开放且 RSSI 足够高的 Wi-Fi 网络（与内部 Wi-Fi 选择算法使用相同 RSSI 阈值）
    - 启用或停用该功能：设置 > 网络和互联网 > 互联网 > 网络偏好设置 > 附近有公共网络时发出通知


#### 1.3 分配MAC地址行为
- 为什么要随机分配MAC地址
    - 随机分配 MAC 地址功能通过在连接到 WLAN 网络时使用随机分配的 MAC 地址，可以加强用户隐私保护。
    - 设备在连接到 WLAN 网络或接入点时会使用 MAC 地址。由于这些 MAC 地址未经加密即被传输，因此可能会被捕获并用于跟踪用户的位置。
    - 过去，设备使用出厂 MAC 地址连接到 WLAN 网络。出厂 MAC 地址在全球范围内是唯一的静态地址，可以跟踪和逐个识别设备。
- 随机分配 MAC 地址类型
    - Android 框架使用两种类型的随机分配 MAC 地址功能：永久性随机分配和非永久性随机分配。
    - 如果用户停用随机分配 MAC 地址功能，则系统会使用出厂 MAC 地址。



#### 1.4 Wi-Fi种类说明
- Wi-Fi种类共有哪些类型
    - 按照是否开放分为：开放网络和加密网络。按照类型分为：Wi-Fi无线网；热点；
    - 开放式网络：开放式网络是最简单的WiFi网络类型，不需要密码。
    - WEP：WEP是一种较老的加密协议，容易被攻击。有线等效加密。
    - WPA：WPA是较新的加密协议，更加安全。有分成家用的WPA-PSK (Pre-Shared Key)与企业用的WPA-Enterprise版本。
    - WPA2：WPA2顾名思义就是WPA的加强版，WPA2与WPA的差别在于，它使用更安全的加密技术AES (Advanced Encryption Standard)，因此比WPA更难被破解、更安全。
    - Web Redirection：连接Wi-Fi后，开启浏览器尝试上网，并强制重导到认证网页要求输入账号密码，然后ACG向RADIUS认证服务器来确认使用者的身分，认证通过才可以自由到其它的网站。
- 什么叫做Wi-Fi直连(也叫点对点连接)
    - 多个设备连接同一个Wi-Fi，然后通过Wi-Fi进行直接通信。从而获得比蓝牙连接更远距离的高速连接通信效果。
- 如何获取Wi-Fi的类型
    - 用ScanResult.capabilities来获取认证方式。capabilities的格式是（认证标准+秘钥管理+加密方案）。


#### 1.5 连接Wi-Fi的流程
- 注意事项
    - 请注意！此代码在Android10 API29的版本上已经无法使用 在新的API 29的SDK中 enableNetwork() 方法始终会返回false， 如果你非要使用可以考虑将API降到28。
- 流程比较长，大致是这样的:
    - 1.搜索WiFi,找到指定WiFi 获取名称/地址/加密方式,以及你自己知道的WiFi密码
    - 2.创建WiFi配置信息WifiConfiguration, 添加WiFi名称,地址
    - 3.在根据加密方式以对应的方式添加密码到WifiConfiguration
    - 4.将WiFi配置WifiConfiguration,添加到以配置的网络列表里addNetwork(wifiConfiguration);
    - 5.获取已经配置好的网络列表mWifiManager.getConfiguredNetworks();,找到指定WiFi,获取id
    - 6.断开现在正在连接的WiFi,输入id启用设置好的WiFi,重新连接



### 02.常见思路和做法
#### 2.1 连接Wi-Fi方式
- 连接Wi-Fi目前可能的方式
    - 给定WiFi名称，连接无密码的网络
    - 给定WiFi名称 和密码，连接网络 （需指定密码类型 wpa 或者web ）
- WEB 和 WPA 有何区别？
    - WEP 代表有线等效隐私。它是世界上普遍使用的Wi-Fi安全协议。WEP 旨在支持类似于有线网络的保护级别。用户可以将他们的连接点设置为打开或使用共享密钥。将其设置为 open 允许任何链接到网络的人，而共享密钥使用密码来验证用户是否已获得认证。
    - WPA是继承了WEP基本原理而又解决了WEP缺点的一种新技术。由于加强了生成加密密钥的算法，因此即便收集到分组信息并对其进行解析，也几乎无法计算出通用密钥。



#### 2.2 开启Wi-Fi操作
- 开启WiFi一般需要一定的时间，因此不能立马去连接，需要等WiFi稳定可用，经测试一般2.5秒即可完成。



#### 2.3 连接Wi-Fi步骤
- 连接Wi-Fi需要用到的核心API
    - addNetwork(WifiConfiguration config)，添加 WifiConfiguration
    - enableNetwork(int netId, boolean attemptConnect)，启用并尝试连接到 wifi
- 第一步：判断并开启Wi-Fi
    - 判断当前手机wifi网络是否开启可用。如果不可用，则需要首先打开Wi-Fi开关。
    - 打开关闭wifi使用wifiManager.setWifiEnabled()的方法，打开关闭前，先要判断wifi的状态，使用isWifiEnabled()方法。
- 第二步：根据Wi-Fi名称获取/创建WifiConfiguration
    - 连接已经配置过wifi，首先检查是否已经配置过该wifi，通过WifiManager获取已经配置的wifi列表。传入要连接的wifi的SSID。
    - 对于连接已经配置过的wifi，这里遍历的结果返回一个WifiConfiguration的对象，拿来实现连接。
- 第三步：将网络配置添加到管理器中
    - 将wifiConfig配置添加到wifiManager管理器中。
- 第四步：判断是否连接上Wi-Fi
    - 判断是否连接到Wi-Fi的状态。判断是否保持配置，是否连接上网络等条件。



#### 2.4 扫描Wi-Fi
- 如何开启扫描Wi-Fi操作
    - 使用 WifiManager.startScan() 请求扫描。
- 如何去拿扫描之后的结果
    - 为 SCAN_RESULTS_AVAILABLE_ACTION 注册一个广播监听器，系统会在完成扫描请求时调用此监听器，提供其成功/失败状态。
    - 使用 WifiManager.getScanResults() 获取扫描结果。系统返回的扫描结果为最近更新的结果。
- 遇到的问题说明
    - 扫描后，拿到的Wi-Fi数据，有很多是ssid重复的。那么这样该如何处理过滤呢？
- 关于Wi-Fi扫描一些说明
    - 具体可以看：https://developer.android.google.cn/guide/topics/connectivity/wifi-scan?hl=zh-cn



#### 2.5 获取连接Wi-Fi信息
- 如何获取连接Wi-Fi的信息
    - 通过 ConnectivityManager 的 getActiveNetworkInfo() 方法获取到当前连接的网络信息；
    - 通过 WifiManager 的 getConnectionInfo() 方法获取当前连接的 WIFI 信息；根据当前 WIFI 的 networkId 获取到当前 WIFI 的配置和状态。




#### 2.7 监听Wi-Fi广播
- ConnectivityManager.CONNECTIVITY_ACTION
    - "android.net.conn.CONNECTIVITY_CHANGE"，网络连接发生变化时的广播，如：建立连接或断开连接等
- WifiManager.CONFIGURED_NETWORKS_CHANGED_ACTION
    - "android.net.wifi.CONFIGURED_NETWORKS_CHANGE"，已配置的 WIFI 网络发生变化时的广播，如：添加，更新或删除等
- WifiManager.LINK_CONFIGURATION_CHANGED_ACTION
    - "android.net.wifi.LINK_CONFIGURATION_CHANGED"，WIFI 链接配置发生变化时的广播
- WifiManager.NETWORK_STATE_CHANGED_ACTION
    - "android.net.wifi.STATE_CHANGE"，WIFI 的连接状态发生变化时的广播
- WifiManager.SUPPLICANT_STATE_CHANGED_ACTION
    - "android.net.wifi.supplicant.STATE_CHANGE"，与 WIFI 建立连接的状态发生变化时的广播，如：密码错误等



### 03.Api调用说明



### 04.遇到的坑分析
#### 4.1 Wi-Fi减少耗电量
- 背景说明
    - Wi-Fi 首选分流网络 (PNO) 扫描是在设备与 Wi-Fi 断开连接且屏幕关闭后定期发生的低电耗 Wi-Fi 扫描。PNO 扫描用于查找并连接到已保存的网络。
    - 在搭载 Android 9 或更低版本的设备上，当设备与 Wi-Fi 断开连接且屏幕关闭后，PNO 扫描将以 20 秒为间隔执行前三次扫描，并以 60 秒为间隔执行所有后续扫描。找到已保存的网络或屏幕开启时，PNO 扫描会停止。
- 如何减少耗电量
    - Android 10 在 WifiManager 中引入了一个名为 setDeviceMobilityState() 的可选 API 方法，该方法可根据设备的移动状态增加 PNO 扫描的间隔时长，从而减少耗电量。
    - 如果设备处于静止状态，Android 框架会将 PNO 扫描的间隔时长从 60 秒增加到 180 秒，以减少耗电量。这种优化基于以下假设：在设备没有移动时，设备不可能通过 PNO 扫描找到任何新网络。


#### 4.2 Wi-Fi连接失败
- 举一个实际场景
    - 使用ssid和pwd连接某个Wi-Fi网络，连接成功。这个时候关掉，然后重新连接，则会频繁出现连接不上的问题。目前该问题已经存在，也是一个历史问题！



#### 4.3 Wi-Fi判断无效
- 先看一个实际场景
    - 连接上Wi-Fi为Tencent-GuessWifi【这个网络是认证网络】后，来看一下一些api获取的结果
    - isWifiConnected，判断结果为true，表示Wi-Fi已经连接上；
    - isWifiAvailable，判断结果为true，表示Wi-Fi是可用的状态；
    - isConnected，获取当前活跃网络，判断结果为true，表示当前活跃网络是可用状态；
    - isWifiEnable()，判断Wi-Fi是否是打开状态，判断结果是true
- 然后结果是怎么样的
    - 结果是，Tencent-GuessWifi是连接上了，但是这个Wi-Fi无法请求网络，可以ping通，但是连接不了外网，比如无法访问百度新闻。
    - 这个是为什么呢，原因是因为该网络需要使用手机号+验证码，验证之后才可以上网。**需要用户手动操作**！！
- 调研发现
    - 暂时通过api无法连接这种Wi-Fi认证网络，难度系数大。关注点：大多数网络通过密码即可连接上，关注大众即可！
- 注意事项
    - 不支持企业Wi-Fi验证密码这类




### 05.其他问题说明



