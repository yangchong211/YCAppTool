package com.ycbjie.todo.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.util.SPUtils;
import com.ycbjie.library.base.mvp.BaseFragment;
import com.ycbjie.library.constant.Constant;
import com.ycbjie.library.db.cache.CacheTaskDetailEntity;
import com.ycbjie.todo.R;
import com.ycbjie.todo.contract.PageFragmentContract;
import com.ycbjie.todo.presenter.PageFragmentPresenter;
import com.ycbjie.todo.ui.WorkDoActivity;
import com.ycbjie.todo.ui.adapter.WorkDoAdapter;

import java.util.ArrayList;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/03/22
 *     desc  : 此版块训练dagger2+MVP
 *     revise:
 * </pre>
 */
public class PageFragment extends BaseFragment implements PageFragmentContract.View{

    RecyclerView recyclerView;
    private ArrayList<CacheTaskDetailEntity> mList = new ArrayList<>();
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
    }

    @Override
    public int getContentView() {
        return R.layout.base_recycler_view;
    }

    @Override
    public void initView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
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
            public void onItemClick(int position, CacheTaskDetailEntity entity) {
                if (mListener != null){
                    mListener.onListTaskItemClick(position, entity);
                }
            }

            @Override
            public void onItemLongClick(int position, CacheTaskDetailEntity entity) {
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

    public void insertTask(CacheTaskDetailEntity task) {
        if (!mList.contains(task)) {
            mList.add(task);
        }
        if (adapter != null) {
            adapter.notifyItemInserted(mList.size() - 1);
        }
    }

    public CacheTaskDetailEntity deleteTask(int index) {
        if (mList.size()>index && index>=0){
            CacheTaskDetailEntity taskDetailEntity = mList.get(index);
            mList.remove(index);
            adapter.notifyItemRemoved(index);
            return taskDetailEntity;
        }else {
            return null;
        }
    }

    private OnPageListener mListener;
    public interface OnPageListener {
        void onListTaskItemClick(int position, CacheTaskDetailEntity entity);
        void onListTaskItemLongClick(int position, CacheTaskDetailEntity entity);
    }

}
