package com.ns.yc.lifehelper.ui.other.bookReader.view.fragment;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.SizeUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp.BaseFragment;
import com.ns.yc.lifehelper.inter.listener.OnListItemClickListener;
import com.ns.yc.lifehelper.ui.other.bookReader.view.BookReaderActivity;
import com.ns.yc.lifehelper.ui.other.bookReader.view.adapter.ReaderFindAdapter;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.ReaderTopBookActivity;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.support.FindBean;
import com.ns.yc.lifehelper.ui.other.bookReader.view.activity.ReaderCategoryActivity;
import com.ns.yc.lifehelper.ui.other.bookReader.view.activity.ReaderSubjectActivity;
import org.yczbj.ycrefreshviewlib.item.RecycleViewItemLine;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/18
 * 描    述：小说阅读器主页面
 * 修订历史：
 * ================================================
 */
public class ReaderFindFragment extends BaseFragment {


    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private BookReaderActivity activity;
    private List<FindBean> mList = new ArrayList<>();
    private ReaderFindAdapter adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (BookReaderActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public int getContentView() {
        return R.layout.fragment_reader_find;
    }

    @Override
    public void initView() {
        initViewData();
        initRecycleView();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }


    private void initViewData() {
        mList.clear();
        mList.add(new FindBean("排行榜", R.drawable.home_find_rank));
        mList.add(new FindBean("主题书单", R.drawable.home_find_topic));
        mList.add(new FindBean("分类", R.drawable.home_find_category));
        mList.add(new FindBean("有声小说", R.drawable.home_find_listen));
    }


    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        RecycleViewItemLine line = new RecycleViewItemLine(activity, LinearLayout.HORIZONTAL,
                SizeUtils.px2dp(2), activity.getResources().getColor(R.color.grayLine));
        recyclerView.addItemDecoration(line);
        adapter = new ReaderFindAdapter(mList,activity);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnListItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position){
                    case 0:
                        startActivity(ReaderTopBookActivity.class);
                        break;
                    case 1:
                        startActivity(ReaderSubjectActivity.class);
                        break;
                    case 2:
                        startActivity(ReaderCategoryActivity.class);
                        break;
                    case 3:

                        break;
                }
            }
        });
        adapter.setOnItemLongClickListener(new ReaderFindAdapter.OnItemLongClickListener() {
            @Override
            public void onLongClick(View v, int position) {

            }
        });
    }


}
