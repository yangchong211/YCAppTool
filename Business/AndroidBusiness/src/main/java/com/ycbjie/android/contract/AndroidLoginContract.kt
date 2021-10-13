package com.ycbjie.android.contract

import com.ycbjie.android.model.bean.LoginBean
import com.ycbjie.library.base.mvp.BasePresenter
import com.ycbjie.library.base.mvp.BaseView

class AndroidLoginContract {


    interface View : BaseView {
        fun loginSuccess(bean: LoginBean)
        fun loginError(message: String?)
    }

    interface Presenter : BasePresenter {
        fun startLogin(name: String, pwd: String)
    }


}
