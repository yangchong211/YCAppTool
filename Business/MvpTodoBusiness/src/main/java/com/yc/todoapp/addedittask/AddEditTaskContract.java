

package com.yc.todoapp.addedittask;

import com.yc.todoapp.BasePresenter;
import com.yc.todoapp.BaseView;

/**
 * 定义了契约类（接口）
 * This specifies the contract between the view and the presenter.
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
