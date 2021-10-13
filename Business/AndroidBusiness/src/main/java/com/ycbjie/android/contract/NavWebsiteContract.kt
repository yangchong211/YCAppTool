package com.ycbjie.android.contract
import com.ycbjie.android.model.bean.NaviBean
import com.ycbjie.library.base.mvp.BasePresenter
import com.ycbjie.library.base.mvp.BaseView


/**
 * <pre>
 *     @author 杨充
 *     blog  :
 *     time  : 2017/05/30
 *     desc  : 网站导航页面
 *     revise:
 * </pre>
 */
interface NavWebsiteContract {

    interface View : BaseView {
        fun getNaviWebSiteSuccess(t: MutableList<NaviBean>?)
        fun getNaiWebSiteFail(message: String)
    }

    interface Presenter : BasePresenter {
        fun getWebsiteNavi()
    }

}
