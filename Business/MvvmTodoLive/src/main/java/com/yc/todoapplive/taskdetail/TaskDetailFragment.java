

package com.yc.todoapplive.taskdetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.yc.todoapplive.Event;
import com.yc.todoapplive.R;
import com.yc.todoapplive.databinding.TaskdetailFragBinding;
import com.yc.todoapplive.util.SnackbarUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;


/**
 * Main UI for the task detail screen.
 */
public class TaskDetailFragment extends Fragment {

    public static final String ARGUMENT_TASK_ID = "TASK_ID";

    public static final int REQUEST_EDIT_TASK = 1;

    private TaskDetailViewModel mViewModel;

    public static TaskDetailFragment newInstance(String taskId) {
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_TASK_ID, taskId);
        TaskDetailFragment fragment = new TaskDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupFab();

        setupSnackbar();
    }

    private void setupSnackbar() {
        mViewModel.getSnackbarMessage().observe(this, new Observer<Event<Integer>>() {
            @Override
            public void onChanged(Event<Integer> event) {
                Integer msg = event.getContentIfNotHandled();
                if (msg != null) {
                    SnackbarUtils.showSnackbar(getView(), getString(msg));
                }
            }
        });
    }

    private void setupFab() {
        FloatingActionButton fab = getActivity().findViewById(R.id.fab_edit_task);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.editTask();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.start(getArguments().getString(ARGUMENT_TASK_ID));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.taskdetail_frag, container, false);

        TaskdetailFragBinding viewDataBinding = TaskdetailFragBinding.bind(view);

        mViewModel = TaskDetailActivity.obtainViewModel(getActivity());

        viewDataBinding.setViewmodel(mViewModel);
        viewDataBinding.setLifecycleOwner(getActivity());

        TaskDetailUserActionsListener actionsListener = getTaskDetailUserActionsListener();

        viewDataBinding.setListener(actionsListener);

        setHasOptionsMenu(true);

        return view;
    }

    private TaskDetailUserActionsListener getTaskDetailUserActionsListener() {
        return new TaskDetailUserActionsListener() {
            @Override
            public void onCompleteChanged(View v) {
                mViewModel.setCompleted(((CheckBox) v).isChecked());
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_delete) {
            mViewModel.deleteTask();
            return true;
        }
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.taskdetail_fragment_menu, menu);
    }
}
