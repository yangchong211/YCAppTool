
package com.ns.yc.lifehelper.ui.other.bookReader.view.adapter;

import android.content.Context;
import android.view.View;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.constantApi.ConstantZssqApi;
import com.ns.yc.lifehelper.inter.listener.NoDoubleClickListener;
import com.ns.yc.lifehelper.inter.listener.OnRvItemClickListener;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.ReaderDetailRecommend;
import com.ns.yc.lifehelper.ui.other.bookReader.manager.SettingManager;

import java.util.List;


public class RecommendBookListAdapter extends EasyRVAdapter<ReaderDetailRecommend.BooklistsBean> {

    private OnRvItemClickListener itemClickListener;

    public RecommendBookListAdapter(Context context, List<ReaderDetailRecommend.BooklistsBean> list, OnRvItemClickListener listener) {
        super(context, list, R.layout.item_book_detail_recommend_book_list);
        this.itemClickListener = listener;
    }

    @Override
    protected void onBindData(final EasyRVHolder holder, final int position, final ReaderDetailRecommend.BooklistsBean item) {
        if (!SettingManager.getInstance().isNoneCover()) {
            holder.setRoundImageUrl(R.id.ivBookListCover, ConstantZssqApi.IMG_BASE_URL + item.getCover(), R.drawable.cover_default);
        }

        holder.setText(R.id.tvBookListTitle, item.getTitle())
                .setText(R.id.tvBookAuthor, item.getAuthor())
                .setText(R.id.tvBookListTitle, item.getTitle())
                .setText(R.id.tvBookListDesc, item.getDesc())
                .setText(R.id.tvBookCount, String.format(mContext.getString(R.string
                        .book_detail_recommend_book_list_book_count), item.getBookCount()))
                .setText(R.id.tvCollectorCount, String.format(mContext.getString(R.string
                        .book_detail_recommend_book_list_collector_count), item.getCollectorCount()));
        holder.setOnItemViewClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                itemClickListener.onItemClick(holder.getItemView(), position, item);
            }
        });
    }

}