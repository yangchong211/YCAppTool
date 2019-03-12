package com.ns.yc.lifehelper.ui.me.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.ns.yc.lifehelper.R;
import com.ycbjie.library.base.mvp.BaseLazyFragment;
import com.ycbjie.library.db.cache.CacheZhLike;
import com.ns.yc.lifehelper.ui.me.contract.MeNewsCollectContract;
import com.ns.yc.lifehelper.ui.me.presenter.MeNewsCollectPresenter;
import com.ns.yc.lifehelper.ui.me.view.activity.MeCollectActivity;
import com.ns.yc.lifehelper.ui.me.view.adapter.MeNewsLikeAdapter;

import org.yczbj.ycrefreshviewlib.callback.DefaultItemTouchHelpCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/9/12
 *     desc  : 我的收藏页面，新闻
 *     revise:
 * </pre>
 */
public class MeNewsCollectFragment extends BaseLazyFragment implements MeNewsCollectContract.View {

    RecyclerView recyclerView;

    private MeNewsCollectContract.Presenter presenter = new MeNewsCollectPresenter(this);
    private List<CacheZhLike> mList;
    private MeCollectActivity activity;
    private MeNewsLikeAdapter mAdapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.subscribe();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MeCollectActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }


    @Override
    public int getContentView() {
        return R.layout.base_recycler_view;
    }

    @Override
    public void initView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        initRecycleView();
        initCallBack();
    }


    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void onLazyLoad() {
        presenter.getLikeData();
    }

    private void initRecycleView() {
        mList = new ArrayList<>();
        mAdapter = new MeNewsLikeAdapter(activity, mList);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(mAdapter);
    }


    private void initCallBack() {
        DefaultItemTouchHelpCallback mCallback = new DefaultItemTouchHelpCallback(
                new DefaultItemTouchHelpCallback.OnItemTouchCallbackListener() {
            @Override
            public void onSwiped(int adapterPosition) {
                // 滑动删除的时候，从数据库、数据源移除，并刷新UI
                if (mList != null) {
                    presenter.deleteLikeData(mList.get(adapterPosition).getId());
                    mList.remove(adapterPosition);
                    mAdapter.notifyItemRemoved(adapterPosition);
                }
            }

            @Override
            public boolean onMove(int srcPosition, int targetPosition) {
                if (mList != null) {
                    // 更换数据库中的数据Item的位置
                    boolean isPlus = srcPosition < targetPosition;
                    presenter.changeLikeTime(mList.get(srcPosition).getId(),
                            mList.get(targetPosition).getTime(), isPlus);
                    // 更换数据源中的数据Item的位置
                    Collections.swap(mList, srcPosition, targetPosition);
                    // 更新UI中的Item的位置，主要是给用户看到交互效果
                    mAdapter.notifyItemMoved(srcPosition, targetPosition);
                    return true;
                }
                return false;
            }
        });
        mCallback.setDragEnable(true);
        mCallback.setSwipeEnable(true);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void showContent(List<CacheZhLike> likeList) {
        mList.clear();
        mList.addAll(likeList);
        mAdapter.notifyDataSetChanged();
    }


}
