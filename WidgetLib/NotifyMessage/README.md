#### 目录介绍
- 01.基础概念说明
- 02.常见思路和做法
- 03.Api调用说明
- 04.一些其他设计
- 05.其他问题说明



### 01.基础概念说明
#### 1.1 业务场景说明
- 业务场景说明
    - 针对Push通知，调用类似于系统悬浮通知栏样式的的弹窗，告知用户一些重要的消息。用户点击消息，浮窗可以自动消失。
- 产品使用场景
    - 用于通知消息提醒，运营活动。其实可以参考微信来了消息的悬浮弹窗！主要是引导用户关注，提供核心信息。



#### 1.2 业务目标说明
- 业务目标
    - 1.通知弹窗可以高度定制各种View(或者layout布局)
    - 2.通知弹窗可以设置自定义延迟消失(自动隐藏弹窗)，且暴露给外部开发者设置自动隐藏时间
    - 3.自定义通知弹窗，需要支持上滑动收起，需要支持可以常驻(用户触发关闭)
    - 4.多条消息通知弹窗，可以设置成覆盖，比如后面一个覆盖前面一个；也有可能是串行，弹出一个后再弹出下一个
- 关注业务交互
    - 1.悬浮的时机控制
    - 2.悬浮的位置，技术要实现动态化配置位置api
    - 3.悬浮的交互(主要是点击)，以及动画效果(主要是展现和隐藏的过渡动画)


#### 1.3 设计目标
- 复用性强
    - 可以快速在多个App中使用该通知弹窗，使用简单。且完全跟业务解耦合，该库只是专注于通知弹窗功能
- 可以完全自定义视图
    - 通知弹窗UI样式，可能在不同的业务场景展示效果不一样。这个时候可以支持自定义视图就很关键了
- 暴露强大的API给开发者
    - 可以支持弹出，设置自定义n秒后，自动消失逻辑。
    - 可以灵活设置是否需要上滑动隐藏通知弹窗。
    - 可以设置自定义View视图中按钮或者整个视图的点击事件，暴露给开发者实现业务逻辑。



### 02.常见思路和做法
#### 2.1 总体设计思路
- 设计思路的灵感
    - 其核心设计思路的灵感来自于手机系统的弹窗Notification的设计思路。
- 第一步设计接口：主要是悬浮窗
    - 1.需要设计悬浮通知弹窗常见操作，比如弹出展现，隐藏，判断是否已经弹出等。这样基于接口设计让弹窗功能一目了然！
    - 2.悬浮通知消失后，可能有一些业务逻辑需要处理，隐藏需要监听该事件，因此需要设计监听回调接口！
- 第二步添加View：主要是利用WMS
    - 1.这个就是创建WindowManager对象，然后设置相关属性，最后通过addView的形式将视图添加到窗口上
- 第三步处理滑动事件：主要是处理事件分发
    - 1.主要是处理是否上滑动隐藏弹窗。这里主要是先记录down按下时y轴的值，然后在move移动的时候记录y轴的值，最后计算差值。
    - 2.判断上滑动的距离如果是大于最小滑动距离，则自己消费事件。对事件的处理就是滑动隐藏悬浮弹窗
- 第四步处理动画：主要是展示或者隐藏动画
    - 1.展示通知弹窗，使用属性动画，调用translationY移动0即可从上往下弹出(目前动画时间200ms)。
    - 2.隐藏通知弹窗，使用属性动画，调用translationY移动负的控件View高度，等到监听动画结束后，再调用removeView移除视图。
- 第五步弹出后间隔n秒自动隐藏
    - 1.这种相当于在通知弹窗弹出的时候，发送一个延迟消息，然后再根据通知type去隐藏销毁指定的通知弹窗即可。


#### 2.2 设计接口说明
- 设计悬浮弹窗的接口：展现，隐藏等api
    ``` java
    public interface INotificationService<T> {
        void show(T notification);
        void cancel(T notification , Animator.AnimatorListener listener);
        boolean isShowing();
        void changeIsShowing(boolean isShowing);
    }
    ```
- 由于上滑动可以设置隐藏通知，在View中处理滑动逻辑，然后通过回调监听去修改Impl类中隐藏逻辑
    ``` java
    public interface OnDismissListener {
        void onDismiss();
    }
    ```
- 对外暴露API设计
    ``` java
    //通知类型（必须设置），相当于原声通知栏id唯一标示
    setType(52)
    //设置是否可被上划收起
    setCollapsible(false)
    //设置自动隐藏时间
    setTimeOut(3000)
    //设置显示优先级
    setPriority(100)
    //设置外部自定义View
    setNotificationView(new MyNotifyView(this))
    //设置数据，用于数据与View绑定
    setData(T data)
    //展示
    show()
    //取消
    cancel(int type)
    ```
- 针对外部开发者设置自定义View，抽取抽象类，主要作用是约束和复用部分逻辑
    - 具体逻辑可以看NotificationView，传入外部View记得继承该类。



#### 2.3 整体架构设计
- 整体架构设计UML类图
    - ![image](https://img-blog.csdnimg.cn/eae99c9f7355413d860c95eb35ee8d52.png)



#### 2.4 关键流程图


### 03.Api调用说明
#### 3.1 如何依赖
- 直接在maven中依赖如下所示：
    ```
    
    ```


#### 3.2 如何使用Api
- 创建通知栏并且展现
    ``` java
    new CustomNotification<Void>()
            .setType(52)
            .setCollapsible(false)
            .setTimeOut(3000)
            .setPriority(100)
            .setNotificationView(new MyNotifyView(this))
            .show();
    ```
- 隐藏具体类型的通知栏
    ``` java
    CustomNotification.cancel(52);
    ```
- 如何自定义View
    ``` java
    public class MyNotifyView extends NotificationView<Teacher> {
    
        public MyNotifyView(@NonNull Activity activity) {
            super(activity);
        }
    
        @Override
        public int provideLayoutResourceId() {
            return R.layout.notify_custom_view;
        }
    
        @Override
        public int[] provideClickableViewArray() {
            return new int[]{R.id.btn_click};
        }
    
        @Override
        protected boolean onClick(View view, int id) {
            if (id == R.id.btn_click) {
                ToastUtils.showRoundRectToast("点击吐司");
                return true;
            }
            return false;
        }
    
        @SuppressLint("SetTextI18n")
        @Override
        public void bindNotification(CustomNotification<NotificationActivity.Teacher> notification) {
            super.bindNotification(notification);
            Teacher data = notification.getData();
            TextView title = findViewById(R.id.tv_custom_title);
            if (data!=null){
                title.setText(data.name + data.age);
            }
        }
    }
    ```
- 案例UI的效果
    - ![image](https://img-blog.csdnimg.cn/f61d70e0a98c4010b13b9b0d6adb2faf.png)



### 04.一些其他设计
#### 4.1 性能上设计
- 展示和隐藏涉及到handler消息
    - 由于通知需要上下文Context，可能会引发handler持有上下文导致内存泄漏。固这里采用弱引用–>随时可能会被垃圾回收器回收，不一定要等到虚拟机内存不足时才强制回收。
- 关于动画的销毁设计
    - 由于展现和隐藏，都会涉及到动画。因此在使用动画的时候，注意动画资源的效果，并且需要移动动画监听。



### 05.其他问题说明









