package com.ns.yc.lifehelper.ui.other.myPicture.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.ui.weight.pileCard.FadeTransitionImageView;
import com.ns.yc.lifehelper.ui.weight.pileCard.HorizontalTransitionLayout;
import com.ns.yc.lifehelper.ui.weight.pileCard.ItemEntity;
import com.ns.yc.yccardviewlib.CardViewLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

import butterknife.Bind;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/30
 * 描    述：图片画廊欣赏【Android56 卡片滑动效果，项目2】
 * 修订历史：
 * ================================================
 */
public class MyPicPileActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.tv_title_left)
    TextView tvTitleLeft;
    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.iv_right_img)
    ImageView ivRightImg;
    @Bind(R.id.ll_search)
    FrameLayout llSearch;
    @Bind(R.id.tv_title_right)
    TextView tvTitleRight;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.countryView)
    HorizontalTransitionLayout countryView;
    @Bind(R.id.temperatureView)
    HorizontalTransitionLayout temperatureView;
    @Bind(R.id.pileLayout)
    CardViewLayout pileLayout;
    @Bind(R.id.addressView)
    HorizontalTransitionLayout addressView;
    @Bind(R.id.descriptionView)
    TextView descriptionView;
    @Bind(R.id.bottomImageView)
    FadeTransitionImageView bottomImageView;
    @Bind(R.id.timeView)
    HorizontalTransitionLayout timeView;
    private Animator.AnimatorListener animatorListener;
    private ArrayList<ItemEntity> dataList;
    private ObjectAnimator transitionAnimator;

    @Override
    public int getContentView() {
        return R.layout.activity_my_pic_pile;
    }

    @Override
    public void initView() {
        initToolBar();
        initAnimation();
        initDataList();
        initPile();
    }


    private void initToolBar() {
        llSearch.setVisibility(View.VISIBLE);
        toolbarTitle.setText("图片欣赏");
    }


    @Override
    public void initListener() {
        llSearch.setOnClickListener(this);
        llTitleMenu.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_search:

                break;
            case R.id.ll_title_menu:
                finish();
                break;
        }
    }


    private void initAnimation() {
        animatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                countryView.onAnimationEnd();
                temperatureView.onAnimationEnd();
                addressView.onAnimationEnd();
                bottomImageView.onAnimationEnd();
                timeView.onAnimationEnd();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };
    }

    private void initDataList() {
        dataList = new ArrayList<>();
        try {
            InputStream in = getAssets().open("preset.config");
            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);
            String jsonStr = new String(buffer, "UTF-8");
            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONArray jsonArray = jsonObject.optJSONArray("result");
            if (null != jsonArray) {
                int len = jsonArray.length();
                for (int j = 0; j < 3; j++) {
                    for (int i = 0; i < len; i++) {
                        JSONObject itemJsonObject = jsonArray.getJSONObject(i);
                        ItemEntity itemEntity = new ItemEntity(itemJsonObject);
                        dataList.add(itemEntity);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int lastDisplay = -1;
    private void initPile() {
        pileLayout.setAdapter(new CardViewLayout.Adapter() {
            class ViewHolder {
                ImageView imageView;
            }

            @Override
            public int getLayoutId() {
                return R.layout.item_card_layout;
            }

            @Override
            public void bindView(View view, int position) {
                ViewHolder viewHolder = (ViewHolder) view.getTag();
                if (viewHolder == null) {
                    viewHolder = new ViewHolder();
                    viewHolder.imageView = (ImageView) view.findViewById(R.id.imageView);
                    view.setTag(viewHolder);
                }

                Glide.with(MyPicPileActivity.this)
                        .load(dataList.get(position).getCoverImageUrl())
                        .into(viewHolder.imageView);
            }

            @Override
            public int getItemCount() {
                return dataList.size();
            }

            @Override
            public void displaying(int position) {
                descriptionView.setText(dataList.get(position).getDescription() + " Since the world is so beautiful, You have to believe me, and this index is " + position);
                if (lastDisplay < 0) {
                    initSecene(position);
                    lastDisplay = 0;
                } else if (lastDisplay != position) {
                    transitionSecene(position);
                    lastDisplay = position;
                }
            }

            @Override
            public void onItemClick(View view, int position) {
                super.onItemClick(view, position);
                ToastUtils.showShortSafe("点击了"+position+"图片");
            }
        });
    }

    private void initSecene(int position) {
        countryView.firstInit(dataList.get(position).getCountry());
        temperatureView.firstInit(dataList.get(position).getTemperature());
        addressView.firstInit(dataList.get(position).getAddress());
        bottomImageView.firstInit(dataList.get(position).getMapImageUrl());
        timeView.firstInit(dataList.get(position).getTime());
    }

    private void transitionSecene(int position) {
        if (transitionAnimator != null) {
            transitionAnimator.cancel();
        }
        countryView.saveNextPosition(position, dataList.get(position).getCountry() + "-" + position);
        temperatureView.saveNextPosition(position, dataList.get(position).getTemperature());
        addressView.saveNextPosition(position, dataList.get(position).getAddress());
        bottomImageView.saveNextPosition(position, dataList.get(position).getMapImageUrl());
        timeView.saveNextPosition(position, dataList.get(position).getTime());
        transitionAnimator = ObjectAnimator.ofFloat(this, "transitionValue", 0.0f, 1.0f);
        transitionAnimator.setDuration(300);
        transitionAnimator.start();
        transitionAnimator.addListener(animatorListener);
    }

}
