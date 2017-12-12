package com.ns.yc.lifehelper.ui.other.zhihu.ui.adapter;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.ui.other.zhihu.model.bean.ZhiHuDailyBean;
import com.ns.yc.lifehelper.utils.ImageUtils;
import com.yc.cn.ycbannerlib.first.BannerView;

import java.util.List;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/11/29
 * 描    述：知乎日报模块        日报
 * 修订历史：
 * ================================================
 */
public class ZhiHuDailyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater inflater;
    private Context mContext;
    private List<ZhiHuDailyBean.StoriesBean> mList;
    private String currentTitle = "今日热闻";
    private OnItemClickListener onItemClickListener;
    private List<ZhiHuDailyBean.TopStoriesBean> mTopList;
    private boolean isBefore = false;
    private BannerView banner;
    private BannerPagerAdapter mAdapter;

    private enum ITEM_TYPE {
        ITEM_TOP,       //滚动栏
        ITEM_DATE,      //日期
        ITEM_CONTENT    //内容
    }


    public ZhiHuDailyAdapter(Context activity, List<ZhiHuDailyBean.StoriesBean> mList) {
        this.mList = mList;
        this.mContext = activity;
        inflater = LayoutInflater.from(activity);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.ITEM_TOP.ordinal()) {
            mAdapter = new BannerPagerAdapter(mContext, mTopList);
            return new TopViewHolder(inflater.inflate(R.layout.item_zh_daily_banner, parent, false));
        } else if (viewType == ITEM_TYPE.ITEM_DATE.ordinal()) {
            return new DateViewHolder(inflater.inflate(R.layout.item_zh_daily_date, parent, false));
        }
        return new ContentViewHolder(inflater.inflate(R.layout.item_tx_news_list, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ContentViewHolder) {
            final int contentPosition;
            if(isBefore) {
                contentPosition = position - 1;
            } else {
                contentPosition = position - 2;
            }
            ((ContentViewHolder)holder).tvTitle.setText(mList.get(contentPosition).getTitle());
            ImageUtils.loadImgByPicasso(mContext,mList.get(contentPosition).getImages().get(0),R.drawable.image_default,((ContentViewHolder)holder).ivLogo);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onItemClickListener != null) {
                        onItemClickListener.onItemClick(contentPosition,view);
                    }
                }
            });
        } else if (holder instanceof DateViewHolder) {
            ((DateViewHolder) holder).tvZhTitle.setText(currentTitle);
        } else {
            ((TopViewHolder) holder).banner.setAdapter(mAdapter);
            banner = ((TopViewHolder) holder).banner;
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (!isBefore) {
            if (position == 0) {
                return ITEM_TYPE.ITEM_TOP.ordinal();
            } else if (position == 1) {
                return ITEM_TYPE.ITEM_DATE.ordinal();
            } else {
                return ITEM_TYPE.ITEM_CONTENT.ordinal();
            }
        } else {
            if (position == 0) {
                return ITEM_TYPE.ITEM_DATE.ordinal();
            } else {
                return ITEM_TYPE.ITEM_CONTENT.ordinal();
            }
        }
    }


    private class ContentViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvTime;
        ImageView ivLogo;

        ContentViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            ivLogo = (ImageView) itemView.findViewById(R.id.iv_logo);
        }
    }


    private class DateViewHolder extends RecyclerView.ViewHolder {

        TextView tvZhTitle;
        TextView tvZhTime;

        DateViewHolder(View itemView) {
            super(itemView);
            tvZhTitle = (TextView) itemView.findViewById(R.id.tv_zh_title);
            tvZhTime = (TextView) itemView.findViewById(R.id.tv_zh_time);
        }
    }

    private class TopViewHolder extends RecyclerView.ViewHolder {

        BannerView banner;

        TopViewHolder(View itemView) {
            super(itemView);
            banner = (BannerView) itemView.findViewById(R.id.banner);
        }
    }


    public void addDailyDate(ZhiHuDailyBean zhiHuDailyBean) {
        currentTitle = "今日热闻";
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ZhiHuDiffCallback(mList, zhiHuDailyBean.getStories()), true);
        mList = zhiHuDailyBean.getStories();
        mTopList = zhiHuDailyBean.getTop_stories();
        isBefore = false;
        diffResult.dispatchUpdatesTo(this);
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }


}
