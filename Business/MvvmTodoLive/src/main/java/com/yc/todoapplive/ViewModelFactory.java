

package com.yc.todoapplive;

import android.annotation.SuppressLint;
import android.app.Application;

import com.yc.todoapplive.addedittask.AddEditTaskViewModel;
import com.yc.todoapplive.data.source.TasksRepository;
import com.yc.todoapplive.statistics.StatisticsViewModel;
import com.yc.todoapplive.taskdetail.TaskDetailViewModel;
import com.yc.todoapplive.tasks.TasksViewModel;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * A creator is used to inject the product ID into the ViewModel
 * <p>
 * This creator is to showcase how to inject dependencies into ViewModels. It's not
 * actually necessary in this case, as the product ID can be passed in a public method.
 */
public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    @SuppressLint("StaticFieldLeak")
    private static volatile ViewModelFactory INSTANCE;

    private final TasksRepository mTasksRepository;

    public static ViewModelFactory getInstance(Application application) {

        if (INSTANCE == null) {
            synchronized (ViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ViewModelFactory(
                            Injection.provideTasksRepository(application.getApplicationContext()));
                }
            }
        }
        return INSTANCE;
    }

    public TasksRepository getTasksRepository() {
        return mTasksRepository;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }

    private ViewModelFactory(TasksRepository repository) {
        mTasksRepository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(StatisticsViewModel.class)) {
            //noinspection unchecked
            return (T) new StatisticsViewModel(mTasksRepository);
        } else if (modelClass.isAssignableFrom(TaskDetailViewModel.class)) {
            //noinspection unchecked
            return (T) new TaskDetailViewModel(mTasksRepository);
        } else if (modelClass.isAssignableFrom(AddEditTaskViewModel.class)) {
            //noinspection unchecked
            return (T) new AddEditTaskViewModel(mTasksRepository);
        } else if (modelClass.isAssignableFrom(TasksViewModel.class)) {
            //noinspection unchecked
            return (T) new TasksViewModel(mTasksRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
