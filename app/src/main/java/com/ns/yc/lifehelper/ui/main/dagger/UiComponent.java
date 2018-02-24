package com.ns.yc.lifehelper.ui.main.dagger;


import com.ns.yc.lifehelper.ui.main.presenter.MainPresenter;
import com.ns.yc.lifehelper.ui.main.view.MainActivity;

import dagger.Component;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/11/14.
 * 描    述：此版块训练dagger2+MVP
 * 修订历史：
 * ================================================
 */

@Component(modules = {UiModule.class})
public interface UiComponent {

    void inject(MainActivity activity);
    void inject(MainPresenter presenter);

}
