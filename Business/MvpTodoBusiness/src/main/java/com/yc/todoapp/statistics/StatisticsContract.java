

package com.yc.todoapp.statistics;

import com.yc.todoapp.BasePresenter;
import com.yc.todoapp.BaseView;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface StatisticsContract {

    interface View extends BaseView<Presenter> {

        void setProgressIndicator(boolean active);

        void showStatistics(int numberOfIncompleteTasks, int numberOfCompletedTasks);

        void showLoadingStatisticsError();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

    }
}
