package com.ns.yc.lifehelper.ui.main.contract;


import com.flyco.tablayout.listener.CustomTabEntity;
import com.ycbjie.library.base.mvp.BasePresenter;
import com.ycbjie.library.base.mvp.BaseView;

import java.util.ArrayList;



/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/03/22
 *     desc  : Main主页面
 *     revise:
 * </pre>
 */
public interface MainContract {

    interface View extends BaseView {

    }

    interface Presenter extends BasePresenter {
        ArrayList<CustomTabEntity> getTabEntity();
        void getUpdate();
    }


}
