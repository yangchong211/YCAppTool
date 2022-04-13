> 本文已授权 微信公众号 **玉刚说** （[@任玉刚](https://blog.csdn.net/singwhatiwanna/)）独家发布。

> **争取打造 Android Jetpack 讲解的最好的博客系列**：
>* [Android官方架构组件Lifecycle：生命周期组件详解&原理分析](https://www.jianshu.com/p/b1208012b268)
>* [Android官方架构组件ViewModel:从前世今生到追本溯源](https://www.jianshu.com/p/59adff59ed29)
>* [Android官方架构组件Paging：分页库的设计美学](https://www.jianshu.com/p/10bf4bf59122)
>* [Android官方架构组件Navigation：大巧不工的Fragment管理框架](https://www.jianshu.com/p/ad040aab0e66)
>* [实战：使用MVVM尝试开发Github客户端及对编程的一些思考](https://www.jianshu.com/p/b03710f19123)


本文中我将尝试分享我个人 **搭建个人MVVM项目** 的过程中的一些心得和踩坑经历，以及在这过程中目前对 **编程本质** 的一些个人理解和感悟，特此分享以期讨论及学习进步。

## 缘由

最近在尝试搭建自己理解的 **MVVM模式** 的应用程序，在这近一个月中，我思考了很多，也参考了若干Github上MVVM项目源码，并从中获益匪浅。

我根据所得搭建了一个MVVM开发模式的Github客户端，并托管在了自己的github上：

[MVVM-Rhine:  MVVM+Jetpack的Github客户端](https://github.com/qingmei2/MVVM-Rhine)

创建这个项目的原因是我想有一个自己写的 **Github客户端** 方便我查看，目前我基本实现了自己的目标，App整体的效果是这样的：

![](https://upload-images.jianshu.io/upload_images/7293029-e52cb98c3b2285e2.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![](https://upload-images.jianshu.io/upload_images/7293029-cfbf6509442541c5.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

在开发过程中，我根据自己对于编程的理解，在技术选型中，加了一些自己喜欢的库，写了一些自己比较满意的风格的代码，特此和大家一起分享我的所得，谬误之处，欢迎拍砖。

### 1.我为什么选择Kotlin? 

回顾近半年来，我博客中的编程语言使用的清一色是 **Kotlin**，这样做的最初目的是督促自己学习Kotlin。

我曾在 **某篇文章** 中这样声明我用**Kotlin**的原因：

![](https://upload-images.jianshu.io/upload_images/7293029-cab60337e0142239.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

不仅如此，Kotlin语言国外已经有相当的热度了，只是目前相比Java，国内还没有完全推广起来而已。

此外，Kotlin的一些特性能够让我们实现Java实现不了的东西（不是空安全，无需findViewById这些基本的语法糖），对于某些设计点，Kotlin是Java无法替代的，这点我会在后文中提到。

### 2.MVVM的本质：异步观察者模式

很多朋友对RxJava的理解是 **链式调用**、**线程切换** 等等，对我来说，在RxJava的逐渐使用过程中，我对它的理解慢慢趋于 **异步** 一词——**RxJava** 强迫开发者从思想上将**异步代码**和**同步代码**归于一统，对于任何业务功能，都可以抽象为一个可观察的对象。

MVVM的本质亦是如此，**DataBinding** 帮我们为 **数据驱动视图** 提供了可实现的方案，因此它成为了大多数MVVM项目中的核心库。

MVVM观察者模式的本质也意味着，即使没有**DataBinding**，我们通过RxJava或者其他方式也能够实现 MVVM，只不过**DataBinding**更方便搭建MVVM而已。

这里不拿MVC、MVP和MVVM进行比较，因为不同的架构思想，都有不同的优劣势，我非常沉迷于RxJava和其优秀的思想，**我认为它的思想相当一部分和MVVM不谋而合，因此我更倾向使用MVVM**，配合以RxJava，能够让代码更加赏心悦目。

### 3.Android Jetpack: Architecture Components

Android Jetpack（下称Jetpack） 是Google今年IO大会上正式推出官方的新一代 **组件、工具和架构指导** ，旨在加快开发者的 **Android** 应用开发速度:

![](https://upload-images.jianshu.io/upload_images/7293029-061f5d645fd0c7a4.jpeg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1000/format/webp)

这是一套非常迷人的架构组件，Google今年还同步（其实晚了2个月）开源了一个Jetpack的示例项目 [Sunflower](https://github.com/googlesamples/android-sunflower)。

这个示例项目有着丰富的学习价值，也很方便开发者迅速上手并熟悉Jetpack的组件——当然，只是上手当然满足不了我的需求，我想通过自己参与一个项目的实践来深入了解并感受这些组件，于是 **我在这个项目中使用了这些组件**：

![](https://upload-images.jianshu.io/upload_images/7293029-12bb84f5d0901d1b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

我简单通过个人感受分别阐述一下这些组件真正融入MVVM项目中的感受：

### 3.1 DataBinding

MVVM的 **核心组件**，通过良好的设计，我的项目中避免了95%以上的 **冗余代码**—— 它的作用简单直接，就是 **数据驱动视图**，我再也不需要去通过控件设置UI，相反，所有UI的变动都交给了 **被观察的成员属性** 去驱动。

![](https://upload-images.jianshu.io/upload_images/7293029-9b7c105f944556c5.gif?imageMogr2/auto-orient/strip)

View的点击事件：

```XML
<ImageView
    android:id="@+id/btnEdit"
    android:layout_width="40dp"
    android:layout_height="40dp"
    android:src="@drawable/ic_edit_pencil"
    app:bind_onClick="@{ () -> delegate.edit() }" />
```
ImageView的url加载：

```XML
<ImageView
    android:id="@+id/ivAvatar"
    android:layout_width="80dp"
    android:layout_height="80dp"
    app:bind_imageUrl_circle="@{ delegate.viewModel.user.avatarUrl }" />
```
TextView的设置值：

```XML
<TextView
    android:id="@+id/tvNickname"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="@{ delegate.viewModel.user.name }" />
```

有同学觉得这太简单，那我们换一些有说服力的。

你还在 `Activity` 代码配置 `RecyclerView`？直接xml里一次性配置`RecyclerView`，包括 **滑动动画**，**下拉刷新**，**点击按钮列表滑动到顶部**：

```XML
<android.support.v4.widget.SwipeRefreshLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:onRefreshListener="@{ () -> delegate.viewModel.queryUserRepos() }"  // 刷新监听
    app:refreshing="@{ safeUnbox(delegate.viewModel.loading) }">    // 刷新状态

    <android.support.v7.widget.RecyclerView
        android:id="@+id/recyclerView"
        app:bind_adapter="@{ delegate.viewModel.adapter }"   // 绑定Adapter
        app:bind_scrollStateChanges="@{ delegate.fabViewModel.stateChangesConsumer }"
        app:bind_scrollStateChanges_debounce="@{ 500 }"
        app:layoutManager="android.support.v7.widget.LinearLayoutManager"
        tools:listitem="@layout/item_repos_repo" />

</android.support.v4.widget.SwipeRefreshLayout>

<android.support.design.widget.FloatingActionButton
    android:id="@+id/fabTop"
    android:src="@drawable/ic_keyboard_arrow_up_white_24dp"
    app:bind_onClick="@{ () -> recyclerView.scrollToPosition(0) }"    // 点击事件，列表直接回到顶部
    app:layout_constraintBottom_toBottomOf="parent"
    app:layout_constraintEnd_toEndOf="parent" />
```

还在配置 ViewPager+Fragment+BottomNavigationView的切换效果，包括**ViewPager滑动切换监听，自动配置Adapter，BottomNavigation的点击监听**, 我们都在Xml声明好，交给DataBinding就行了：

```XML
<android.support.v4.view.ViewPager
    android:id="@+id/viewPager"
    app:onViewPagerPageChanged="@{ (index) -> delegate.onPageSelectChanged(index) }"
    app:viewPagerAdapter="@{ delegate.viewPagerAdapter }"
    app:viewPagerDefaultItem="@{ 0 }"
    app:viewPagerPageLimit="@{ 2 }" />

<android.support.design.widget.BottomNavigationView
    android:id="@+id/navigation"
    app:bind_onNavigationBottomSelectedChanged="@{ (menuItem) -> delegate.onBottomNavigationSelectChanged(menuItem) }" 
    app:itemBackground="@color/colorPrimary"
    app:itemIconTint="@drawable/selector_main_bottom_nav_button"
    app:itemTextColor="@drawable/selector_main_bottom_nav_button"
    app:menu="@menu/menu_main_bottom_nav" />
```

> 篇幅所限，省略了一些常见的属性，上述的所有源码，你都可以在我的项目中找到。

我的意思不是想说 **DataBinding** 多么强大（它确实可以实现足够多的功能），对我而言，它最强大的好处是—— 节省了足够多UI控件的设置代码，让我能够  **抽出更多时间去写纯粹业务逻辑的代码。**

有朋友觉得DataBinding最大的问题就是不好Debug，我的解决方案是统一 **状态管理**，这个后文再提。

### 3.2 Lifecycle

**Lifecycle** 让我能够更专注于 **业务逻辑** 而非 **生命周期**，我认为这是不可代替的，如果你熟悉 **Lifecycle**,你可以看我的这篇文章：

[Android官方架构组件Lifecycle:生命周期组件详解&原理分析](https://www.jianshu.com/p/b1208012b268)

Lifecycle能够让我想要的组件也拥有 **生命周期**（实际上是对生命周期容器的观察），比如，我不再需要让Activity或者Fragment在`onCreated()`中去请求网络，取而代之的是：

```KOTLIN
class LoginViewModel(private val repo: LoginDataSourceRepository) : BaseViewModel() {

  override fun onCreate(lifecycleOwner: LifecycleOwner) {
          super.onCreate(lifecycleOwner)

          // 自动登录
          autoLogin.toFlowable()
                .filter { it }
                .doOnNext { login() }
                .bindLifecycle(this)
                .subscribe()
    }
}
```

> 上文的示例代码展示了，Login界面的自动登录逻辑（当然也可以是网络请求展示数据的逻辑），ViewModel检测到了Activity的生命周期并自动调用了`onCreate()`函数——我并没有通过Activity去调用它。

### 3.3 ViewModel

ViewModel能够检测到持有者的 **生命周期**，并避免了 **横竖屏切换时额外的代码的配置**，它的内部是通过一个不可见的 **Fragment** 对数据进行持有，并在真正该销毁数据的时候去销毁它们。

同时，它是MVVM中的 **核心组件**，我在项目的规范定义中，layout中所有的属性配置都应该依赖于`ViewModel`中的`MutableLiveData`属性:

```KOTLIN
class LoginViewModel(
        private val repo: LoginDataSourceRepository
) : BaseViewModel() {
 
    val username: MutableLiveData<String> = MutableLiveData()  // 用户名输入框
    val password: MutableLiveData<String> = MutableLiveData()  // 密码输入框

    val loading: MutableLiveData<Boolean> = MutableLiveData()   // ProgressBar
    val error: MutableLiveData<Option<Throwable>> = MutableLiveData()  // Errors

    val userInfo: MutableLiveData<LoginUser> = MutableLiveData()   // 用户信息

    private val autoLogin: MutableLiveData<Boolean> = MutableLiveData() // 是否自动登录

    // ......
}
```

### 3.4 LiveData

参照 **RxJava** 丰富的生态圈,  **LiveData** 看起来似乎实在鸡肋，但是**DataBinding**在最近的版本中提供了对 **LiveData** 的支持，考虑再三，我采用了 **LiveData**，正如上文示例代码，配合以 **ViewModel**, UI完整的驱动系统被搭建起来。

LiveData并非一无是处，它确实值得我作为依赖添加进自己的项目中，原因有二：

* 原生支持 DataBinding 和 Room

实际上 `Paging` 也是支持的，但是我没有用到`Paging`。

* 安全的数据更新

`RxJava`在子线程进行UI的更新依赖于 `observerOn(AndroidSchedudler.mainThread())`，但是`LiveData`不需要，你只需要通过 `postValue()`，就能安全的进行数据更新，就像这样:

```kotlin
 val loading: MutableLiveData<Boolean> = MutableLiveData()

this.loading.postValue(value)    // 数据的设置会在主线程上
```

但是我仍然需要面临一个问题，就是`LiveData`的生态圈实在没办法和 `RxJava` 相关的库对比，想要通过`LiveData`的操作符进行业务处理实在不靠谱，因此我选择将`LiveData`的`observe()`变成`RxJava`的`Flowable`：

```KOTLIN
private val autoLogin: MutableLiveData<Boolean> = MutableLiveData()

 autoLogin.toFlowable()   // 变成了一个Flowable
                .filter { it }
                .doOnNext { login() }
                .bindLifecycle(this)
                .subscribe()
```

得益于 **kotlin** 强大的 **扩展函数**，两者之间的融合如 **丝滑般的流畅**：

```KOTLIN
fun <T> LiveData<T>.toFlowable(): Flowable<T> = Flowable.create({ emitter ->
    val observer = Observer<T> { data ->
        data?.let { emitter.onNext(it) }
    }
    observeForever(observer)

    emitter.setCancellable {
        object : MainThreadDisposable() {
            override fun onDispose() = removeObserver(observer)
        }
    }
}, BackpressureStrategy.LATEST)
```

现在，我们一边享受着 `LiveData` **安全的数据更新和DataBinding的原生支持**，一边享受 `RxJava` 无以伦比 **强大的操作符和函数式编程思想**，这简直让我如沐春风。

### 3.5 Room

ORM数据库，市面上太多了不解释，我选择使用它的原因有二：

* 1.Google爸爸官方出品，无脑用
* 2.原生支持`RxJava`和`LiveData`, 无脑用

**真香。**

### 3.6 Navigation

Google官方 **单Activity多Fragment** 的架构组件，如果你不是很熟悉，可以参考这篇文章：

[Android官方架构组件Navigation：大巧不工的Fragment管理框架](https://www.jianshu.com/p/ad040aab0e66)

很感谢文章吹来之后，很多同学对文章的肯定，我也相信很多同学已经熟悉甚至尝试上手了这个库，我这次尝试在项目中使用它，原因是，我想试试 **它是不是真的像我文章吹的那么好用**。

经实战，初步结果是：

> 可以用，但没必要。

![](https://upload-images.jianshu.io/upload_images/7293029-90d4e10a1ab04ffb.gif?imageMogr2/auto-orient/strip)

在大多数情况下，`Navigation`都显得非常稳健，但是 **框架是死的，但是需求是千变万化的**，我总是不可避免去面对一些问题：

* 1.官方提供了`Navigation`对 `Toolbar` 和 `BottomNavigationView`的原生支持，但是令我哭笑不得的是，`Navigation`内部对`Fragment`的切换采用的是`replace()`,这意味着，每次点击底部导航控件，我都会销毁当前的`Fragment`，并且实例化一个新的`Fragment`。

* 2.很多APP采用了Home界面，双击返回才会退出Application的需求，正常我们可以重写Activity的`onBackPress()`方法，而使用了`Navigation`，我们不得不把导航的返回行为委托给了`Navigation`：

```KOTLIN
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override val layoutId = R.layout.activity_main

    override fun onSupportNavigateUp(): Boolean =
            findNavController(R.id.navHostFragment).navigateUp()

     // ...
}
```

当然，这些问题都是有解决方案的，以`BottomNavigationView`每次切换都会销毁当前`Fragment`并实例化新的`Fragment`为例，我的建议是：

> 对根布局的View使用`Navigation`，界面内部的布局采用常规实现方式（比如ViewPager+Fragment）。

比如我在MainActivity中声明`NavHostFragment`:

```xml
    <android.support.constraint.ConstraintLayout
        android:id="@+id/container"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <fragment
            android:id="@+id/navHostFragment"
            android:name="androidx.navigation.fragment.NavHostFragment"
            android:layout_width="0dp"
            android:layout_height="0dp"
            app:defaultNavHost="true"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintRight_toRightOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            app:navGraph="@navigation/navigation_main" />

    </android.support.constraint.ConstraintLayout>
```
我的`BottomNavigationView`导航界面,则是一个MainFragment：

```XML
<android.support.constraint.ConstraintLayout
    android:id="@+id/container"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <android.support.v4.view.ViewPager
        android:id="@+id/viewPager"
        android:layout_width="0dp"
        android:layout_height="0dp"" />

    <android.support.design.widget.BottomNavigationView
        android:id="@+id/navigation"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        app:menu="@menu/menu_main_bottom_nav" />

</android.support.constraint.ConstraintLayout>
```
我保证 **只有根布局的页面通过Navigation进行导航**，至于`Navigation`对`BottomNavigationView`的原生支持，我选择无视......

总而言之，对于是否使用`Navigation`，我的建议是持保守态度，因为这个东西和其它三方库不同，`Navigation`的配置是 **项目级** 的。

## 4. 天马行空：RxJava

关于项目中RxJava相关库的配置，我选择了这些：

![](https://upload-images.jianshu.io/upload_images/7293029-7bbfa201625bc0cf.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

我是`RxJava`的重度依赖使用者，它让我沉迷于 **业务逻辑的抽象**，尝试将所有代码归 **异步** 于一统，因此我依赖了这些库。

## 5. 依赖注入：Kodein

编程的乐趣在于 **探索**，对于Android开发者来说，**Dagger2** 可能会是更多开发者的首选，但对于一个 **探索性质更多** 的项目来说，**Dagger2** 并不是最优选，最终我选择了Kodein：

[Kodein官网：Painless Kotlin Dependency Injection](https://github.com/Kodein-Framework/Kodein-DI)

如果您完整的阅读了 **《Kotlin 实战》**这本书，你能在书末的附录中找到选择它的原因：

> 常见的Java依赖注入框架，比如 Spring/Guide/Dagger，都能很好地和Kotlin一起工作，如果你对原生的Kotin方案感兴趣，试试 [Kodein](https://github.com/Kodein-Framework/Kodein-DI), 它 **提供了一套漂亮的DSL来配置依赖，而且它的实现也非常高效。**

总结一下我个人的感受：

* 更Kotlin，整个框架都由Kotlin实现
* 实现方式依赖于 Kotlin 的 **属性委托**
* 很简洁，相比复杂的Dagger，上手更简单
* 超级漂亮的DSL && 说出去更唬人......

以 **Http网络请求** 相关为例，来看看依赖注入的代码：

![](https://upload-images.jianshu.io/upload_images/7293029-712445a8585b5560.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

很漂亮，对吧？

当然，对于依赖注入库，**Dagger2**是一个不会错的选择，但是如果仅仅只是个人项目，或者您已经厌倦了Dagger的配置，**Kodein**是一个不错的建议。

如果你对 **Kodein** 感兴趣，可以参考这篇文章，参考本文的项目代码，相信很快就能上手：

[告别Dagger2，Android的Kotlin项目中使用Kodein进行依赖注入](https://www.jianshu.com/p/b0da805f7534)

## 6.函数式支持库：Arrow

对于Kotlin的各种优点，**函数是第一等公民** 是一个无法忽视的闪光点，它与其他简单的语法糖不同，它能够让你的代码更加优雅。

Arrow是提供了一些简单函数式编程的特性，利用Arrow提供的各种各样的函子，你的代码可以更加简洁并且优雅。

比如，配合`RxJava`，你可以实现这样的代码以避免各种分支的处理，比如随时都有可能的`if..else()`，并将这些额外的操作放在最终的操作符中（Terminal Operator）去处理：

```kotlin
interface ILoginLocalDataSource : ILocalDataSource {

    fun fetchPrefsUser(): Flowable<Either<Errors, LoginEntity>>
}

class LoginLocalDataSource(
        private val database: UserDatabase,
        private val prefs: PrefsHelper
) : ILoginLocalDataSource {

    override fun fetchPrefsUser(): Flowable<Either<Errors, LoginEntity>> =
            Flowable.just(prefs)
                    .map {
                        when (it.username.isNotEmpty() && it.password.isNotEmpty()) {
                            true -> Either.right(LoginEntity(1, it.username, it.password))
                            false -> Either.left(Errors.EmptyResultsError)
                        }
                    }
}
```

现在我们将特殊的分支（数据错误）也同样像正常的流程一样交给了 `Either<Errors, LoginEntity>`统一返回,只有我们在真正需要使用它们时，它们才会被解析：

```Kotlin
 fun login() {
        when (username.value.isNullOrEmpty() || password.value.isNullOrEmpty()) {
            true -> applyState(isLoading = false, error = Errors.EmptyInputError.some())
            false -> repo
                    .login(username.value!!, password.value!!)   // 返回的是 Flowable<Either<Errors, LoginUser>>
                    .compose(globalHandleError()) 
                    .map { either ->      // 用到的时候再处理它
                        either.fold({
                            SimpleViewState.error<LoginUser>(it)
                        }, {
                            SimpleViewState.result(it)
                        })
                    }
                    .startWith(SimpleViewState.loading())
                    .startWith(SimpleViewState.idle())
                    .onErrorReturn { it -> SimpleViewState.error(it) }
                    .bindLifecycle(this)
                    .subscribe { state ->
                        // ...
                    }
        }
    }
```

在函数式编程的领域，我只是一个满怀敬意且不断学习探索的新人，但是它的好处在于，即使没有完全理解 **函数式编程** 的思想，我也可以通过运用一些简单的函子写出更加Functional的代码。

## 7. 其他库

除上述库之外，我还引用了目前比较优秀的三方库：

![](https://upload-images.jianshu.io/upload_images/7293029-87f9305f64885831.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

基于OkHttp的 网络请求库**Retrofit**，不赘述。

![](https://upload-images.jianshu.io/upload_images/7293029-1a09f5282b0a181e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

Glide 和 Timber，已经被大众所熟知的 **图片加载库** 和 小巧精致的 **日志打印库**，不赘述。

[DslAdapter](https://github.com/Yumenokanata/DslAdapter) 是低调的[Yumenokanata](https://github.com/Yumenokanata)开发的**RecyclerViewAdapter**，API的DSL设计加上对 **DataBinding** 的支持，我认为我还远远没达到写这个库的水平，因此在阅读完源码之后，我选择使用它。

## 8. 面向工具编程：模版插件

无论是MVP还是MVVM，对于一种开发模式而言，代码规范是很重要的，这意味着界面的实现总是需要用 **同一种开发模式** 进行规范化。

以MVP为例，标准的MVP，实现一个Activity的容器页面，我们需要定义`Contract`和其对应的`View`，`Presenter`,`Model`层的接口及其实现类，这就引发了另外一个问题，类似这种死板的开发模式的流程是否太繁琐（即简单的界面是否就没写这么多接口类的必要）？

我不这样认为，模版代码意味着开发的规范，这在团队开发中尤其重要，这样能够**保证项目品质的稳定性和一致性**，并且**便于扩展**，对于繁琐的生成重复性模版代码的情况，我认为MVP的代表性框架 [MVPArms](https://github.com/JessYanCoding/MVPArms)做出了非常值得学习的方案，即配置[模版插件](https://github.com/JessYanCoding/MVPArmsTemplate)。

因此我也花了一点时间配置了一套属于自己MVVM开发模式的[模版插件](https://github.com/qingmei2/MVVM-Rhine-Template),对于每个界面的初始化，可以很方便一键生成：

![](https://upload-images.jianshu.io/upload_images/7293029-749ca76b25cf86c3.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


![](https://upload-images.jianshu.io/upload_images/7293029-bd45b3368a33e13d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![](https://upload-images.jianshu.io/upload_images/7293029-058f16487e722645.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

就这样几步，Activity/Fragment，ViewModel,ViewDelegate以及依赖注入的KodeinModule类，都通过模版插件自动生成，我只需要关注UI的绘制和业务逻辑的编写即可。

无论是哪种开发模式，我认为模版插件都是一个能大大提高开发效率的工具，而且它的学习成本并不高，以我个人经验，即使没有相关经验，也只需要3～4小时，就能开发出一套属于自己的模版插件。

## 9.没有使用的一些尝试

### 9.1 组件化/模块化开发

从我个人经验来看，对于简单的项目并不需要进行复杂的模块化配置，因为开发者和维护者也只有我一个人。

### 9.2 Paging和WorkManager

这两个也是 Android Jetpack 的架构组件，但我并没有使用它们。

`Paging`是一个优秀的库，我曾举出它的优点（参考我的[这篇文章](https://www.jianshu.com/p/10bf4bf59122)），但是正如有朋友提到的，它的缺点很明显，那就是`Paging`本身是对`RecyclerView.Adapter`的继承，这意味着使用了`Paging`，就必须抛弃其他的`Adapter`库，或者自己造轮子，最终我选择了搁置。

`WorkManager`的原因就很简单了，项目中的功能暂时用不到它....

### 9.3 事件总线

说到事件总线，国内比较容易被提及的有 `EventBus`和`RxBus`，此外之前还看到某位大佬曾经分享过 `LiveDataBus`，印象很深刻，但是文章找不到了。

没有采用事件总线的原因是，我已经有`RxJava`了。

有同学说既然你有`RxJava`,为什么不使用`RxBus`呢，因为对于依赖来说并没有额外的负担？

对此我推荐这篇文章[放弃RxBus，拥抱RxJava:为什么避免使用EventBus/RxBus](https://www.jianshu.com/p/61631134498e)。

引用文章中作者[@W_BinaryTree](https://www.jianshu.com/u/a502db06530c)对Jake Wharton对RxBus的评价翻译：

![](https://upload-images.jianshu.io/upload_images/7293029-c1ae1b364ec3b6e1.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

> [W_BinaryTree](https://www.jianshu.com/u/a502db06530c)的相关文章写的都很有深度，我读完很受启发，冒昧推荐一下这位作者。

我认为`RxJava`本身就是对**发布-订阅者模式**最优秀的体现，我尽量保证我的工程中处处都由`RxJava`去串联就够了。

于我个人而言，我完全赞同没有引入`RxJava`的项目中使用`EventBus`,但是我确实不推荐`RxBus`，因为这意味着业务模块之间层级设计得不清晰，才会导致全部交由`RxJava`中全局的`Subject`的订阅情况的产生。

### 9.4 协程

协程的整体替换也在我下一步的学习计划中。

这需要一段时间的发展，因为我认为目前协程还没有发展足够的生态环境——我更期待更多类似 [retrofit2-kotlin-coroutines-adapter](https://github.com/JakeWharton/retrofit2-kotlin-coroutines-adapter)这样优秀的拓展库，能够让我下决定把所有RxJava的代码给替换掉。

目前项目中，`Room`，网络请求以及`Databinding`依赖的`LiveData`，都是通过`RxJava`进行编织串在一起的，这些代码糅合很深，因此`Kotlin1.3`发布后（协程从实验性的功能正式Release），我只先尝试性的使用了类似 `Result` 这样的API在异常处理上代替`Arrow`的`Either`, 而协程则处于观察状态。

此外，我还没有开始深入学习协程，从新手角度来看，可能还需要一段时间学习深入并理解它，因此我期待更多关于协程的分析和相关分享的文章。

## 10.关于状态管理

状态的管理一直是争论不休的话题，甚至基于**状态管理**还引申了 **MVI** (Model-View-Intent)的开发模式,关于MVI中文相关的博客我推荐这篇文章：

[从状态管理(State Manage)到MVI（Model-View-Intent）](https://www.jianshu.com/p/f84f083a65eb)

这是一篇分析非常透彻的文章，阅读之如饮甘怡，其中最重要的优势便是对状态额统一管理，读后收获甚丰，并做出了一些实验性的尝试，篇幅所限，不再赘述，详情请参考 [项目中ViewModel](https://github.com/qingmei2/MVVM-Rhine) 的源码。

## 11.感受

MVVM模式和设计理念相关博客已经烂大街了，而且我也不认为我能够讲的比别人更透彻。

我写本文的原因是分享自己对于编程本质的理解，于我对编程的认知，探索过程中所带来的**乐趣**和**成就感**才是最重要的，追究本质可能是**探索**和**创造**。

我不喜欢拘泥于**固定的开发模式**，日复一日的重复操作让我想起了**工厂的流水线**，编程不同，每个人的代码风格的迥异背后代表着思想的碰撞，这是很多工作不能给予我的。

回顾本文，我希望本文的每一小节都能给您带来有益的东西，它可能是一种积极状态的传递，也可能某小节涉及的知识点让您感兴趣，或是其他——项目本身意义和**这种收获** 相比反而不大，因为每个人的思想不同，对于MVVM的理解也不同。

因此，我不敢妄言这个项目代表了MVVM的规范，但至少目前我对它的设计很满意（对您来说可能嘈点满满），它代表了我是这一阶段持续学习的结果，，**很期待不久之后的我能够用怀疑的眼光去看待这个项目，那将意味着下一阶段的进步。**

项目地址：https://github.com/qingmei2/MVVM-Rhine

**------------------------------------------------------------------广告分割线----------------------------------------------------------------------**

## 关于我

Hello，我是[却把清梅嗅](https://github.com/qingmei2)，如果您觉得文章对您有价值，欢迎 ❤️，也欢迎关注我的[博客](https://www.jianshu.com/u/df76f81fe3ff)或者[Github](https://github.com/qingmei2)。

如果您觉得文章还差了那么点东西，也请通过**关注**督促我写出更好的文章——万一哪天我进步了呢？


