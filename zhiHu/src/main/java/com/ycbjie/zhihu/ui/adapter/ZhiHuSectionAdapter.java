package com.ycbjie.zhihu.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.ycbjie.zhihu.R;
import com.ycbjie.zhihu.model.ZhiHuSectionBean;
import com.ycbjie.library.utils.image.ImageUtils;
import java.util.List;


public class ZhiHuSectionAdapter extends RecyclerView.Adapter<ZhiHuSectionAdapter.ViewHolder>{

    private Context mContext;
    private List<ZhiHuSectionBean.DataBean> mList;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;

    public ZhiHuSectionAdapter(Context mContext, List<ZhiHuSectionBean.DataBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_zh_theme,parent,false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //Glide在加载GridView等时,由于ImageView和Bitmap实际大小不符合,第一次时加载可能会变形(我这里出现了放大),必须在加载前再次固定ImageView大小
        ViewGroup.LayoutParams lp = holder.themeBg.getLayoutParams();
        lp.width = ScreenUtils.getScreenWidth() / 2;
        lp.height = SizeUtils.dp2px(100);
        ImageUtils.loadImgByPicasso(mContext,mList.get(position).getThumbnail(),R.drawable.image_default , holder.themeBg);
        holder.themeKind.setText(mList.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(view ,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView themeBg;
        TextView themeKind;

        public ViewHolder(View itemView) {
            super(itemView);
            themeBg = itemView.findViewById(R.id.iv_theme_bg);
            themeKind = itemView.findViewById(R.id.tv_theme_kind);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view ,int position);
    }

}
