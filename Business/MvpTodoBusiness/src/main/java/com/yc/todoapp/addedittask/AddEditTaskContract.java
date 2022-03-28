

package com.yc.todoapp.addedittask;

import com.yc.todoapp.BasePresenter;
import com.yc.todoapp.BaseView;


/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/7/10
 *     desc  : 定义了契约类（接口）
 *     revise: Google官方的实现中加入了契约类来统一管理view与presenter的所有的接口，这种方式使得view与presenter中有哪些功能，一目了然，维护起来也方便
 * </pre>
 */
public interface AddEditTaskContract {

    interface View extends BaseView<Presenter> {

        void showEmptyTaskError();

        void showTasksList();

        void setTitle(String title);

        void setDescription(String description);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void saveTask(String title, String description);

        void populateTask();

        boolean isDataMissing();
    }
}
