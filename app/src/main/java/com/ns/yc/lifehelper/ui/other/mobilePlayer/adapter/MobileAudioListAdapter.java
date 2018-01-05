package com.ns.yc.lifehelper.ui.other.mobilePlayer.adapter;

import android.app.Activity;
import android.text.format.Formatter;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.utils.localFile.bean.AudioItem;
import com.ns.yc.lifehelper.utils.ImageUtils;

import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.viewHolder.BaseViewHolder;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/31
 * 描    述：音乐播放器页面适配器
 * 修订历史：
 * ================================================
 */
public class MobileAudioListAdapter extends RecyclerArrayAdapter<AudioItem> {

    public MobileAudioListAdapter(Activity activity) {
        super(activity);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExpressDeliveryViewHolder(parent);
    }


    private class ExpressDeliveryViewHolder extends BaseViewHolder<AudioItem> {

        ImageView iv_image;
        TextView tv_title , tv_size ,tv_duration;

        ExpressDeliveryViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_video_list);
            iv_image = $(R.id.iv_image);
            tv_title = $(R.id.tv_title);
            tv_size = $(R.id.tv_size);
            tv_duration = $(R.id.tv_duration);
        }

        @Override
        public void setData(AudioItem data) {
            super.setData(data);
            ImageUtils.loadImgByPicasso(getContext(),R.drawable.mobile_audio_default_icon,iv_image);
            tv_title.setText(data.getName());
            tv_size.setText(Formatter.formatFileSize(getContext(), data.getSize()));
            tv_duration.setText(data.getAlbum());
        }
    }
}
