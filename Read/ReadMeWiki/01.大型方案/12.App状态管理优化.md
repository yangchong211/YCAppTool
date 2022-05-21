#### 目录介绍
- 01.整体概述
    - 1.1 项目背景
    - 1.2 思考问题
    - 1.3 常见方式
    - 1.4 设计目标
    - 1.5 收益分析
- 02.市面上方案对比
    - 2.1 include方式管理
    - 2.2 自定义LoadingView
- 03.封装设计思路
    - 3.1 自定义帧布局
    - 3.2 自定义状态管理器
    - 3.3 管理多种状态的集合
    - 3.4 用ViewStub布局
- 04.方案基础设计
    - 4.1 整体架构图
    - 4.2 UML设计图
    - 4.3 关键流程图
    - 4.4 接口设计图
    - 4.5 模块间依赖关系
- 03.其他设计
    - 3.1 性能设计
    - 3.2 稳定性设计
    - 3.3 灰度设计
    - 3.4 降级设计
    - 3.5 异常设计





### 01.整体概述
#### 1.1 项目背景
- 在Android中，不管是activity或者fragment，在加载视图的时候都有可能会出现多种不同的状态页面View。比如常见的就有这些：
    - 内容界面，也就是正常有数据页面
    - 加载数据中，加载loading
    - 加载数据错误，请求数据异常
    - 加载后没有数据，请求数据为空
    - 没有网络，网络异常
- 有的时候，产品童鞋，还会针对不同的页面，设计一些差异化的视图
    - 比如不同的页面，可能展示不同的无数据页面，那么需要支持自定义view
    - 比如，网络异常页面，可能添加一个按钮，点击去设置网络操作


### 1.2 思考问题
- 同时，思考一下几个问题。
    - 怎样切换界面状态？有些界面想定制自定义状态？状态如何添加点击事件？下面就为解决这些问题！
- 为何要这样？
    - 一般在加载网络数据时，需要用户等待的场景，显示一个加载的Loading动画可以让用户知道App正在加载数据，而不是程序卡死，从而给用户较好的使用体验。
    - 当加载的数据为空时显示一个数据为空的视图、在数据加载失败时显示加载失败对应的UI并支持点击重试会比白屏的用户体验更好一些。
    - 加载中、加载失败、空数据等不同状态页面风格，一般来说在App内的所有页面中需要保持一致，也就是需要做到全局统一。


### 1.3 常见方式


### 1.4 设计目标
- 让View状态的切换和Activity彻底分离开
    - 必须把这些状态View都封装到一个管理类中，然后暴露出几个方法来实现View之间的切换。
    - 在不同的项目中可以需要的View也不一样，所以考虑把管理类设计成builder模式来自由的添加需要的状态View。
- 那么如何降低耦合性，让代码入侵性低。方便维护和修改，且移植性强呢？大概具备这样的条件……
    - 可以运用在activity或者fragment中
    - 不需要在布局中添加LoadingView，而是统一管理不同状态视图，同时暴露对外设置自定义状态视图方法，方便UI特定页面定制
    - 支持设置自定义不同状态视图，即使在BaseActivity统一处理状态视图管理，也支持单个页面定制
    - 在加载视图的时候像异常和空页面能否用ViewStub代替，这样减少绘制，只有等到出现异常和空页面时，才将视图给inflate出来
    - 当页面出现网络异常页面，空页面等，页面会有交互事件，这时候可以设置点击设置网络或者点击重新加载等等



### 02.市面上方案对比
#### 2.1 采用include方式管理
- 直接把这些界面include到main界面中，然后动态去切换界面，具体一点的做法如下所示。
    - 在布局中，会存放多个状态的布局。然后在页面中根据逻辑将对应的布局给显示或者隐藏，但存在诸多问题。
    ```
    <LinearLayout>
        <!--正常时布局-->
        <include layout="@layout/activity_content"/>
        <!--加载loading布局-->
        <include layout="@layout/activity_loading"/>
        <!--异常时布局-->
        <include layout="@layout/activity_error"/>
        <!--空数据时布局-->
        <include layout="@layout/activity_emptydata"/>
    </LinearLayout>
    ```
- 存在的问题分析
    - 后来发现这样处理不容易复用到其他项目中，代码复用性很低。在activity中处理这些状态的显示和隐藏比较乱
    - 调用setContentView方法时，是将所有的布局给加载绘制出来。其实没有必要
    - 如果将逻辑写在BaseActivity中，利用子类继承父类特性，在父类中写切换状态，但有些界面如果没有继承父类，又该如何处理




### 03.自定义LoadingView
- 首先是定义一个自定义的控件，比如把它命名成LoadingView，然后在这个里面include一个布局，该布局包含一些不同状态的视图。代码思路如下所示：
    ```
    public class LoadingView extends LinearLayout implements View.OnClickListener {
        public static final int LOADING = 0;
        public static final int STOP_LOADING = 1;
        public static final int NO_DATA = 2;
        public static final int NO_NETWORK = 3;
        public static final int GONE = 4;
        public static final int LOADING_DIALOG = 5;
        //省略很多代码，主要写各种状态view的展示和隐藏
    }
    ```
- 然后在BaseActivity/BaseFragment中封装LoadingView的初始化逻辑，并封装加载状态切换时的UI显示逻辑，暴露给子类以下方法：
    ```
    void showLoading(); //调用此方法显示加载中的动画
    void showLoadFailed(); //调用此方法显示加载失败界面
    void showEmpty(); //调用此方法显示空页面
    void onClickRetry(); //子类中实现，点击重试的回调方法
    ```
    - 在BaseActivity/BaseFragment的子类中可通过上一步的封装比较方便地使用加载状态显示功能。这种使用方式耦合度太高，每个页面的布局文件中都需要添加LoadingView，使用起来不方便而且维护成本较高，比如说有时候异常状态的布局各个页面不同，那么难以自定义处理，修改起来成本较高。
    - 同时如果是要用这种状态管理工具，则需要在需要的页面布局中添加该LoadingView视图。这样也能够完成需求，但是感觉有点麻烦。
- 具体如何使用它进行状态管理呢？可以看到在对应的布局中需要写上LoadingView
    ```
    <RelativeLayout >
        <com.cheoo.app.view.recyclerview.TypeRecyclerView />
        <com.cheoo.app.view.LoadingView />
    </RelativeLayout>
    ```
- 那么，如果某个子类不想继承BaseActivity类，如何使用该状态管理器呢？代码中可以这种使用。
    ```
    mLoadingView  = (LoadingView)findViewById(R.id.mLoadingView);
    mLoadingView.setStatue(LoadingView.LOADING);
    mLoadingView.setStatue(LoadingView.STOP_LOADING);
    mLoadingView.setStatue(LoadingView.NO_NETWORK);
    mLoadingView.setStatue(LoadingView.NO_DATA);
    ```



### 03.封装设计思路
#### 3.1 自定义帧布局
- 首先需要自定义一个状态StateFrameLayout布局，它是继承FrameLayout。
    - 在这个类中，目前是设置五种不同状态的视图布局，主要的功能操作是显示或者隐藏布局。为了后期代码维护性，根据面向对象的思想，类尽量保证单一职责，所以关于状态切换，以及设置自定义状态布局，把这个功能分离处理，放到一个StateLayoutManager中处理。
    - 看代码可知，这个类的功能非常明确，就是隐藏或者展示视图作用。


#### 3.2 自定义状态管理器
- 上面状态的自定义布局创建出来了，而且隐藏和展示都做了。那么如何控制设置自定义视图布局，还有如何控制不同布局之间切换，那么就需要用到这个类呢！https://github.com/yangchong211/YCStateLayout
    - loadingLayoutResId和contentLayoutResId代表等待加载和显示内容的xml文件
    - 几种异常状态要用ViewStub，因为在界面状态切换中loading和内容View都是一直需要加载显示的，但是其他的3个只有在没数据或者网络异常的情况下才会加载显示，所以用ViewStub来加载他们可以提高性能。
    - 采用builder模式，十分简单，代码如下所示。创建StateFrameLayout对象，然后再设置setStatusLayoutManager，这一步操作是传递一个Manager对象到StateFrameLayout，建立连接。


#### 3.3 管理多种状态的集合
- 大约5种状态，如何管理这些状态？添加到集合中，Android中选用SparseArray比HashMap更省内存，在某些条件下性能更好，主要是因为它避免了对key的自动装箱（int转为Integer类型），它内部则是通过两个数组来进行数据存储的，一个存储key，另外一个存储value，为了优化性能，它内部对数据还采取了压缩的方式来表示稀疏数组的数据，从而节约内存空间
    ``` 
    /**存放布局集合 */
    private SparseArray<View> layoutSparseArray = new SparseArray();
    
    /**将布局添加到集合 */
    private void addLayoutResId(@LayoutRes int layoutResId, int id) {
      View resView = LayoutInflater.from(mStatusLayoutManager.context).inflate(layoutResId, null);
      layoutSparseArray.put(id, resView);
      addView(resView);
    }
    
    //那么哪里从集合中取数据呢
    public void showContent() {
        if (layoutSparseArray.get(LAYOUT_CONTENT_ID) != null) {
            showHideViewById(LAYOUT_CONTENT_ID);
        }
    }
    ``` 

#### 3.4 用ViewStub布局
- 什么是ViewStub
    - ViewStub 是一个看不见的，没有大小，不占布局位置的 View，可以用来懒加载布局。当 ViewStub 变得可见或 ```inflate()``` 的时候，布局就会被加载（替换 ViewStub）。
    - 因此，ViewStub 一直存在于视图层次结构中直到调用了 ```setVisibility(int)``` 或 ```inflate()```。在 ViewStub 加载完成后就会被移除，它所占用的空间就会被新的布局替换。
- 方法里面通过id判断来执行不同的代码，首先判断ViewStub是否为空，如果为空就代表没有添加这个View就返回false，不为空就加载View并且添加到集合当中，然后调用showHideViewById方法显示隐藏View，retryLoad方法是给重试按钮添加事件
    - 注意，即使当你设置了多种不同状态视图，调用setContentView的时候，因为异常页面使用ViewStub，所以在绘制的时候不会影响性能的。
    ``` 
    public void showNetWorkError() {
        if (inflateLayout(LAYOUT_NETWORK_ERROR_ID)) {
            showHideViewById(LAYOUT_NETWORK_ERROR_ID);
        }
    }
    
    //调用inflateLayout方法，方法返回true然后调用showHideViewById方法
    private boolean inflateLayout(int id) {
      boolean isShow = true;
      if (layoutSparseArray.get(id) != null) return isShow;
      switch (id) {
        case LAYOUT_NETWORK_ERROR_ID:
          if (mStatusLayoutManager.netWorkErrorVs != null) {
            View view = mStatusLayoutManager.netWorkErrorVs.inflate();
            layoutSparseArray.put(id, view);
            isShow = true;
          } else {
            isShow = false;
          }
          break;
      }
      return isShow;
    }
    ``` 
- ViewStub为何无大小？
    - draw和dispatchDraw虽然重写了，但是看代码却都是什么也不做!并且onMeasure还什么也不做，直接setMeasuredDimension(0,0)；来把view区域设置位0，原来一个ViewStub虽然是一个view，却是一个没有任何显示内容，也不显示任何内容的特殊view，并且对layout在加载时候不可见的。
- ViewStub为何不绘制
    - 在构造的时候，设置了setWillNotDraw(true)方法，看源码可知onDraw()不会被调用，通过略过绘制的过程，优化了性能。
- inflate使用特点
    - ViewStub只能被Inflate一次，inflate之后ViewStub对象就会被置为空。即某个被ViewStub指定的布局被Inflate后，就不能够再通过ViewStub来控制它了。
 

#### 6.2 处理重新加载逻辑
- 最后看看重新加载方法
    ``` 
    /**
    * 重试加载
    */
    private void retryLoad(View view, int id) {
      View retryView = view.findViewById(mStatusLayoutManager.retryViewId != 0 ? mStatusLayoutManager.retryViewId : id);
      if (retryView == null || mStatusLayoutManager.onRetryListener == null) return;
      retryView.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          mStatusLayoutManager.onRetryListener.onRetry();
        }
      });
    }
    ``` 


### 07.如何使用该封装库
- 可以自由切换内容，空数据，异常错误，加载，网络错误等5种状态。父类BaseActivity直接暴露5中状态，方便子类统一管理状态切换，这里fragment的封装和activity差不多。
    ``` 
    /**
    * ================================================
    * 作  者：杨充
    * 版  本：1.0
    * 创建日期：2017/7/6
    * 描  述：抽取类
    * 修订历史：
    * ================================================
    */
    public abstract class BaseActivity extends AppCompatActivity {
    
      protected StatusLayoutManager statusLayoutManager;
    
      @Override
      protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_view);
        initStatusLayout();
        initBaseView();
        initToolBar();
        initView();
      }
        
        //子类必须重写该方法
      protected abstract void initStatusLayout();
    
      protected abstract void initView();
    
      /**
      * 获取到布局
      */
      private void initBaseView() {
        LinearLayout ll_main = (LinearLayout) findViewById(R.id.ll_main);
        ll_main.addView(statusLayoutManager.getRootLayout());
      }
    
      //正常展示数据状态
      protected void showContent() {
        statusLayoutManager.showContent();
      }
    
      //加载数据为空时状态
      protected void showEmptyData() {
        statusLayoutManager.showEmptyData();
      }
    
      //加载数据错误时状态
      protected void showError() {
        statusLayoutManager.showError();
      }
    
      //网络错误时状态
      protected void showNetWorkError() {
        statusLayoutManager.showNetWorkError();
      }
    
      //正在加载中状态
      protected void showLoading() {
        statusLayoutManager.showLoading();
      }
    }
    ```
- 子类继承BaseActivity后，该如何操作呢？具体如下所示
    ```
    @Override
    protected void initStatusLayout() {
        statusLayoutManager = StateLayoutManager.newBuilder(this)
                .contentView(R.layout.activity_main)
                .emptyDataView(R.layout.activity_emptydata)
                .errorView(R.layout.activity_error)
                .loadingView(R.layout.activity_loading)
                .netWorkErrorView(R.layout.activity_networkerror)
                .build();
    }
    
    //或者添加监听事件
    @Override
    protected void initStatusLayout() {
      statusLayoutManager = StateLayoutManager.newBuilder(this)
          .contentView(R.layout.activity_content_data)
          .emptyDataView(R.layout.activity_empty_data)
          .errorView(R.layout.activity_error_data)
          .loadingView(R.layout.activity_loading_data)
          .netWorkErrorView(R.layout.activity_networkerror)
          .onRetryListener(new OnRetryListener() {
            @Override
            public void onRetry() {
              //为重试加载按钮的监听事件
            }
          })
          .onShowHideViewListener(new OnShowHideViewListener() {
            @Override
            public void onShowView(View view, int id) {
              //为状态View显示监听事件
            }
    
            @Override
            public void onHideView(View view, int id) {
              //为状态View隐藏监听事件
            }
          })
          .build();
    }
    
    //如何切换状态呢？
    showContent();
    showEmptyData();
    showError();
    showLoading();
    showNetWorkError();
    
    //或者这样操作也可以
    statusLayoutManager.showLoading();
    statusLayoutManager.showContent();
    ```
- 那么如何设置状态页面的交互事件呢？当状态是加载数据失败时，点击可以刷新数据；当状态是无网络时，点击可以设置网络。代码如下所示：
    ```
    /**
    * 点击重新刷新
    */
    private void initErrorDataView() {
      statusLayoutManager.showError();
      LinearLayout ll_error_data = (LinearLayout) findViewById(R.id.ll_error_data);
      ll_error_data.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          initData();
          adapter.notifyDataSetChanged();
          showContent();
        }
      });
    }
    
    /**
    * 点击设置网络
    */
    private void initSettingNetwork() {
      statusLayoutManager.showNetWorkError();
      LinearLayout ll_set_network = (LinearLayout) findViewById(R.id.ll_set_network);
      ll_set_network.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Intent intent = new Intent("android.settings.WIRELESS_SETTINGS");
          startActivity(intent);
        }
      });
    }
    ```
- 那有些页面想要自定义指定的状态页面UI，又该如何操作呢？倘若有些页面想定制状态布局，也可以自由实现，很简单：
    ```
    /**
    * 自定义加载数据为空时的状态布局
    */
    private void initEmptyDataView() {
      statusLayoutManager.showEmptyData();
      //此处是自己定义的状态布局
      statusLayoutManager.showLayoutEmptyData(R.layout.activity_emptydata);
      LinearLayout ll_empty_data = (LinearLayout) findViewById(R.id.ll_empty_data);
      ll_empty_data.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          initData();
          adapter.notifyDataSetChanged();
          showContent();
        }
      });
    }
    ``` 



### 其他介绍
#### 01.关于博客汇总链接
- 1.[技术博客汇总](https://www.jianshu.com/p/614cb839182c)
- 2.[开源项目汇总](https://blog.csdn.net/m0_37700275/article/details/80863574)
- 3.[生活博客汇总](https://blog.csdn.net/m0_37700275/article/details/79832978)
- 4.[喜马拉雅音频汇总](https://www.jianshu.com/p/f665de16d1eb)
- 5.[其他汇总](https://www.jianshu.com/p/53017c3fc75d)



#### 02.关于我的博客
- github：https://github.com/yangchong211
- 知乎：https://www.zhihu.com/people/yczbj/activities
- 简书：http://www.jianshu.com/u/b7b2c6ed9284
- csdn：http://my.csdn.net/m0_37700275
- 喜马拉雅听书：http://www.ximalaya.com/zhubo/71989305/
- 开源中国：https://my.oschina.net/zbj1618/blog
- 泡在网上的日子：http://www.jcodecraeer.com/member/content_list.php?channelid=1
- 邮箱：yangchong211@163.com
- 阿里云博客：https://yq.aliyun.com/users/article?spm=5176.100- 239.headeruserinfo.3.dT4bcV
- segmentfault头条：https://segmentfault.com/u/xiangjianyu/articles
- 掘金：https://juejin.im/user/5939433efe88c2006afa0c6e


### 项目开源地址：https://github.com/yangchong211/YCStateLayout







