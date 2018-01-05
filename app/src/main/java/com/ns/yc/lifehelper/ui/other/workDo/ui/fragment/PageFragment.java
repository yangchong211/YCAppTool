package com.ns.yc.lifehelper.ui.other.workDo.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.util.SPUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.Constant;
import com.ns.yc.lifehelper.base.mvp1.BaseFragment;
import com.ns.yc.lifehelper.ui.other.toDo.bean.TaskDetailEntity;
import com.ns.yc.lifehelper.ui.other.workDo.contract.PageFragmentContract;
import com.ns.yc.lifehelper.ui.other.workDo.presenter.PageFragmentPresenter;
import com.ns.yc.lifehelper.ui.other.workDo.ui.WorkDoActivity;
import com.ns.yc.lifehelper.ui.other.workDo.ui.adapter.WorkDoAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/10/21
 * 描    述：此版块训练dagger2+MVP
 * 修订历史：
 * ================================================
 */
public class PageFragment extends BaseFragment implements PageFragmentContract.View{

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private List<TaskDetailEntity> mList = new ArrayList<>();
    private PageFragmentContract.Presenter presenter = new PageFragmentPresenter(this);
    private WorkDoActivity activity;
    private WorkDoAdapter adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (WorkDoActivity) context;
        if (context instanceof OnPageListener) {
            mListener = (OnPageListener) context;
        } else {
            Log.e("PageFragment", "context is not instanceof OnPageFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.subscribe();
        presenter.bindView(activity);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
        mListener = null;
        adapter = null;
        mList = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public int getContentView() {
        return R.layout.base_recycler_view;
    }

    @Override
    public void initView() {
        initRecycleView();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }


    private void initRecycleView() {
        String s = SPUtils.getInstance(Constant.SP_NAME).getString(Constant.CONFIG_KEY.SHOW_AS_LIST, "list");
        if (s.equals("list")){
            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(activity, 2));
        }
        adapter = new WorkDoAdapter(mList ,activity);
        setListener();
        recyclerView.setAdapter(adapter);
    }

    private void setListener() {
        boolean showPriority = SPUtils.getInstance(Constant.SP_NAME).getBoolean(Constant.CONFIG_KEY.SHOW_PRIORITY, true);
        adapter.setShowPriority(showPriority);
        if (mListener == null && getActivity() instanceof OnPageListener) {
            mListener = (OnPageListener) getActivity();
        }
        adapter.setListener(new WorkDoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TaskDetailEntity entity) {
                if (mListener != null){
                    mListener.onListTaskItemClick(position, entity);
                }
            }

            @Override
            public void onItemLongClick(int position, TaskDetailEntity entity) {
                if (mListener != null){
                    mListener.onListTaskItemLongClick(position, entity);
                }
            }
        });
    }

    public WorkDoAdapter getAdapter() {
        return adapter;
    }

    public void clearTasks() {
        if(mList!=null){
            mList.clear();
        }
    }

    public void insertTask(TaskDetailEntity task) {
        if (!mList.contains(task)) {
            mList.add(task);
        }
        if (adapter != null) {
            adapter.notifyItemInserted(mList.size() - 1);
        }
    }

    public TaskDetailEntity deleteTask(int index) {
        TaskDetailEntity taskDetailEntity = mList.get(index);
        mList.remove(index);
        adapter.notifyItemRemoved(index);
        return taskDetailEntity;
    }

    private OnPageListener mListener;
    public interface OnPageListener {
        void onListTaskItemClick(int position, TaskDetailEntity entity);
        void onListTaskItemLongClick(int position, TaskDetailEntity entity);
    }

}
