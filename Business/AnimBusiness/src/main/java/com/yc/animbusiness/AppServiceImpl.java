package com.yc.animbusiness;

import com.pedaily.yc.ycdialoglib.toast.ToastUtils;
import com.yc.businessinterface.AppServiceProvider;
import com.yc.spi.annotation.ServiceProvider;

@ServiceProvider(AppServiceProvider.class)
public class AppServiceImpl implements AppServiceProvider {

    @Override
    public void toast() {
        ToastUtils.showRoundRectToast("吐司内容");
    }

    @Override
    public void dialog() {
        ToastUtils.showRoundRectToast("弹窗dialog");
    }

    @Override
    public void snackBar() {
        ToastUtils.showRoundRectToast("弹出snack");
    }
}
