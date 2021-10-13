# YCTimer倒计时工具
#### 目录介绍
- 01.实现倒计时的方式
- 02.使用封装计时器
- 03.各种倒计时器分析
- 04.倒计时UI图


### 00.简单的介绍
- 倒计时工具，分别使用了handler，自定义CountDownTimer，Timer和TimerTask，chronometer控件，和属性动画实现倒计时的功能。封装CountDownTimer倒计时器【在原有基础上增加了暂停和恢复倒计时功能】，解决了倒计时无法到0和从最大值开始bug


### 01.实现倒计时的方式
- 01.使用Handler实现倒计时
    - mHandler + runnable ，这种是最常见的一种方式。实质是不断调用mHandler.postDelayed(this, 1000)达到定时周期目的
- 02.使用CountDownTimer实现倒计时
    - 也是利用mHandler + runnable，在此基础上简单封装一下。使用场景更强大，比如一个页面有多个倒计时器，用这个就很方便……
- 03.利用Timer实现定时器
    - 使用Timer + TimerTask + handler方式实现倒计时
- 04.使用chronometer控件倒计时
    - 新出的继承TextView组件，里头是使用了View.postDelayed + runnable实现倒计时
- 05.利用动画实现倒计时
    - 这种方式用的比较少，但也是一种思路。主要是设置动画时间，在onAnimationUpdate监听设置倒计时处理


### 02.使用封装计时器
- 使用CountDownTimer实现倒计时。看CountDownTimer源码可知，也是通过对handler封装实现的
    ```
    mCountDownTimer = new CountDownTimer();
    //设置倒计时总时间
    mCountDownTimer.setMillisInFuture(MAX_TIME);
    //设置倒计时间隔值
    mCountDownTimer.setCountdownInterval(1000);
    //设置倒计时监听
    mCountDownTimer.setCountDownListener(new TimerListener() {
        @Override
        public void onStart() {
        }

        @Override
        public void onFinish() {
            mTvTime.setText("完成!");
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mTvTime.setText(CountTimeTools.getCountTimeByLong(millisUntilFinished));
        }
    });
    ```
- 关于开始，暂停，恢复，移除等操作
    ```
    //开始
    mCountDownTimer.start();
    //结束销毁
    mCountDownTimer.cancel();
    //暂停
    mCountDownTimer.pause();
    //恢复暂停
    mCountDownTimer.resume();
    ```
- 注意在页面销毁的时候
    ```
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountDownTimer!=null){
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
    }
    ```


### 03.各种倒计时器分析
- 第一种利用Handler实现倒计时
    - 这种用的很普遍，但存在一个问题。如果是一个页面需要开启多个倒计时【比如列表页面】，则比较难处理。
- 第二种使用CountDownTimer实现倒计时
    - new CountDownTimer(5000, 1000).start()
        - 期待的效果是：“5-4-3-2-1-finish”或者“5-4-3-2-1-0”。这里，显示 0 和 finish 的时间应该是一致的，所以把 0 放在 onFinish() 里显示也可以。但实际有误差……
    - 存在的几个问题
        - 问题1. 每次 onTick() 都会有几毫秒的误差，并不是期待的准确的 "5000, 4000, 3000, 2000, 1000, 0"。
        - 问题2. 多运行几次，就会发现这几毫秒的误差，导致了计算得出的剩余秒数并不准确，如果你的倒计时需要显示剩余秒数，就会发生 秒数跳跃/缺失 的情况（比如一开始从“4”开始显示——缺少“5”，或者直接从“5”跳到了“3”——缺少“4”）。
        - 问题3. 最后一次 onTick() 到 onFinish() 的间隔通常超过了 1 秒，差不多是 2 秒左右。如果你的倒计时在显示秒数，就能很明显的感觉到最后 1 秒停顿的时间很长。
        - 问题4. 如果onTick耗时超时，比如超过了1000毫秒，则会导致出现onTick出现跳动问题
    - 解决方案
        - 具体看lib中的CountDownTimer类。
        - 注意：onTick方法中如何执行耗时操作【大于1秒的执行代码】，建议使用handler消息机制进行处理，避免出现其他问题。
- 第三种利用Timer实现定时器
    - 注意点
        - Timer和TimerTask都有cancel方法，而且最好同时调用；如果已经cancel，下次必须创建新的Timer才能schedule。
    - 可能存在的问题
        - 如果你在当前的activity中schedule了一个task，但是没有等到task结束，就按Back键finish了当前的activity，Timer和TimerTask并不会自动cancel或者销毁，它还会在后台运行，此时如果你在task的某个阶段要调起一个控件（比如AlertDialog），而该控制依赖被销毁的activity，那么将会引发crash。
        - 所以建议在页面销毁的时候，将Timer和TimerTask都有cancel结束并且设置成null
        - Timer 的方式实现定时任务，用来做倒计时是没有问题的。但是如果用来执行周期任务，恰好又有多个任务，恰好两个任务之间的时间间隔又比前一个任务执行时间短就会发生定时不准确的现象了。Timer 在执行过程中如果任务跑出了异常，Timer 会停止所有的任务。Timer 执行周期任务时依赖系统时间，系统时间的变化会引起 Timer 任务执行的变化。
    - [关于timer的源码分析](https://github.com/yangchong211/YCTimer/blob/master/read/04.Timer%E5%92%8CTimerTask.md)







