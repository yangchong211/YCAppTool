package com.ns.yc.lifehelper.ui.other.bookReader.view.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.comment.ConstantBookReader;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.ReaderRecommendBean;
import com.ns.yc.lifehelper.ui.other.bookReader.manager.SettingManager;
import com.ns.yc.lifehelper.utils.FileUtils;

import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.viewHolder.BaseViewHolder;

import java.text.NumberFormat;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：22017/9/29
 * 描    述：扫描本地书籍页面适配器
 * 修订历史：
 * ================================================
 */
public class ReaderLocalAdapter extends RecyclerArrayAdapter<ReaderRecommendBean.RecommendBooks> {

    private Activity activity;

    public ReaderLocalAdapter(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExpressDeliveryViewHolder(parent);
    }


    private class ExpressDeliveryViewHolder extends BaseViewHolder<ReaderRecommendBean.RecommendBooks> {

        ImageView ivRecommendCover ,ivTopLabel ,ivUnReadDot;
        TextView tvRecommendTitle , tvLatelyUpdate , tvRecommendShort ;
        CheckBox ckBoxSelect;

        ExpressDeliveryViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_reader_recommend_list);
            ivRecommendCover = $(R.id.ivRecommendCover);
            tvRecommendTitle = $(R.id.tvRecommendTitle);
            tvLatelyUpdate = $(R.id.tvLatelyUpdate);
            tvRecommendShort = $(R.id.tvRecommendShort);
            ivTopLabel = $(R.id.ivTopLabel);
            ckBoxSelect = $(R.id.ckBoxSelect);
            ivUnReadDot = $(R.id.ivUnReadDot);
        }

        @Override
        public void setData(ReaderRecommendBean.RecommendBooks data) {
            super.setData(data);
            tvRecommendTitle.setText(data.getTitle());
            //tvLatelyUpdate.setText(data.getTitle());
            tvRecommendShort.setText(data.getLastChapter());
            ivTopLabel.setVisibility(View.GONE);
            ckBoxSelect.setVisibility(View.GONE);
            ivUnReadDot.setVisibility(View.GONE);

            if (data.getPath() != null && data.getPath().endsWith(ConstantBookReader.SUFFIX_PDF)) {
                ivRecommendCover.setImageResource(R.drawable.ic_shelf_pdf);
            } else if (data.getPath() != null && data.getPath().endsWith(ConstantBookReader.SUFFIX_EPUB)) {
                ivRecommendCover.setImageResource(R.drawable.ic_shelf_epub);
            } else if (data.getPath() != null && data.getPath().endsWith(ConstantBookReader.SUFFIX_CHM)) {
                ivRecommendCover.setImageResource(R.drawable.ic_shelf_chm);
            } else if (data.isFromSD()) {
                ivRecommendCover.setImageResource(R.drawable.ic_shelf_txt);
                long fileLen = FileUtils.getChapterFile(data._id, 1).length();
                if (fileLen > 10) {
                    double progress = ((double) SettingManager.getInstance().getReadProgress(data._id)[2]) / fileLen;
                    NumberFormat fmt = NumberFormat.getPercentInstance();
                    fmt.setMaximumFractionDigits(2);
                    tvRecommendShort.setText("当前阅读进度：" + fmt.format(progress));
                }
            } else if (!SettingManager.getInstance().isNoneCover()) {

            } else {
                ivRecommendCover.setImageResource(R.drawable.cover_default);
            }

        }
    }
}
