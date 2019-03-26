package com.ycbjie.zhihu.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.ycbjie.library.inter.listener.OnListItemClickListener;
import com.ycbjie.zhihu.R;
import com.ycbjie.zhihu.model.ZhiHuDailyBeforeListBean;
import com.ycbjie.zhihu.model.ZhiHuDailyListBean;
import com.ycbjie.library.utils.image.ImageUtils;
import com.yc.cn.ycbannerlib.banner.BannerView;

import java.util.List;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/10/29
 *     desc  : 知乎日报模块           日报
 *     revise:
 * </pre>
 */
public class ZhiHuDailyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater inflater;
    private Context mContext;
    private List<ZhiHuDailyListBean.StoriesBean> mList;
    private String currentTitle = "今日热闻";
    private OnListItemClickListener onItemClickListener;
    private List<ZhiHuDailyListBean.TopStoriesBean> mTopList;
    private boolean isBefore = false;
    private BannerView banner;
    private BannerPagerAdapter mAdapter;

    private enum ITEM_TYPE {
        //滚动栏
        ITEM_TOP,
        //日期
        ITEM_DATE,
        //内容
        ITEM_CONTENT
    }


    public ZhiHuDailyAdapter(Context activity, List<ZhiHuDailyListBean.StoriesBean> mList) {
        this.mList = mList;
        this.mContext = activity;
        inflater = LayoutInflater.from(activity);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.ITEM_TOP.ordinal()) {
            mAdapter = new BannerPagerAdapter(mContext, mTopList);
            return new TopViewHolder(inflater.inflate(R.layout.base_wrap_banner,
                    parent, false));
        } else if (viewType == ITEM_TYPE.ITEM_DATE.ordinal()) {
            return new DateViewHolder(inflater.inflate(R.layout.base_title_view,
                    parent, false));
        }
        return new ContentViewHolder(inflater.inflate(R.layout.item_zh_news_list,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ContentViewHolder) {
            final int contentPosition;
            if(isBefore) {
                contentPosition = position - 1;
            } else {
                contentPosition = position - 2;
            }
            ((ContentViewHolder)holder).tvTitle.setText(mList.get(contentPosition).getTitle());
            ImageUtils.loadImgByPicasso(mContext,mList.get(contentPosition).getImages().get(0)
                    ,R.drawable.image_default,((ContentViewHolder)holder).ivLogo);
            holder.itemView.setOnClickListener(view -> {
                if(onItemClickListener != null) {
                    onItemClickListener.onItemClick(view,contentPosition);
                }
            });
        } else if (holder instanceof DateViewHolder) {
            ((DateViewHolder) holder).tvZhTitle.setText(currentTitle);
        } else {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.height = SizeUtils.dp2px(100);
            ((TopViewHolder) holder).banner.setLayoutParams(layoutParams);
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
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvTime = itemView.findViewById(R.id.tv_time);
            ivLogo = itemView.findViewById(R.id.iv_logo);
        }
    }


    private class DateViewHolder extends RecyclerView.ViewHolder {
        TextView tvZhTitle;
        TextView tvZhTime;
        DateViewHolder(View itemView) {
            super(itemView);
            tvZhTitle = itemView.findViewById(R.id.tv_zh_title);
            tvZhTime = itemView.findViewById(R.id.tv_zh_time);
        }
    }



    private class TopViewHolder extends RecyclerView.ViewHolder {
        BannerView banner;
        TopViewHolder(View itemView) {
            super(itemView);
            banner = itemView.findViewById(R.id.banner);
        }
    }


    /**
     * 添加今天日期新闻的数据
     * @param zhiHuDailyBean      数据
     */
    public void addDailyDate(ZhiHuDailyListBean zhiHuDailyBean) {
        currentTitle = "今日热闻";
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(
                new ZhihuDiffCallback(mList, zhiHuDailyBean.getStories()), true);
        mList = zhiHuDailyBean.getStories();
        mTopList = zhiHuDailyBean.getTop_stories();
        isBefore = false;
        diffResult.dispatchUpdatesTo(this);
    }


    /**
     * 添加之前日期新闻的数据
     * @param zhiHuDailyBeforeListBean      数据
     */
    public void addDailyBeforeDate(ZhiHuDailyBeforeListBean zhiHuDailyBeforeListBean) {
        currentTitle = zhiHuDailyBeforeListBean.getDate();
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(
                new ZhihuDiffCallback(mList, zhiHuDailyBeforeListBean.getStories()), true);
        mList = zhiHuDailyBeforeListBean.getStories();
        isBefore = true;
        diffResult.dispatchUpdatesTo(this);
    }


    /**
     * 设置阅读的状态，记录位置
     * @param position                      索引
     * @param readState                     是否阅读
     */
    public void setReadState(int position, boolean readState) {
        mList.get(position).setReadState(readState);
    }


    /**
     * 是否是之前的
     * @return
     */
    public boolean getIsBefore() {
        return isBefore;
    }


    /**
     * 设置轮播图滑动位置
     * @param currentCount          索引
     */
    public void changeTopPager(int currentCount) {
        if(!isBefore && banner != null) {
            banner.getViewPager().setCurrentItem(currentCount);
        }
    }


    public void setOnItemClickListener(OnListItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }



}
