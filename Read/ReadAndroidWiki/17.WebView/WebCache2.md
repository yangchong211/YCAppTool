#### 使用目录介绍
- 11.自身构建缓存步骤
- 12.实现原理分析
- 13.资源请求拦截详细内容
- 14.交给OkHttp缓存
- 15.缓存达到最大处理


### 01.自身构建缓存步骤
- 需求场景
    - 缓存web页面
- 大概的思路
    - 通过拦截webView中渲染网页过程中各种资源（包括图片、js文件、css样式文件、html页面文件等）的下载，根据业务的场景考虑缓存的策略
- 实现步骤
    - 事先将更新频率较低、常用 & 固定的H5静态资源 文件（如JS、CSS文件、图片等） 放到本地
    - 拦截H5页面的资源网络请求 并进行检测
    - 如果检测到本地具有相同的静态资源 就 直接从本地读取进行替换 而 不发送该资源的网络请求 到 服务器获取
- 拦截处理
    - 在shouldInterceptRequest方法中拦截处理
    - 步骤1:判断拦截资源的条件，即判断url里的图片资源的文件名
    - 步骤2:创建一个输入流，这里可以先从内存中拿，拿不到从磁盘中拿，再拿不到就从网络获取数据
    - 步骤3:打开需要替换的资源(存放在assets文件夹里)，或者从lru中取出缓存的数据
    - 步骤4:替换资源
- 有几个问题
    - 如何判断url中资源是否需要拦截，或者说是否需要缓存
    - 如何缓存js，css等
    - 缓存数据是否有时效性
    - 关于缓存下载的问题，是引入okhttp还是原生网络请求，缓存下载失败该怎么处理



### 02.实现原理分析
- webView在加载网页的时候，用户能够通过系统提供的API干预各个中间过程。我们要拦截的就是网页资源请求的环节。这个过程，WebViewClient当中提供了以下两个入口：
    ```java
    // android5.0以上的版本加入
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
        return super.shouldInterceptRequest(webView, webResourceRequest);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView webView, String s) {
        return super.shouldInterceptRequest(webView, s);
    }
    ```
- 上面的两个API是在调用了WebView#loadUrl()之后，请求网页资源（包括html文件、js文件、css文件以及图片文件）的时候回调。关于这两个API有几个点需要注意：
    - 回调不是发生在主线程，因此不能做一些处理UI的事情
    - 接口的返回值是同步的
    - WebResourceResponse这个返回值可以自行构造，其中关键的属性主要是：代表资源内容的一个输入流InputStream以及标记这个资源内容类型的mMimeType
- 替换资源操作
    - 只要在这两个入口构造正确的WebResourceResponse对象，就可以替换默认的请求为我们提供的资源
    - 因此，在每次请求资源的时候根据请求的URL/WebResourceRequest判断是否存在本地的缓存，并在缓存存在的情况下将缓存的输入流返回



### 03.资源请求拦截详细内容
- 核心任务就是拦截资源请求，下载资源并缓存资源，因此拦截缓存的设计就分为了下面三个核心点：
    - 请求拦截
    - 资源响应（下载/读取缓存）
    - 缓存
- 请求拦截
    - 先判断是否需要缓存，然后从Url获取文件扩展名extension，从扩展中获取Mime类型，当mine类型为空时则不缓存
    - 根据对应的资源请求定义是否参与拦截、以及选择性的自定义配置下载和缓存的行为
- 资源响应（下载/读取缓存）
    - 资源响应有两种情况：
        - 缓存响应
        - 下载响应
    - 当对应的资源缓存不存在的时候，会直接触发资源的下载。在类库内部，会通过HttpConnectionDownloader直接建立一个HttpURLConnection进行资源的下载，获得资源的文件流。
- 缓存
    -



### 14.交给OkHttp缓存





### 15.缓存达到最大处理










