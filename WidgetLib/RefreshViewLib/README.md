# RecycleView封装库
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.列表性能优化



### 01.基础概念介绍
- 关于RecyclerView，用途十分广泛，大概结构如下所示
	* RecyclerView.Adapter - 处理数据集合并负责绑定视图
	* ViewHolder - 持有所有的用于绑定数据或者需要操作的View
	* LayoutManager - 负责摆放视图等相关操作
	* ItemDecoration - 负责绘制Item附近的分割线
	* ItemAnimator - 为Item的一般操作添加动画效果，如，增删条目等
- Adapter扮演的角色
    - 一是，根据不同ViewType创建与之相应的的Item-Layout；二是，访问数据集合并将数据绑定到正确的View上




### 02.常见思路和做法



### 03.Api调用说明




### 04.遇到的坑分析



### 05.列表性能优化














