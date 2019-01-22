package com.ycbjie.todo.dagger;


import com.ycbjie.todo.presenter.SearchListPresenter;
import com.ycbjie.todo.presenter.WorkDoPresenter;
import com.ycbjie.todo.presenter.WorkNewPresenter;
import com.ycbjie.todo.ui.WorkDoActivity;
import com.ycbjie.todo.ui.activity.SearchListActivity;
import com.ycbjie.todo.ui.activity.WorkNewActivity;

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

@Component(modules = {UiModule.class, DataModule.class})
public interface UiComponent {

    void inject(WorkDoActivity activity);
    void inject(WorkDoPresenter presenter);

    void inject(WorkNewActivity activity);
    void inject(WorkNewPresenter presenter);

    void inject(SearchListActivity activity);
    void inject(SearchListPresenter presenter);

}
