package com.yc.fragmentmanager

import android.os.Bundle
import android.view.View

/**
 * <pre>
 * @author yangchong
 * email  : yangchong211@163.com
 * GitHub : https://github.com/yangchong211/YCCommonLib
 * time   : 2019/5/18
 * desc   : 实现所有fragment生命周期监听接口
 * revise :
</pre> *
 */
interface IFragmentLifecycle {
    fun onCreate(savedInstanceState: Bundle?)
    fun onViewCreated(view: View, savedInstanceState: Bundle?)
    fun onResume()
    fun onStart()
    fun onPause()
    fun onStop()
    fun onDestroy()
}