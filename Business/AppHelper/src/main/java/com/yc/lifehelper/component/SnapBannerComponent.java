package com.yc.lifehelper.component;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.yc.eastadapterlib.BaseRecycleAdapter;
import com.yc.eastadapterlib.BaseViewHolder;
import com.yc.imageserver.utils.GlideImageUtils;
import com.yc.library.api.ConstantImageApi;
import com.yc.library.api.ConstantStringApi;
import com.yc.library.bean.ListNewsData;
import com.yc.lifehelper.R;
import com.yc.snapbannerlib.ScrollLinearHelper;
import com.yc.toolutils.system.AppLogUtils;
import com.yc.toolutils.size.AppSizeUtils;

import org.yczbj.ycrefreshviewlib.inter.InterItemView;

import java.util.ArrayList;
import java.util.List;

public class SnapBannerComponent implements InterItemView {

    private RecyclerView recyclerView;
    private Context context;
    private SnapBannerAdapter adapter;

    @Override
    public View onCreateView(ViewGroup parent) {
        View headerView = View.inflate(parent.getContext(), R.layout.base_title_list_view, null);
        AppLogUtils.d("banner on create view");
        context = headerView.getContext();
        return headerView;
    }

    @Override
    public void onBindView(View header) {
        TextView tvTitle = header.findViewById(R.id.tv_title);
        recyclerView = header.findViewById(R.id.recyclerView);
        tvTitle.setText("热门活动");
        AppLogUtils.d("banner on bind view");
        if (adapter == null){
            initRecyclerView();
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        //该方法可以解决嵌套滑动冲突
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(manager);
        ScrollLinearHelper snapHelper = new ScrollLinearHelper();
        try {
            snapHelper.attachToRecyclerView(recyclerView);
        } catch (IllegalStateException e){
            e.printStackTrace();
        }
        //snapHelper.attachToRecyclerView(recyclerView);
        adapter = new SnapBannerAdapter(context);
        recyclerView.setAdapter(adapter);
        SpaceItemLine spaceViewItemLine = new SpaceItemLine();
        recyclerView.addItemDecoration(spaceViewItemLine);
        List<ListNewsData> newsDataList = new ArrayList<>();
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(0),"", ConstantImageApi.createSmallImage().get(0)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(1),"", ConstantImageApi.createSmallImage().get(1)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(2),"", ConstantImageApi.createSmallImage().get(2)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(3),"", ConstantImageApi.createSmallImage().get(3)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(4),"", ConstantImageApi.createSmallImage().get(4)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(5),"", ConstantImageApi.createSmallImage().get(5)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(0),"", ConstantImageApi.createSmallImage().get(0)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(1),"", ConstantImageApi.createSmallImage().get(1)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(2),"", ConstantImageApi.createSmallImage().get(2)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(3),"", ConstantImageApi.createSmallImage().get(3)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(4),"", ConstantImageApi.createSmallImage().get(4)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(5),"", ConstantImageApi.createSmallImage().get(5)));
        adapter.setData(newsDataList);
    }

    public static class SnapBannerAdapter extends BaseRecycleAdapter<ListNewsData> {

        public SnapBannerAdapter(Context context) {
            super(context, R.layout.item_snap_view);
        }

        @Override
        protected void bindData(BaseViewHolder holder, ListNewsData data) {
            TextView tv_title = holder.getView(R.id.tv_title);
            ImageView iv_image = holder.getView(R.id.iv_image);
            tv_title.setText(data.getTitle());
            GlideImageUtils.loadImageLocal(context,data.getImage(),iv_image);
            //iv_image.setBackgroundResource(data.getImage());
        }
    }
    
    private static class SpaceItemLine extends RecyclerView.ItemDecoration{
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull  View view,
                                   @NonNull RecyclerView parent,
                                   @NonNull  RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int position = parent.getChildAdapterPosition(view);
            outRect.left = AppSizeUtils.dp2px(parent.getContext(),20);
            RecyclerView.Adapter adapter = parent.getAdapter();
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            if (adapter == null || layoutManager == null){
                return;
            }
            if (position == layoutManager.getItemCount()){
                outRect.right = AppSizeUtils.dp2px(parent.getContext(),20);
            }
        }
    }
}
