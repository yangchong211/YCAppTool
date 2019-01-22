package com.ns.yc.lifehelper.ui.guide.contract;


import android.app.Activity;

import com.ycbjie.library.base.mvp.BasePresenter;
import com.ycbjie.library.base.mvp.BaseView;
import com.ns.yc.lifehelper.model.bean.SelectPoint;

import java.util.List;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/03/22
 *     desc  : 关注点页面
 *     revise:
 * </pre>
 */
public interface SelectFollowContract {

    interface View extends BaseView {
        void refreshData(List<SelectPoint> list);
        void toMainActivity();
    }

    interface Presenter extends BasePresenter {
        void addData(Activity activity);
        void addSelectToRealm(Integer[] selectedIndices);
    }


}
