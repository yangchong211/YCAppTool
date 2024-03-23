package com.yc.lifehelper.component;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.yc.eastadapterlib.BaseRecycleAdapter;
import com.yc.eastadapterlib.BaseViewHolder;
import com.yc.library.api.ConstantImageApi;
import com.yc.library.api.ConstantStringApi;
import com.yc.library.bean.ListNewsData;
import com.yc.lifehelper.R;
import com.yc.toolutils.system.AppLogUtils;
import com.yc.toolutils.time.AppTimeUtils;

import org.yczbj.ycrefreshviewlib.inter.InterItemView;
import org.yczbj.ycrefreshviewlib.item.RecycleViewItemLine;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VideoListComponent implements InterItemView {

    private RecyclerView recyclerView;
    private Context context;

    @Override
    public View onCreateView(ViewGroup parent) {
        View headerView = View.inflate(parent.getContext(), R.layout.base_recycler_view, null);
        AppLogUtils.d("banner on create view");
        context = headerView.getContext();
        return headerView;
    }

    @Override
    public void onBindView(View header) {
        recyclerView = header.findViewById(R.id.recyclerView);
        AppLogUtils.d("banner on bind view");
        initRecyclerView();
    }

    private void initRecyclerView() {
        ListNewsAdapter newsAdapter = new ListNewsAdapter(context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(newsAdapter);
        final RecycleViewItemLine line = new RecycleViewItemLine(context, LinearLayout.HORIZONTAL,
                1, Color.parseColor("#e5e5e5"));
        recyclerView.addItemDecoration(line);
        List<ListNewsData> newsDataList = new ArrayList<>();
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(0), "", ConstantImageApi.createSmallImage().get(0)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(1), "", ConstantImageApi.createSmallImage().get(1)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(2), "", ConstantImageApi.createSmallImage().get(2)));
        newsAdapter.setData(newsDataList);
    }

    public static class ListNewsAdapter extends BaseRecycleAdapter<ListNewsData> {

        public ListNewsAdapter(Context context) {
            super(context, R.layout.item_news_base_view);
        }

        @Override
        protected void bindData(BaseViewHolder holder, ListNewsData data) {
            TextView tv_title = holder.getView(R.id.tv_title);
            TextView tv_time = holder.getView(R.id.tv_time);
            ImageView iv_logo = holder.getView(R.id.iv_logo);
            tv_title.setText(data.getTitle());
            tv_time.setText(AppTimeUtils.date2String(new Date()));
            iv_logo.setBackgroundResource(data.getImage());
        }

    }

}
