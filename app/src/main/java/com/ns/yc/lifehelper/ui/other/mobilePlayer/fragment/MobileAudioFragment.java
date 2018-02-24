package com.ns.yc.lifehelper.ui.other.mobilePlayer.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.SizeUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.constant.ConstantKeys;
import com.ns.yc.lifehelper.base.mvp1.BaseFragment;
import com.ns.yc.lifehelper.ui.other.mobilePlayer.MobilePlayerActivity;
import com.ns.yc.lifehelper.ui.other.mobilePlayer.activity.MobileAudioPlayActivity;
import com.ns.yc.lifehelper.ui.other.mobilePlayer.adapter.MobileAudioListAdapter;
import org.yczbj.ycrefreshviewlib.item.RecycleViewItemLine;
import com.ns.yc.lifehelper.utils.localFile.FileManager;
import com.ns.yc.lifehelper.utils.localFile.bean.AudioItem;

import org.yczbj.ycrefreshviewlib.YCRefreshView;
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;

import butterknife.Bind;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/31
 * 描    述：音乐list页面
 * 修订历史：
 * ================================================
 */
public class MobileAudioFragment extends BaseFragment {

    @Bind(R.id.recyclerView)
    YCRefreshView recyclerView;
    private MobilePlayerActivity activity;
    private MobileAudioListAdapter adapter;
    public static ArrayList<AudioItem> musics;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MobilePlayerActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }


    @Override
    public int getContentView() {
        return R.layout.base_easy_recycle;
    }


    @Override
    public void initView() {
        initRecycleView();
    }


    @Override
    public void initListener() {
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                int size = musics.size();
                AudioItem audioItem = musics.get(position);
                Intent intent = new Intent(activity, MobileAudioPlayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(ConstantKeys.MOBILE_AUDIO_ITEMS, audioItem);
                bundle.putInt(ConstantKeys.CURRENT_POSITION,position);
                bundle.putInt(ConstantKeys.MOBILE_SIZE,size);
                intent.putExtras(bundle);
                //intent.putExtra(ConstantKeys.MOBILE_AUDIO_ITEMS, musics.get(position));
                //intent.putExtra(ConstantKeys.CURRENT_POSITION,position);
                startActivity(intent);
            }
        });
    }


    @Override
    public void initData() {
        musics = FileManager.getInstance().getMusics(activity);
        adapter.addAll(musics);
        adapter.notifyDataSetChanged();
    }


    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        final RecycleViewItemLine line = new RecycleViewItemLine(activity, LinearLayout.HORIZONTAL,
                SizeUtils.dp2px(1), Color.parseColor("#e5e5e5"));
        recyclerView.addItemDecoration(line);
        adapter = new MobileAudioListAdapter(activity);
        recyclerView.setAdapter(adapter);
    }


}
