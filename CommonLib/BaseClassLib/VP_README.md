# Vp控件封装库
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.该库性能分析



### 01.基础概念介绍
#### 1.1 该库简单的介绍
- 通用PagerAdapter封装
    - 方便快速tabLayout+ViewPager。针对页面较少的Fragment选择使用BaseFragmentPagerAdapter，针对页面较多的Fragment选择使用BasePagerStateAdapter。
- BaseLazyFragment懒加载fragment
    - 就是等到该页面的UI展示给用户时，再加载该页面的数据(从网络、数据库等)。
- BaseVisibilityFragment
    - fragment是否可见封装类，比如之前需求在页面可见时埋点就需要这个。弥补了setUserVisibleHint遇到的bug……
- ViewPager2封装库
    - DiffFragmentStateAdapter，可以用作diff操作的适配器；ViewPagerDiffCallback用来做diff计算的工具类


#### 1.3 ViewPager介绍
- 三种Adapter的缓存策略
    - PagerAdapter：缓存三个，通过重写instantiateItem和destroyItem达到创建和销毁view的目的。
    - FragmentPagerAdapter：内部通过FragmentManager来持久化每一个Fragment，在destroyItem方法调用时只是detach对应的Fragment，并没有真正移除！
    - FragmentPagerStateAdapter：内部通过FragmentManager来管理每一个Fragment，在destroyItem方法，调用时移除对应的Fragment。
- 三个Adapter使用场景分析
    - PagerAdapter：当所要展示的视图比较简单时适用
    - FragmentPagerAdapter：当所要展示的视图是Fragment，并且数量比较少时适用
    - FragmentStatePagerAdapter：当所要展示的视图是Fragment，并且数量比较多时适用


#### 1.4 ViewPager2介绍
- ViewPager2是用来替换ViewPager的
    - ViewPager2是final修饰的，直接继承ViewGroup，其内部是使用RecyclerView，ViewPager2默认是使用懒加载。
- ViewPager2与ViewPager的改进：
    - （1）支持Right to Left布局，即从右向左布局；（2）支持竖向滚动；（3）支持notifyDataSetChanged；（4）支持懒加载
- ViewPager2相关api上的变动如下：
    - （1）FragmentStateAdapter替换了原来的 FragmentStatePagerAdapter；（2）RecyclerView.Adapter替换了原来的 PagerAdapter；（3）registerOnPageChangeCallback替换了原来的 addPageChangeListener



### 02.常见思路和做法
#### 2.3 ViewPager封装设计
- 首先看下ViewPager的特点？
    - ViewPager有预加载机制，即默认情况下当前页面左右两侧的1个页面会被加载，以方便用户滑动切换到相邻的界面时，可以更加顺畅的显示出来。通过ViewPager的setOffscreenPageLimit(int limit)可以设置预加载页面数量，当前页面相邻的limit个页面会被预加载进内存。
- 为何要有ViewPager2这个控件？
    - ViewPager2内部是通过RV实现的。但是对于Fragment的处理有单独的Adapter实现。
- ViewPager2如何实现局部刷新操作？
    - RecyclerView基于DiffUtil可以实现局部更新，FragmentStateAdapter也可以对Fragment实现局部更新。
- ViewPager2预加载说明
    - 建议：如果页面具有复杂的布局，应该将这个限制保持在较低的水平。当前页面的前后（limit数）页面会被添加到视图层次结构中，即使它是不可见的，超出limit数将会从视图删除，但会像RecyclerView一样被回收。
- ViewPager2遇到的坑
    - 在FragmentStateAdapter中有一个带有多个片段的viewpager2，每当我尝试打开一个新片段，然后使用viewpager2返回到当前片段时，都会出现异常。Expected the adapter to be 'fresh' while restoring state.



### 03.Api调用说明



### 04.遇到的坑分析
#### 4.1 ViewPager事件崩溃
- 抛出的异常：onInterceptTouchEvent(DetailViewPager.java:36)，基本上是一堆的源代码异常堆栈信息。
- 异常分析：类似此种系统性异常，尽可能得去捕获了。
- 解决办法：重写onTouchEvent和onInterceptTouchEvent方法，然后捕获异常。具体可以看：[SecureViewPager]()
  




### 05.该库性能分析






















