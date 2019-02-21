package com.ns.yc.lifehelper.ui.me.view.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ycbjie.library.arounter.ARouterConstant;
import com.ycbjie.library.arounter.ARouterUtils;
import com.ycbjie.library.constant.Constant;
import com.ycbjie.library.db.cache.CacheZhLike;
import com.ycbjie.library.utils.image.ImageUtils;
import java.util.List;



public class MeNewsLikeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private List<CacheZhLike> mList;
    private LayoutInflater inflater;

    private static final int TYPE_ARTICLE = 0;
    private static final int TYPE_GIRL = 1;

    public MeNewsLikeAdapter(Context mContext, List<CacheZhLike> mList) {
        this.mContext = mContext;
        this.mList = mList;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getItemViewType(int position) {
        if(mList.get(position).getType() == Constant.LikeType.TYPE_GIRL) {
            return TYPE_GIRL;
        } else {
            return TYPE_ARTICLE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ARTICLE) {
            return new ArticleViewHolder(inflater.inflate(R.layout.item_me_like_article, parent, false));
        } else {
            return new GirlViewHolder(inflater.inflate(R.layout.item_me_like_girl, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ArticleViewHolder) {
            ((ArticleViewHolder) holder).title.setText(mList.get(position).getTitle());
            switch (mList.get(position).getType()) {
                case Constant.LikeType.TYPE_ZHI_HU:
                    if (mList.get(position).getImage() != null) {
                        ImageUtils.loadImgByPicasso(mContext, mList.get(position).getImage(), R.drawable.image_default,((ArticleViewHolder) holder).image);
                    } else {
                        ((ArticleViewHolder) holder).image.setImageResource(R.mipmap.ic_launcher);
                    }
                    ((ArticleViewHolder) holder).from.setText("来自 知乎");
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            gotoDailyDetail(Integer.valueOf(mList.get(holder.getAdapterPosition()).getId()));
                        }
                    });
                    break;
//                case Constants.TYPE_ANDROID:
//                    ((ArticleViewHolder) holder).image.setImageResource(R.mipmap.ic_android);
//                    ((ArticleViewHolder) holder).from.setText("来自 干货");
//                    holder.itemView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            gotoTechDetail(mList.get(holder.getAdapterPosition()).getUrl(), null, mList.get(holder.getAdapterPosition()).getTitle()
//                                    ,mList.get(holder.getAdapterPosition()).getId(), Constants.TYPE_ANDROID);
//                        }
//                    });
//                    break;
//                case Constants.TYPE_IOS:
//                    ((ArticleViewHolder) holder).image.setImageResource(R.mipmap.ic_ios);
//                    ((ArticleViewHolder) holder).from.setText("来自 干货");
//                    holder.itemView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            gotoTechDetail(mList.get(holder.getAdapterPosition()).getUrl(), null, mList.get(holder.getAdapterPosition()).getTitle()
//                                    ,mList.get(holder.getAdapterPosition()).getId(), Constants.TYPE_IOS);
//                        }
//                    });
//                    break;
//                case Constants.TYPE_WEB:
//                    ((ArticleViewHolder) holder).image.setImageResource(R.mipmap.ic_web);
//                    ((ArticleViewHolder) holder).from.setText("来自 干货");
//                    holder.itemView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            gotoTechDetail(mList.get(holder.getAdapterPosition()).getUrl(), null ,mList.get(holder.getAdapterPosition()).getTitle()
//                                    ,mList.get(holder.getAdapterPosition()).getId(), Constants.TYPE_WEB);
//                        }
//                    });
//                    break;
//                case Constants.TYPE_WECHAT:
//                    ImageLoader.load(mContext, mList.get(position).getId(), ((ArticleViewHolder) holder).image);
//                    ((ArticleViewHolder) holder).from.setText("来自 微信");
//                    holder.itemView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            gotoTechDetail(mList.get(holder.getAdapterPosition()).getUrl(), mList.get(holder.getAdapterPosition()).getImage(), mList.get(holder.getAdapterPosition()).getTitle()
//                                    ,mList.get(holder.getAdapterPosition()).getId(), Constants.TYPE_WECHAT);
//                        }
//                    });
//                    break;
//                case Constants.TYPE_GOLD:
//                    if (mList.get(position).getImage() != null) {
//                        ImageLoader.load(mContext, mList.get(position).getImage(), ((ArticleViewHolder) holder).image);
//                    } else {
//                        ((ArticleViewHolder) holder).image.setImageResource(R.mipmap.ic_launcher);
//                    }
//                    ((ArticleViewHolder) holder).from.setText("来自 掘金");
//                    holder.itemView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            gotoTechDetail(mList.get(holder.getAdapterPosition()).getUrl(), mList.get(holder.getAdapterPosition()).getImage(), mList.get(holder.getAdapterPosition()).getTitle()
//                                    ,mList.get(holder.getAdapterPosition()).getId(), Constants.TYPE_GOLD);
//                        }
//                    });
//                    break;
//                case Constants.TYPE_VTEX:
//                    if (mList.get(position).getImage() != null) {
//                        ImageLoader.load(mContext, VtexPresenter.parseImg(mList.get(position).getImage()), ((ArticleViewHolder) holder).image);
//                    } else {
//                        ((ArticleViewHolder) holder).image.setImageResource(R.mipmap.ic_launcher);
//                    }
//                    ((ArticleViewHolder) holder).from.setText("来自 V2EX");
//                    holder.itemView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            gotoVtexDetail(mList.get(holder.getAdapterPosition()).getId());
//                        }
//                    });
//                    break;
                default:
                    break;
            }
        } else if(holder instanceof GirlViewHolder) {
            ImageUtils.loadImgByPicasso(mContext, mList.get(position).getImage(),R.drawable.image_default, ((GirlViewHolder) holder).image);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    gotoGirlDetail(mList.get(holder.getAdapterPosition()).getImage(),mList.get(holder.getAdapterPosition()).getId());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ArticleViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title;
        TextView from;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.iv_article_image);
            title = itemView.findViewById(R.id.tv_article_title);
            from = itemView.findViewById(R.id.tv_article_from);
        }

    }

    public static class GirlViewHolder extends RecyclerView.ViewHolder {

        ImageView image;

        public GirlViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.iv_girl_image);
        }
    }

    public void gotoDailyDetail(int id) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.ID,id);
        ARouterUtils.navigation(ARouterConstant.ACTIVITY_ZHI_HU_WEB_VIEW,bundle);
    }

//    public void gotoTechDetail(String url,String imgUrl, String title,String id,int type) {
//        TechDetailActivity.launch(new TechDetailActivity.Builder()
//                .setContext(mContext)
//                .setUrl(url)
//                .setImgUrl(imgUrl)
//                .setId(id)
//                .setTitle(title)
//                .setType(type));
//    }
//
//    public void gotoGirlDetail(String url,String id) {
//        Intent intent = new Intent();
//        intent.setClass(mContext, GirlDetailActivity.class);
//        intent.putExtra("url",url);
//        intent.putExtra("id",id);
//        mContext.startActivity(intent);
//    }
//
//    public void gotoVtexDetail(String topicId) {
//        Intent intent = new Intent();
//        intent.setClass(mContext, RepliesActivity.class);
//        intent.putExtra(Constants.IT_VTEX_TOPIC_ID,topicId);
//        mContext.startActivity(intent);
//    }
}
