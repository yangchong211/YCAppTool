package com.yc.fragmentmanager;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     time   : 2019/5/18
 *     desc   : 实现所有fragment生命周期监听接口
 *     revise :
 * </pre>
 */
public interface IFragmentLifecycle {

    void onCreate(@Nullable Bundle savedInstanceState);

    void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState);

    void onResume();

    void onStart();

    void onPause();

    void onStop();

    void onDestroy();
}
