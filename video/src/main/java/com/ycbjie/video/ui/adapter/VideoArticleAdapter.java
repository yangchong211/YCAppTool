package com.ycbjie.video.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.ycbjie.library.utils.image.ImageUtils;
import com.ycbjie.video.R;
import com.ycbjie.video.model.MultiNewsArticleDataBean;

import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.viewHolder.BaseViewHolder;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/01/30
 *     desc  : 视频页面VideoArticleAdapter
 *     revise:
 * </pre>
 */
public class VideoArticleAdapter extends RecyclerArrayAdapter<MultiNewsArticleDataBean> {

    public VideoArticleAdapter(Activity activity) {
        super(activity);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(parent);
    }


    private class MyViewHolder extends BaseViewHolder<MultiNewsArticleDataBean> {

        ImageView ivMedia;
        TextView tvExtra;
        ImageView ivDots;
        LinearLayout header;
        TextView tvTitle;
        ImageView ivVideoImage;
        TextView tvVideoTime;
        LinearLayout content;

        MyViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_news_article_video);
            ivMedia = getView(R.id.iv_media);
            tvExtra = getView(R.id.tv_extra);
            ivDots = getView(R.id.iv_dots);
            header = getView(R.id.header);
            tvTitle = getView(R.id.tv_title);
            ivVideoImage = getView(R.id.iv_video_image);
            tvVideoTime = getView(R.id.tv_video_time);
            content = getView(R.id.content);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void setData(MultiNewsArticleDataBean item) {
            super.setData(item);
            if (null != item.getVideo_detail_info()) {
                if (null != item.getVideo_detail_info().getDetail_video_large_image()) {
                    String image = item.getVideo_detail_info().getDetail_video_large_image().getUrl();
                    if (!TextUtils.isEmpty(image)) {
                        ImageUtils.loadImgByPicasso(getContext(),image,
                                R.drawable.image_default,ivVideoImage);
                    }
                }
            } else {
                ivVideoImage.setImageResource(R.drawable.image_default);
            }

            if (null != item.getUser_info()) {
                String avatarUrl = item.getUser_info().getAvatar_url();
                if (!TextUtils.isEmpty(avatarUrl)) {
                    ImageUtils.loadImgByPicasso(getContext(),avatarUrl,
                            R.drawable.image_default,ivMedia);
                }
            }

            String tvTitle = item.getTitle();
            String tvSource = item.getSource();
            String tvCommentCount = item.getComment_count() + "评论";
            String tvDatetime = item.getBehot_time() + "";
            if (!TextUtils.isEmpty(tvDatetime)) {
                tvDatetime = TimeUtils.getFriendlyTimeSpanByNow(tvDatetime);
            }
            int videoDuration = item.getVideo_duration();
            String min = String.valueOf(videoDuration / 60);
            String second = String.valueOf(videoDuration % 10);
            if (Integer.parseInt(second) < 10) {
                second = "0" + second;
            }
            String tvVideoTime = min + ":" + second;
            this.tvTitle.setText(tvTitle);
            tvExtra.setText(tvSource + " - " + tvCommentCount + " - " + tvDatetime);
            this.tvVideoTime.setText(tvVideoTime);
        }
    }
}
