package com.ns.yc.lifehelper.ui.home.view.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.model.bean.HomeBlogEntity;
import com.ns.yc.lifehelper.utils.image.ImageUtils;

import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.viewHolder.BaseViewHolder;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/21
 * 描    述：Home主页面适配器
 * 修订历史：
 * ================================================
 */
public class HomeBlogAdapter extends RecyclerArrayAdapter<HomeBlogEntity> {

    private Activity activity;

    public HomeBlogAdapter(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new HomeBlogViewHolder(parent);
    }


    public class HomeBlogViewHolder extends BaseViewHolder<HomeBlogEntity> {

        @Bind(R.id.ll_news_head)
        LinearLayout llNewsHead;
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.tv_author)
        TextView tvAuthor;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.tv_type)
        TextView tvType;
        @Bind(R.id.iv_img)
        ImageView ivImg;
        @Bind(R.id.ll_new_content)
        LinearLayout llNewContent;

        HomeBlogViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_home_list);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setData(HomeBlogEntity data) {
            super.setData(data);
            if (getAdapterPosition() == 0) {
                llNewContent.setVisibility(View.GONE);
                llNewsHead.setVisibility(View.VISIBLE);
            } else {
                llNewContent.setVisibility(View.VISIBLE);
                llNewsHead.setVisibility(View.GONE);
                tvTitle.setText(data.getTitle());
                ImageUtils.loadImgByPicassoError(activity, data.getImageUrl(), R.drawable.image_default, ivImg);
                tvAuthor.setText(data.getAuthor());
                tvTime.setText(data.getTime());
            }
        }
    }
}
