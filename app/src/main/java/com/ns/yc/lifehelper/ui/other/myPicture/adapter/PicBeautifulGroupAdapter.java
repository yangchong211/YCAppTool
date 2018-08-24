package com.ns.yc.lifehelper.ui.other.myPicture.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.LayoutRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.ui.other.myPicture.bean.PicBeautifulContentBean;
import com.ns.yc.lifehelper.weight.imageView.RadioImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.concurrent.CopyOnWriteArrayList;

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
public abstract class PicBeautifulGroupAdapter extends ArrayRecyclerAdapter<PicBeautifulContentBean, PicBeautifulGroupAdapter.ViewHolder>{

    private final Context context;
    private final LayoutInflater inflater;

    public PicBeautifulGroupAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        setHasStableIds(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(R.layout.item_beautiful_group_pic, parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PicBeautifulContentBean image = get(position);
        holder.iv_item.setOriginalSize(image.getImagewidth(), image.getImageheight());
        Picasso.with(context).load(image.getUrl()).tag("1").config(Bitmap.Config.RGB_565).
                transform(new CopyOnWriteArrayList<Transformation>()).
                into(holder.iv_item);
        holder.iv_item.setTag(image.getUrl());
        ViewCompat.setTransitionName(holder.iv_item, image.getUrl());
    }

    @Override
    public long getItemId(int position) {
        return get(position).getUrl().hashCode();
    }

    protected abstract void onItemClick(View v, int position);

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_item)
        public RadioImageView iv_item;

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
