package com.ns.yc.lifehelper.ui.other.bookReader.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.constantApi.ConstantZssqApi;
import com.ns.yc.lifehelper.inter.listener.OnRvItemClickListener;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.ReaderTopBookBean;
import com.ns.yc.lifehelper.utils.image.ImageUtils;

import java.util.List;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/19
 * 描    述：小说阅读器排行版适配器
 * 修订历史：
 * ================================================
 */
public class TopReaderAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private LayoutInflater inflater;

    private List<ReaderTopBookBean.MaleBean> groupArray;
    private List<List<ReaderTopBookBean.MaleBean>> childArray;

    private OnRvItemClickListener<ReaderTopBookBean.MaleBean> listener;

    public TopReaderAdapter(Context context, List<ReaderTopBookBean.MaleBean> groupArray, List<List<ReaderTopBookBean.MaleBean>> childArray) {
        this.childArray = childArray;
        this.groupArray = groupArray;
        mContext = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return groupArray.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childArray.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupArray.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childArray.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final View group = inflater.inflate(R.layout.item_top_rank_group, null);

        ImageView ivCover = (ImageView) group.findViewById(R.id.ivRankCover);
        if (!TextUtils.isEmpty(groupArray.get(groupPosition).getCover())) {
            String url = ConstantZssqApi.IMG_BASE_URL + groupArray.get(groupPosition).getCover();
            ImageUtils.loadImgByPicassoPerson(mContext,url,R.drawable.avatar_default,ivCover);
            group.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(group, groupPosition, groupArray.get(groupPosition));
                    }
                }
            });
        } else {
            ivCover.setImageResource(R.drawable.ic_rank_collapse);
        }

        TextView tvName = (TextView) group.findViewById(R.id.tvRankGroupName);
        tvName.setText(groupArray.get(groupPosition).getTitle());

        ImageView ivArrow = (ImageView) group.findViewById(R.id.ivRankArrow);
        if (childArray.get(groupPosition).size() > 0) {
            if (isExpanded) {
                ivArrow.setImageResource(R.drawable.rank_arrow_up);
            } else {
                ivArrow.setImageResource(R.drawable.rank_arrow_down);
            }
        } else {
            ivArrow.setVisibility(View.GONE);
        }
        return group;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final View child = inflater.inflate(R.layout.item_top_rank_child, null);

        TextView tvName = (TextView) child.findViewById(R.id.tvRankChildName);
        tvName.setText(childArray.get(groupPosition).get(childPosition).getTitle());

        child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(child, childPosition, childArray.get(groupPosition).get(childPosition));
            }
        });
        return child;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void setItemClickListener(OnRvItemClickListener<ReaderTopBookBean.MaleBean> listener) {
        this.listener = listener;
    }

}
