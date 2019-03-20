package com.ns.yc.lifehelper.ui.home.view.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.Utils;
import com.ns.yc.lifehelper.R;
import com.ycbjie.library.model.HomeBlogEntity;
import com.ycbjie.library.utils.image.ImageUtils;
import com.ycbjie.library.utils.spannable.RoundBackgroundSpan;
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
            super(parent, R.layout.item_news_list);
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
                if(getAdapterPosition()==2){
                    SpannableStringBuilder string = SpannableUtils.appendString(
                            getContext(), "杨充", data.getTitle());
                    tvTitle.setText(string);
                }else if(getAdapterPosition()==3 || getAdapterPosition()==4){
                    String text1 = "杨充";
                    String text2 = "潇湘剑雨";
                    String titleContent = text1 + text2 + data.getTitle();
                    SpannableString titleSpannable = new SpannableString(titleContent);
                    // 杨充
                    RoundBackgroundSpan exclusiveSpannable = new RoundBackgroundSpan(
                            Color.parseColor("#f25057"),
                            Color.parseColor("#f25057"),
                            Color.parseColor("#ffffff"),
                            Utils.getApp().getResources().getDimensionPixelOffset(R.dimen.dp1),
                            Utils.getApp().getResources().getDimensionPixelOffset(R.dimen.dp4));
                    spannable(titleSpannable, exclusiveSpannable, 0, text1.length());

                    // text2 不等于空 在绘制text
                    if (!TextUtils.isEmpty(text2)) {
                        int bgColor = Color.parseColor("#ffffff");
                        int borderColor = Color.parseColor("#ff3d51");
                        int textColor = Color.parseColor("#f25057");
                        RoundBackgroundSpan spannable = new RoundBackgroundSpan(bgColor, borderColor, textColor,
                                Utils.getApp().getResources().getDimensionPixelOffset(R.dimen.dp1),
                                Utils.getApp().getResources().getDimensionPixelOffset(R.dimen.dp4));
                        spannable(titleSpannable, spannable, 2, 2 + text2.length());
                    }
                    //tvTitle.setText(titleSpannable.toString());
                    //用下面这种，否则span不会生效
                    tvTitle.setText(titleSpannable);
                }else {
                    tvTitle.setText(data.getTitle());
                }
            }
        }
    }


    private void spannable(SpannableString titleSpannable,RoundBackgroundSpan spannable,
                          int start,int end) {
        titleSpannable.setSpan(spannable, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        titleSpannable.setSpan(new AbsoluteSizeSpan(Utils.getApp().getResources().getDimensionPixelSize(
                R.dimen.textSize9)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
}
