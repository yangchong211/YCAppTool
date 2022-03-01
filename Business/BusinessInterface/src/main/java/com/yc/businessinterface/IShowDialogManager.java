package com.yc.businessinterface;

import android.content.Context;

import com.yc.api.route.IRoute;

public interface IShowDialogManager extends IRoute {

    void showDialog(Context context, CallBack callBack);

    interface CallBack{
        void select(boolean sure);
    }

}
