package com.ns.yc.lifehelper.ui.data.contract;


import com.ns.yc.lifehelper.model.bean.ImageIconBean;
import com.ycbjie.library.base.mvp.BasePresenter;
import com.ycbjie.library.base.mvp.BaseView;

import java.util.ArrayList;
import java.util.List;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/03/22
 *     desc  :
 *     revise: v1.4 17年6月8日
 *             v1.5 17年10月3日修改
 * </pre>
 */
public interface DataFragmentContract {

    interface View extends BaseView {
        void setGridView(String[] toolName, ArrayList<Integer> logoList);
        void setRecycleView(ArrayList<Integer> list);
    }

    interface Presenter extends BasePresenter {
        List<ImageIconBean> getVpData();
        void initGridViewData();
        void initRecycleViewData();
    }


}
