package com.ns.yc.lifehelper.ui.other.myPicture.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.LayoutRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.ui.other.myPicture.bean.PicBeautifulMainBean;
import com.ns.yc.lifehelper.weight.imageView.RadioImageView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/1
 * 描    述：美图欣赏适配器
 * 修订历史：
 * ================================================
 */
public abstract class PicBeautifulAdapter extends ArrayRecyclerAdapter<PicBeautifulMainBean, PicBeautifulAdapter.ViewHolder>{

    private final Context context;
    private final LayoutInflater inflater;

    public PicBeautifulAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        setHasStableIds(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(R.layout.item_beautiful_pic, parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PicBeautifulMainBean bean = get(position);
        holder.imageView.setOriginalSize(bean.getWidth(), bean.getHeight());
        Picasso.with(context)
                .load(bean.getImageurl())
                .tag("1")
                .config(Bitmap.Config.RGB_565).
                into(holder.imageView);
        holder.title.setText(bean.getTitle());
        ViewCompat.setTransitionName(holder.imageView, bean.getUrl());
    }

    @Override
    public long getItemId(int position) {
        return get(position).getUrl().hashCode();
    }

    protected abstract void onItemClick(View v, int position);

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_image)
        public RadioImageView imageView;
        @BindView(R.id.tv_title)
        public TextView title;

        public ViewHolder(@LayoutRes int resource, ViewGroup parent) {
            super(inflater.inflate(resource, parent, false));
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(v, getAdapterPosition());
                }
            });
        }

    }


}
