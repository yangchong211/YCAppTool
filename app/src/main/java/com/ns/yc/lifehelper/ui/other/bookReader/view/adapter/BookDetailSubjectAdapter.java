package com.ns.yc.lifehelper.ui.other.bookReader.view.adapter;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.constantApi.ConstantZssqApi;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.DetailBookSubjectList;
import com.ns.yc.lifehelper.ui.other.bookReader.manager.SettingManager;
import com.ns.yc.lifehelper.utils.image.ImageUtils;

import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.viewHolder.BaseViewHolder;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/28
 * 描    述：小说阅读器主题书单详情页面适配器
 * 修订历史：
 * ================================================
 */
public class BookDetailSubjectAdapter extends RecyclerArrayAdapter<DetailBookSubjectList.BookListBean.BooksBean> {

    private Activity activity;

    public BookDetailSubjectAdapter(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExpressDeliveryViewHolder(parent);
    }


    private class ExpressDeliveryViewHolder extends BaseViewHolder<DetailBookSubjectList.BookListBean.BooksBean> {

        ImageView ivBookCover;
        TextView tvBookListTitle , tvBookAuthor , tvBookLatelyFollower , tvBookWordCount ,tvBookDetail;

        ExpressDeliveryViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_subject_book_list_detail);
            ivBookCover = $(R.id.ivBookCover);
            tvBookListTitle = $(R.id.tvBookListTitle);
            tvBookAuthor = $(R.id.tvBookAuthor);
            tvBookLatelyFollower = $(R.id.tvBookLatelyFollower);
            tvBookWordCount = $(R.id.tvBookWordCount);
            tvBookDetail = $(R.id.tvBookDetail);
        }

        @Override
        public void setData(DetailBookSubjectList.BookListBean.BooksBean data) {
            super.setData(data);
            if (!SettingManager.getInstance().isNoneCover()) {
                ImageUtils.loadImgByPicassoPerson(activity,
                        ConstantZssqApi.IMG_BASE_URL+data.getBook().getAuthor(),R.drawable.avatar_default,ivBookCover);
            } else {
                ivBookCover.setImageResource(R.drawable.cover_default);
            }

            tvBookListTitle.setText(data.getBook().getTitle());
            tvBookAuthor.setText(data.getBook().getAuthor());
            String follower = String.format(getContext().getResources().getString(R.string.subject_book_list_detail_book_lately_follower),
                    data.getBook().getLatelyFollower());
            tvBookLatelyFollower.setText(follower);
            String wordCount = String.format(getContext().getResources().getString(R.string.subject_book_list_detail_book_word_count),
                    data.getBook().getWordCount() / 10000);
            tvBookWordCount.setText(wordCount);
            tvBookDetail.setText(data.getBook().getLongIntro());

        }

    }
}
