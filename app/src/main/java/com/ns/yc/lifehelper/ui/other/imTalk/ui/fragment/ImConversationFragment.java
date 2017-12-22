package com.ns.yc.lifehelper.ui.other.imTalk.ui.fragment;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.BaseFragment;
import com.ns.yc.lifehelper.ui.other.imTalk.model.ConversationList;
import com.ns.yc.lifehelper.ui.other.imTalk.ui.ImTalkActivity;
import com.ns.yc.lifehelper.ui.other.imTalk.ui.adapter.ImConversationAdapter;
import com.ns.yc.lifehelper.utils.IMemClientUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/12/20
 * 描    述：即时通讯训练案例
 * 修订历史：
 * ================================================
 */
public class ImConversationFragment extends BaseFragment {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private ImTalkActivity activity;
    List<ConversationList> lists = new ArrayList<>();
    private ImConversationAdapter adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (ImTalkActivity) context;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }



    @Override
    public int getContentView() {
        return R.layout.fragment_im_conversation;
    }

    @Override
    public void initView() {
        initRecycleView();
    }

    @Override
    public void initListener() {
        adapter.setOnItemClickListener(new ImConversationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position , int id) {
                // 获取当前登录用户的 username
                String currUsername = IMemClientUtils.getIMemClientInstance().getCurrentUser();

            }
        });
    }

    @Override
    public void initData() {

    }

    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new ImConversationAdapter(activity,lists);
        recyclerView.setAdapter(adapter);
    }


}
