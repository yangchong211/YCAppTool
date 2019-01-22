package com.ns.yc.lifehelper.ui.home.view.adapter;

import android.app.Activity;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ycbjie.library.model.HomeBlogEntity;
import com.ycbjie.library.utils.ImageUtils;
import com.ycbjie.library.utils.spannable.SpannableUtils;

import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.viewHolder.BaseViewHolder;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/03/22
 *     desc  : Home主页面适配器
 *     revise:
 * </pre>
 */
public class HomeBlogAdapter extends RecyclerArrayAdapter<HomeBlogEntity> {


    public HomeBlogAdapter(Activity activity) {
        super(activity);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(parent);
    }


    public class MyViewHolder extends BaseViewHolder<HomeBlogEntity> {

        LinearLayout llNewsHead;
        TextView tvTitle;
        TextView tvAuthor;
        TextView tvTime;
        ImageView ivImg;
        LinearLayout llNewContent;

        MyViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_home_list);
            llNewsHead = getView(R.id.ll_news_head);
            tvTitle = getView(R.id.tv_title);
            tvAuthor = getView(R.id.tv_author);
            tvTime = getView(R.id.tv_time);
            ivImg = getView(R.id.iv_img);
            llNewContent = getView(R.id.ll_new_content);
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
                ImageUtils.loadImgByPicasso(getContext(), data.getImageUrl(), R.drawable.image_default, ivImg);
                tvAuthor.setText(data.getAuthor());
                tvTime.setText(data.getTime());
                if(getAdapterPosition()==2||getAdapterPosition()==5||getAdapterPosition()==7){
                    SpannableStringBuilder string = SpannableUtils.appendString(
                            getContext(), "杨充", data.getTitle());
                    tvTitle.setText(string);
                }else {
                    tvTitle.setText(data.getTitle());
                }
            }
        }
    }
}
