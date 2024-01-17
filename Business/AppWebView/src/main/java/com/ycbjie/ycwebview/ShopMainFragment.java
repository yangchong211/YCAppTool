package com.ycbjie.ycwebview;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class ShopMainFragment extends Fragment {

    private ScrollView mScrollView;
    private TextView mTvGoodsTitle;
    private TextView mTvNewPrice;
    private TextView mTvOldPrice;
    private LinearLayout mLlActivity;
    private LinearLayout mLlCurrentGoods;
    private TextView mTvCurrentGoods;
    private ImageView mIvEnsure;
    private LinearLayout mLlComment;
    private TextView mTvCommentCount;
    private TextView mTvGoodComment;
    private ImageView mIvCommentRight;
    private LinearLayout mLlEmptyComment;
    private LinearLayout mLlRecommend;
    private TextView mTvBottomView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getContentView(), container , false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private int getContentView() {
        return R.layout.include_shop_main;
    }


    private void initView(View view) {
        mScrollView = view.findViewById(R.id.scrollView);
        mTvGoodsTitle = view.findViewById(R.id.tv_goods_title);
        mTvNewPrice = view.findViewById(R.id.tv_new_price);
        mTvOldPrice = view.findViewById(R.id.tv_old_price);
        mLlCurrentGoods = view.findViewById(R.id.ll_current_goods);
        mTvCurrentGoods = view.findViewById(R.id.tv_current_goods);
        mIvEnsure = view.findViewById(R.id.iv_ensure);
        mLlComment = view.findViewById(R.id.ll_comment);
        mTvCommentCount = view.findViewById(R.id.tv_comment_count);
        mTvGoodComment = view.findViewById(R.id.tv_good_comment);
        mIvCommentRight = view.findViewById(R.id.iv_comment_right);
        mLlEmptyComment = view.findViewById(R.id.ll_empty_comment);
        mLlRecommend = view.findViewById(R.id.ll_recommend);
        mTvBottomView = view.findViewById(R.id.tv_bottom_view);

    }


    public void changBottomView(boolean isDetail) {
        if(isDetail){
            mTvBottomView.setText("下拉回到商品详情");
        }else {
            mTvBottomView.setText("继续上拉，查看图文详情");
        }
    }
}
