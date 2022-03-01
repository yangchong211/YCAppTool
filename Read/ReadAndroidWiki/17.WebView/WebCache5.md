## OkHttp缓存介绍
#### 目录介绍
- 01.为什么要用缓存
- 02.Cache类介绍
- 03.OkHttp缓存实现




### 01.OkHttp缓存介绍
- CacheInterceptor，在建立连接之前，我们检查响应是否已经被缓存、缓存是否可用，如果是则直接返回缓存的数据，否则就进行后面的流程，并在返回之前，把网络的数据写入缓存。
- 这块代码比较多，但也很直观，主要涉及 HTTP 协议缓存细节的实现，而具体的缓存逻辑 OkHttp 内置封装了一个Cache类，它利用DiskLruCache，用磁盘上的有限大小空间进行缓存，按照 LRU 算法进行缓存淘汰，这里也不再展开。



### 02.Cache类介绍
#### 2.1 基本特征
- 代码介绍
    ```java
    private final DiskLruCache cache;

    public Cache(File directory, long maxSize) {
        this(directory, maxSize, FileSystem.SYSTEM);
      }
    Cache(File directory, long maxSize, FileSystem fileSystem) {
        this.cache = DiskLruCache.create(fileSystem, directory, VERSION, ENTRY_COUNT, maxSize);
    }
    ```
- 通过上面代码可知
- 1、Cache对象拥有一个DiskLruCache引用。
- 2、Cache构造器接受两个参数，意味着如果我们想要创建一个缓存必须指定缓存文件存储的目录和缓存文件的最大值


#### 2.2 换成处理
- 既然是Cache，那么一定会有"增"、"删"、"改"、"查"。
- (1) ”增“操作——put()方法
    ```java
    CacheRequest put(Response response) {
        String requestMethod = response.request().method();
        //判断请求如果是"POST"、"PATCH"、"PUT"、"DELETE"、"MOVE"中的任何一个则调用DiskLruCache.remove(urlToKey(request));将这个请求从缓存中移除出去。
        if (HttpMethod.invalidatesCache(response.request().method())) {
          try {
            remove(response.request());
          } catch (IOException ignored) {
            // The cache cannot be written.
          }
          return null;
        }
        //判断请求如果不是Get则不进行缓存，直接返回null。官方给的解释是缓存get方法得到的Response效率高，其它方法的Response没有缓存效率低。通常通过get方法获取到的数据都是固定不变的的，因此缓存效率自然就高了。其它方法会根据请求报文参数的不同得到不同的Response，因此缓存效率自然而然就低了。
        if (!requestMethod.equals("GET")) {
          // Don't cache non-GET responses. We're technically allowed to cache
          // HEAD requests and some POST requests, but the complexity of doing
          // so is high and the benefit is low.
          return null;
        }
         //判断请求中的http数据包中headers是否有符号"*"的通配符，有则不缓存直接返回null
        if (HttpHeaders.hasVaryAll(response)) {
          return null;
        }
        //由Response对象构建一个Entry对象,Entry是Cache的一个内部类
        Entry entry = new Entry(response);
        //通过调用DiskLruCache.edit();方法得到一个DiskLruCache.Editor对象。
        DiskLruCache.Editor editor = null;
        try {
          editor = cache.edit(key(response.request().url()));
          if (editor == null) {
            return null;
          }
          //把这个entry写入
          //方法内部是通过Okio.buffer(editor.newSink(ENTRY_METADATA));获取到一个BufferedSink对象，随后将Entry中存储的Http报头数据写入到sink流中。
          entry.writeTo(editor);
          //构建一个CacheRequestImpl对象，构造器中通过editor.newSink(ENTRY_BODY)方法获得Sink对象
          return new CacheRequestImpl(editor);
        } catch (IOException e) {
          abortQuietly(editor);
          return null;
        }
    }
    ```
    - 总结一下上述步骤
    - 第一步，先判断是不是一个正常的请求(get,post等)
    - 第二步，由于只支持get请求，非get请求直接返回
    - 第三步，通配符过滤
    - 第四步，通过上述检验后开始真正的缓存流程，new一个Entry
    - 第五步，获取一个DiskLruCache.Editor对象
    - 第六步，通过DiskLruCache.Edito写入数据
    - 第七步，返回数据
    - PS：关于key()方法在remove里面详解。上面使用到了remove方法，莫非就是"删"的操作，那咱们来看下
- (2) ”删“操作——remove()方法
    ```
      void remove(Request request) throws IOException {
        cache.remove(key(request.url()));
      }
      public static String key(HttpUrl url) {
        return ByteString.encodeUtf8(url.toString()).md5().hex();
      }
    ```
    - 果然remove就是传说中的"删除"操作，key()这个方法原来就说获取url的MD5和hex生成的key
- (3) ”改“操作——update()方法
    ```java
    void update(Response cached, Response network) {
        //用response构造一个Entry对象
        Entry entry = new Entry(network);
        //从命中缓存中获取到的DiskLruCache.Snapshot
        DiskLruCache.Snapshot snapshot = ((CacheResponseBody) cached.body()).snapshot;
        //从DiskLruCache.Snapshot获取DiskLruCache.Editor()对象
        DiskLruCache.Editor editor = null;
        try {
          editor = snapshot.edit(); // Returns null if snapshot is not current.
          if (editor != null) {
            //将entry写入editor中
            entry.writeTo(editor);
            editor.commit();
          }
        } catch (IOException e) {
          abortQuietly(editor);
        }
    }
    ```
    - 根据上述代码大体流程如下：
    - 第一步，首先要获取entry对象
    - 第二步，获取DiskLruCache.Editor对象
    - 第三步，写入entry对象
- (4) ”查“操作——get()方法
    ```java
    Response get(Request request) {
        //获取url经过MD5和HEX的key
        String key = key(request.url());
        DiskLruCache.Snapshot snapshot;
        Entry entry;
        try {
         //根据key来获取一个snapshot，由此可知我们的key-value里面的value对应的是snapshot
          snapshot = cache.get(key);
          if (snapshot == null) {
            return null;
          }
        } catch (IOException e) {
          // Give up because the cache cannot be read.
          return null;
        }
        //利用前面的Snapshot创建一个Entry对象。存储的内容是响应的Http数据包Header部分的数据。snapshot.getSource得到的是一个Source对象 (source是okio里面的一个接口)
        try {
          entry = new Entry(snapshot.getSource(ENTRY_METADATA));
        } catch (IOException e) {
          Util.closeQuietly(snapshot);
          return null;
        }

        //利用entry和snapshot得到Response对象，该方法内部会利用前面的Entry和Snapshot得到响应的Http数据包Body（body的获取方式通过snapshot.getSource(ENTRY_BODY)得到）创建一个CacheResponseBody对象；再利用该CacheResponseBody对象和第三步得到的Entry对象构建一个Response的对象，这样该对象就包含了一个网络响应的全部数据了。
        Response response = entry.response(snapshot);
        //对request和Response进行比配检查，成功则返回该Response。匹配方法就是url.equals(request.url().toString()) && requestMethod.equals(request.method()) && OkHeaders.varyMatches(response, varyHeaders, request);其中Entry.url和Entry.requestMethod两个值在构建的时候就被初始化好了，初始化值从命中的缓存中获取。因此该匹配方法就是将缓存的请求url和请求方法跟新的客户请求进行对比。最后OkHeaders.varyMatches(response, varyHeaders, request)是检查命中的缓存Http报头跟新的客户请求的Http报头中的键值对是否一样。如果全部结果为真，则返回命中的Response。
        if (!entry.matches(request, response)) {
          Util.closeQuietly(response.body());
          return null;
        }
        return response;
    }
    ```
    - 总结上面流程大体是：
    - 第一步 获取key
    - 第一步 获取DiskLruCache.Snapshot对象
    - 第三步 获取Entry对象
    - 第四步 获取response
    - 第五步 检查是response
- 通过对上述增删改查的分析，我们可以得出如下结论
    ```java
    方法	返回值
    DiskLruCache.get(String)	可以获取DiskLruCache.Snapshot
    DiskLruCache.remove(String)	可以移除请求
    DiskLruCache.edit(String)	可以获得一个DiskLruCache.Editor对象
    DiskLruCache.Editor.newSink(int)	可以获得一个sink流
    DiskLruCache.Snapshot.getSource(int)	可以获取一个Source对象。
    DiskLruCache.Snapshot.edit()	可以获得一个DiskLruCache.Editor对象
    ```



### 03.OkHttp缓存实现
#### 3.1 原理和注意事项
- 1、原理
    - (1)、okhttp的网络缓存是基于http协议，不清楚请仔细看上一篇文章
    - (2)、使用DiskLruCache的缓存策略，具体请看本片文章的第一章节
- 2、注意事项：
    - 1、目前只支持GET，其他请求方式需要自己实现。
    - 2、需要服务器配合，通过head设置相关头来控制缓存
    - 3、创建OkHttpClient时候需要配置Cache


#### 3.2 流程
- 流程如下所示
    - 1、如果配置了缓存，则从缓存中取出(可能为null)
    - 2、获取缓存的策略.
    - 3、监测缓存
    - 4、如果禁止使用网络(比如飞行模式),且缓存无效，直接返回
    - 5、如果缓存有效，使用网络，不使用网络
    - 6、如果缓存无效，执行下一个拦截器
    - 7、本地有缓存、根据条件判断是使用缓存还是使用网络的response
    - 8、把response缓存到本地


#### 3.3 源码对比
- 代码如下所示
    ```java
    @Override public Response intercept(Chain chain) throws IOException {
        //1、如果配置了缓存，则从缓存中取出(可能为null)
        Response cacheCandidate = cache != null
            ? cache.get(chain.request())
            : null;

        long now = System.currentTimeMillis();
        //2、获取缓存的策略.
        CacheStrategy strategy = new CacheStrategy.Factory(now, chain.request(), cacheCandidate).get();
        Request networkRequest = strategy.networkRequest;
        Response cacheResponse = strategy.cacheResponse;
        //3、监测缓存
        if (cache != null) {
          cache.trackResponse(strategy);
        }

        if (cacheCandidate != null && cacheResponse == null) {
          closeQuietly(cacheCandidate.body()); // The cache candidate wasn't applicable. Close it.
        }

        // If we're forbidden from using the network and the cache is insufficient, fail.
          //4、如果禁止使用网络(比如飞行模式),且缓存无效，直接返回
        if (networkRequest == null && cacheResponse == null) {
          return new Response.Builder()
              .request(chain.request())
              .protocol(Protocol.HTTP_1_1)
              .code(504)
              .message("Unsatisfiable Request (only-if-cached)")
              .body(Util.EMPTY_RESPONSE)
              .sentRequestAtMillis(-1L)
              .receivedResponseAtMillis(System.currentTimeMillis())
              .build();
        }
        //5、如果缓存有效，使用网络，不使用网络
        // If we don't need the network, we're done.
        if (networkRequest == null) {
          return cacheResponse.newBuilder()
              .cacheResponse(stripBody(cacheResponse))
              .build();
        }

        Response networkResponse = null;
        try {
         //6、如果缓存无效，执行下一个拦截器
          networkResponse = chain.proceed(networkRequest);
        } finally {
          // If we're crashing on I/O or otherwise, don't leak the cache body.
          if (networkResponse == null && cacheCandidate != null) {
            closeQuietly(cacheCandidate.body());
          }
        }
        //7、本地有缓存、根据条件判断是使用缓存还是使用网络的response
        // If we have a cache response too, then we're doing a conditional get.
        if (cacheResponse != null) {
          if (networkResponse.code() == HTTP_NOT_MODIFIED) {
            Response response = cacheResponse.newBuilder()
                .headers(combine(cacheResponse.headers(), networkResponse.headers()))
                .sentRequestAtMillis(networkResponse.sentRequestAtMillis())
                .receivedResponseAtMillis(networkResponse.receivedResponseAtMillis())
                .cacheResponse(stripBody(cacheResponse))
                .networkResponse(stripBody(networkResponse))
                .build();
            networkResponse.body().close();

            // Update the cache after combining headers but before stripping the
            // Content-Encoding header (as performed by initContentStream()).
            cache.trackConditionalCacheHit();
            cache.update(cacheResponse, response);
            return response;
          } else {
            closeQuietly(cacheResponse.body());
          }
        }
        //这个response是用来返回的
        Response response = networkResponse.newBuilder()
            .cacheResponse(stripBody(cacheResponse))
            .networkResponse(stripBody(networkResponse))
            .build();
        //8、把response缓存到本地
        if (cache != null) {
          if (HttpHeaders.hasBody(response) && CacheStrategy.isCacheable(response, networkRequest)) {
            // Offer this request to the cache.
            CacheRequest cacheRequest = cache.put(response);
            return cacheWritingResponse(cacheRequest, response);
          }

          if (HttpMethod.invalidatesCache(networkRequest.method())) {
            try {
              cache.remove(networkRequest);
            } catch (IOException ignored) {
              // The cache cannot be written.
            }
          }
        }
        return response;
    }
    ```


#### 3.4 倒序具体分析
- 1、什么是“倒序具体分析”？
    - 这里的倒序具体分析是指先分析缓存，在分析使用缓存，因为第一次使用的时候，肯定没有缓存，所以肯定先发起请求request，然后收到响应response的时候，缓存起来，等下次调用的时候，才具体获取缓存策略。
- 2、先分析获取响应response的流程,保存的流程是如下
    - 在CacheInterceptor的代码是
    ```java
    if (cache != null) {
      if (HttpHeaders.hasBody(response) && CacheStrategy.isCacheable(response, networkRequest)) {
        // Offer this request to the cache.
        CacheRequest cacheRequest = cache.put(response);
        return cacheWritingResponse(cacheRequest, response);
      }
    }
    ```
    - 核心代码是CacheRequest cacheRequest = cache.put(response);cache就是咱们设置的Cache对象，put(reponse)方法就是调用Cache类的put方法
    ```java
    Entry entry = new Entry(response);
    DiskLruCache.Editor editor = null;
    try {
      editor = cache.edit(key(response.request().url()));
      if (editor == null) {
        return null;
      }
      entry.writeTo(editor);
      return new CacheRequestImpl(editor);
    } catch (IOException e) {
      abortQuietly(editor);
      return null;
    }
    ```
    - 先是用resonse作为参数来构造Cache.Entry对象，这里强烈提示下，是Cache.Entry对象，不是DiskLruCache.Entry对象。 然后 调用的是DiskLruCache类的edit(String key)方法，而DiskLruCache类的edit(String key)方法调用的是DiskLruCache类的edit(String key, long expectedSequenceNumber)方法,在DiskLruCache类的edit(String key, long expectedSequenceNumber)方法里面其实是通过lruEntries的 lruEntries.get(key)方法获取的DiskLruCache.Entry对象，然后通过这个DiskLruCache.Entry获取对应的编辑器,获取到编辑器后， 再次这个编辑器(editor)通过okio把Cache.Entry写入这个编辑器(editor)对应的文件上。注意，这里是写入的是http中的header的内容 ，最后 返回一个CacheRequestImpl对象
- 紧接着又调用了 CacheInterceptor.cacheWritingResponse(CacheRequest, Response)方法。主要就是通过配置好的cache写入缓存，都是通过Cache和DiskLruCache来具体实现。
- 总结：缓存实际上是一个比较复杂的逻辑，单独的功能块，实际上不属于OKhttp上的功能，实际上是通过是http协议和DiskLruCache做了处理。










