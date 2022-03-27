

package com.yc.todoapplive.tasks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.yc.todoapplive.Event;
import com.yc.todoapplive.R;
import com.yc.todoapplive.ViewModelFactory;
import com.yc.todoapplive.addedittask.AddEditTaskActivity;
import com.yc.todoapplive.statistics.StatisticsActivity;
import com.yc.todoapplive.taskdetail.TaskDetailActivity;
import com.yc.todoapplive.util.ActivityUtils;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


public class TasksLiveActivity extends AppCompatActivity implements TaskItemNavigator, TasksNavigator {

    private DrawerLayout mDrawerLayout;

    private TasksViewModel mViewModel;


    /**
     * 开启页面
     *
     * @param context 上下文
     */
    public static void startActivity(Context context) {
        try {
            Intent target = new Intent();
            target.setClass(context, TasksLiveActivity.class);
            //target.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(target);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tasks_act);

        setupToolbar();

        setupNavigationDrawer();

        setupViewFragment();

        mViewModel = obtainViewModel(this);

        // Subscribe to "open task" event
        mViewModel.getOpenTaskEvent().observe(this, new Observer<Event<String>>() {
            @Override
            public void onChanged(Event<String> taskIdEvent) {
                String taskId = taskIdEvent.getContentIfNotHandled();
                if (taskId != null) {
                    openTaskDetails(taskId);
                }

            }
        });

        // Subscribe to "new task" event
        mViewModel.getNewTaskEvent().observe(this, new Observer<Event<Object>>() {
            @Override
            public void onChanged(Event<Object> taskIdEvent) {
                if (taskIdEvent.getContentIfNotHandled() != null) {
                    addNewTask();
                }
            }
        });
    }

    public static TasksViewModel obtainViewModel(FragmentActivity activity) {
        // Use a Factory to inject dependencies into the ViewModel
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());

        TasksViewModel viewModel =
                ViewModelProviders.of(activity, factory).get(TasksViewModel.class);

        return viewModel;
    }

    private void setupViewFragment() {
        TasksFragment tasksFragment =
                (TasksFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (tasksFragment == null) {
            // Create the fragment
            tasksFragment = TasksFragment.newInstance();
            ActivityUtils.replaceFragmentInActivity(
                    getSupportFragmentManager(), tasksFragment, R.id.contentFrame);
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void setupNavigationDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        int itemId = menuItem.getItemId();
                        if (itemId == R.id.list_navigation_menu_item) {// Do nothing, we're already on that screen
                        } else if (itemId == R.id.statistics_navigation_menu_item) {
                            Intent intent =
                                    new Intent(TasksLiveActivity.this, StatisticsActivity.class);
                            startActivity(intent);
                        }
                        // Close the navigation drawer when an item is selected.
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mViewModel.handleActivityResult(requestCode, resultCode);
    }

    @Override
    public void openTaskDetails(String taskId) {
        Intent intent = new Intent(this, TaskDetailActivity.class);
        intent.putExtra(TaskDetailActivity.EXTRA_TASK_ID, taskId);
        startActivityForResult(intent, AddEditTaskActivity.REQUEST_CODE);

    }

    @Override
    public void addNewTask() {
        Intent intent = new Intent(this, AddEditTaskActivity.class);
        startActivityForResult(intent, AddEditTaskActivity.REQUEST_CODE);
    }
}
