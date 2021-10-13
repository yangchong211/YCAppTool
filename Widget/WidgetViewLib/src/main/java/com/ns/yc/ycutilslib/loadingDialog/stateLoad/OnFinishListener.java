package com.ns.yc.ycutilslib.loadingDialog.stateLoad;

import android.view.View;


public interface OnFinishListener {

    /**
     * 分发绘制完成事件
     * @param v 绘制完成的View
     */
    void dispatchFinishEvent(View v);
}
