#### 目录介绍
# 图片压缩库
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


#### 1.4 ViewPager2封装
- ViewPager2是用来替换ViewPager的
    - ViewPager2是final修饰的，直接继承ViewGroup，其内部是使用RecyclerView，ViewPager2默认是使用懒加载。
- ViewPager2与ViewPager的改进：
    - （1）支持Right to Left布局，即从右向左布局；（2）支持竖向滚动；（3）支持notifyDataSetChanged；（4）支持懒加载
- api上的变动如下：
    - （1）FragmentStateAdapter替换了原来的 FragmentStatePagerAdapter；（2）RecyclerView.Adapter替换了原来的 PagerAdapter；（3）registerOnPageChangeCallback替换了原来的 addPageChangeListener



### 02.常见思路和做法
#### 2.3 Fragment懒加载
- 先说下业务背景：TabLayout+ViewPager+Fragment是开发常用的组合。ViewPager的默认机制就是把全部Fragment都加载出来，而为了保障一些用户体验，使用懒加载的Fragment，就是在用户可见这个Fragment之后才处理业务逻辑。
- 目前该场景懒加载大概思路如下：当页面可见的时候，才加载当前页面。没有打开的页面，就不会预加载网络，说白了懒加载就是可见的时候才去请求数据。



### 03.Api调用说明



### 04.遇到的坑分析



### 05.该库性能分析






- 如何提高Message的优先级
    - https://mp.weixin.qq.com/s/nOcI2J6yUW-vTNy-VuZufQ
















