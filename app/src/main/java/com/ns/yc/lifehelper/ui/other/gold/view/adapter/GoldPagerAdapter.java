package com.ns.yc.lifehelper.ui.other.gold.view.adapter;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.ui.other.gold.model.GoldListBean;
import com.ns.yc.lifehelper.utils.image.ImageUtils;

import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.viewHolder.BaseViewHolder;

public class GoldPagerAdapter extends RecyclerArrayAdapter<GoldListBean> {

    public GoldPagerAdapter(Activity activity) {
        super(activity);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExpressDeliveryViewHolder(parent);
    }

    private class ExpressDeliveryViewHolder extends BaseViewHolder<GoldListBean> {

        ImageView iv_logo;
        TextView tv_title , tv_time ;

        ExpressDeliveryViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_tx_news_list);
            tv_title = $(R.id.tv_title);
            tv_time = $(R.id.tv_time);
            iv_logo = $(R.id.iv_logo);
        }

        @Override
        public void setData(GoldListBean data) {
            super.setData(data);
            if(data!=null){
                tv_title.setText(data.getTitle());
                tv_time.setText(getItemInfoStr(data.getCollectionCount(),
                        data.getCommentsCount(),
                        data.getUser().getUsername(),
                        data.getCreatedAt()));
                ImageUtils.loadImgByPicasso(getContext(), data.getScreenshot().getUrl(),R.drawable.image_default,iv_logo);
            }
        }

        private String  getItemInfoStr(int likeNum, int cmtNum, String author, String time) {
            StringBuilder sb = new StringBuilder(String.valueOf(likeNum));
            sb.append("人收藏 · ");
            sb.append(cmtNum);
            sb.append("条评论 · ");
            sb.append(author);
            sb.append(" · ");
            sb.append(time);
            return sb.toString();
        }
    }
}
