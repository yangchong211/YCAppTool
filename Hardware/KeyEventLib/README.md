# 硬键盘按键监听库
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.其他问题说明
- 06.参考博客说明




### 01.基础概念说明
#### 1.1 业务背景说明
- 实际开发中，时常会需要监听软键盘的输入事件。
    - App聊天的消息发送，你会发现他的聊天框并没有发送的控件，软键盘的换行或者某个按钮会变成发送；
    - 在某些浏览器的输入框，输入内容后，软件盘的某个按钮会有搜索的字样；
- 监听键盘需求分析
    - 这些都可以通过监听键盘和输入框的一些配置实现这些功能。



#### 1.2 键盘技术背景
- 在设备上按键盘上数字，确定，删除等按键。可以拿到对应的数据信息。主要是监听对应的keyCode值



#### 1.3 KeyEvent分发流程
- Key Event 在View 树里的分发流程:
    - 1.ViewRootImpl#processKeyEvent()，这个方法可以看作是按键入口
    - 2.View#dispatchKeyEvent()，将键传递给视图层次结构。
    - 3.DecorView#dispatchKeyEvent()，DecorView是Android中页面的根容器布局，因此看它的分发流程
    - 4.Window#dispatchKeyEvent，每个页面都是一个Window窗口，DecorView将keyEvent分发到Window
    - 5.AppCompatActivity#dispatchKeyEvent()，这个是activity分发操作
    - 6.KeyEventDispatcher#dispatchKeyEvent()，这个里面判断activity，dialog等方式，以便分发到对应的组件上
    - 7.KeyEventDispatcher#activitySuperDispatchKeyEventPre28()，这里主要看activity，可以看到调用了superDispatchKeyEvent传递到window，如果window没处理则调用Decor分发。
    - 8.最终可以发现，处理KeyEvent事件，可以是Window，Activity，View。
- 总结按键消息分发流程:
    - 1.分发时使用的主要方法名为dispatchKeyEvent，superDispatchKeyEvent等名字，虽然这些方法可以被覆盖，但设计的目的不是用于接收KeyEvent的，用于接收KeyEvent的方法名有onKeyDown,onKeyUp,onKeyLongPress,onKeyMultiple
    - 2.分发时的主要路径 DecorView.dispatchKeyEvent-> PhoneWindow.dispatchKeyEvent->DecorView.superDispatchKeyEvent -> ViewGroup.dispatchKeyEvent 这时候就会在ViewTree中调用dispatchKeyEvent，先找到focusView的最父级调用dispatchKeyEvent，然后再找下一级，最终找到focus view，并调用它的dispatchKeyEvent，然后再调用focus view的onKeyDown等事件，这时候才想让View接收KeyEvent
    - 3.所以如果没有拦截dispatchKeyEvent的话，从接收KeyEvent的角度来看，最先可接收KeyEvent的是focus view，然后是View的UnhandledKeyEvent，然后才是Activity，然后才是Window，最后才是ViewRootImpl的焦点导航处理
    - 4.从分发的角度来看，最先到达DecorView，然后是Activity，然后是KeyEventDispatcher，然后是PhoneWindow，然后是回到DecorView，然后在ViewTree中分发，沿着找focus view的路径分发，最终找到focus view进行分发



#### 1.4 监听键盘入口
- 在软键盘/硬件盘，输入中我们需要监听我们按的键，监听按键的KeyCode。
    - 比如：KeyEvent.KEYCODE_ENTER，KeyEvent.KEYCODE_DEL等按键。就分别代表的意思是：Enter确认键，Del删除键。
- 硬件（驱动）层到应用层的转化，这层可以理解为SCANCODE->KEYCODE的转化。
    - 当在硬件键盘上按上按键，比如按下5的时候。按键会唤醒设备，但是该key消息不会发送给应用，而WAKE 一样会唤醒设备且key消息会发送给应用。
    - 转化时，会通过scanCode 找到keyCodeName，再通过 keyCodeName 去转化成keyCode。后面一层的定义在：frameworks/native/include/input/InputEventLabels.h
    - Android开发目前仅仅是关注于处理KeyCode的逻辑，至于硬件层是如何传递，暂且放着不动。



#### 1.5 KeyCode是什么
- 参数keyCode，该参数为被按下的键值即键盘码，手机键盘中每个按钮都会有其单独的键盘码，在应用程序都是通过键盘码才知道用户按下的是哪个键。
- 参数event，该参数为按键事件的对象，其中包含了触发事件的详细信息，例如事件的状态、事件的类型、事件发生的时间等。当用户按下按键时，系统会自动将事件封装成KeyEvent对象供应用程序使用。



### 02.常见思路和做法
#### 2.1 整体设计思路
- 可以把这个需求拆分成几步
    - 第一步：将硬键盘按键分为两类，一类是数字按键，一类是功能按键。定义按键接口：IKeyCode，IKeyEvent。
    - 第二步：实现按键接口，实现数字按键，功能按键判断的功能。比如判断是否是Enter键，keyCode == KeyEvent.KEYCODE_ENTER。
    - 第三步：定义键盘分发的接口，因为要监听键盘并且暴露给外部开发者使用。因此需要设计注册监听回调(添加到集合中)。
    - 第四步：键盘分发接口实现，这里面做具体的逻辑。比如监听activity按键分发，遍历监听回调callBack集合，然后回调。



#### 2.2 分发按键事件
- 按键分发的入口有哪些
    - 第一种：setOnKeyListener 监听。这种做法主要是针对View级别的按键监听。
    - 第二种：dispatchKeyEvent 监听。这种做法主要是针对Activity级别的按键监听。
    - 第三种：onKeyUp 监听，onKeyDown 监听。
- 当键盘按下时
    - 首先触发dispatchKeyEvent
    - 然后触发onUserInteraction
    - 再次onKeyDown
- 如果按下紧接着松开
    - 紧跟着触发dispatchKeyEvent
    - 然后触发onUserInteraction
    - 再次onKeyUp
- 可以选择分发事件的入口为
    - dispatchKeyEvent是做分发的工作，当然onKeyDown也可以接收到这样实现分发的工作。


### 03.Api调用说明
#### 3.1 基础功能API
- 初始化该库的操作
    ```
    KeyEventManager.getInstance().init(this);
    KeyEventManager.getInstance().setKeyboard(new AndroidKeyBoard());  
    ```
- 在某Activity页面添加键盘回调
    ```
    KeyEventManager.getInstance().getKeyboard().addDispatchCallback(keyBoardCallback);
    KeyEventManager.getInstance().getKeyboard().removeDispatchCallback(keyBoardCallback);
    
    private final ResultCallback<IKeyEvent> keyBoardCallback = new ResultCallback<IKeyEvent>() {
        @Override
        public boolean result(IKeyEvent keyEvent) {
            //键盘设备回调
            int keycode = keyEvent.getKeyEvent().getKeyCode();
            String msg = KeyEventManager.getInstance().getKeyMsg();
            AppLogUtils.d("KeyCodeActivity : yc result keycode " + keycode);
            AppLogUtils.d("KeyCodeActivity : yc onKeyUp msg " + msg);
            tvContent.setText("逗比"+msg);
            return false;
        }
    };
    ```
- activity页面分发按键事件
    ```
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        KeyEventManager.getInstance().getKeyboard().dispatchKeyEvent(event.getKeyCode(),event);
        return super.dispatchKeyEvent(event);
    }
    ```


#### 3.2 高度定制调用
- 如果按键code对不上具体功能。比如说有的设备点击enter键回调code是100，有的是101。这个时候可以自己实现
    ```
    public class AndroidKeyBoard extends DispatchEventImpl {
    
        @Override
        public IKeyEvent getKeyEvent() {
            return new MyKeyEventImpl();
        }
    
        /**
         * 如果按键code对不上具体功能。比如说有的设备点击enter键回调code是100，有的是101。这个时候可以自己实现
         */
        private static class MyKeyEventImpl extends KeyEventImpl{
            @Override
            public boolean isEnter() {
                return getKeyCode() == KeyEvent.KEYCODE_ENTER;
            }
        }
    }
    ```


### 04.遇到的坑分析



### 05.其他问题说明
#### 5.1 按键快速测试
- 设置按键的映射实现操作
    ```
    KeyCodeHelper.getInstance().setKeycodeImpl(new KeyEventImpl());
    ```
- 获取按键信息然后获取code
    ```
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        AppLogUtils.d("KeyCodeActivity : yc onKeyUp " + keyCode);
        String msg = KeyCodeHelper.getInstance().getKeyMsg(keyCode);
        AppLogUtils.d("KeyCodeActivity : yc onKeyUp msg " + msg);
        tvContent.setText(msg);
        return super.onKeyUp(keyCode,event);
    }
    ```


### 06.参考博客说明


