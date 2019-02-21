package com.ycbjie.music.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.library.utils.image.ImageUtils;
import com.ycbjie.music.R;
import com.ycbjie.music.api.OnLineMusicModel;
import com.ycbjie.music.model.bean.ArtistInfo;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class ArtistInfoActivity extends BaseActivity implements View.OnClickListener {


    private Toolbar toolbar;
    private TextView tvTitleLeft;
    private FrameLayout llTitleMenu;
    private TextView toolbarTitle;
    private FrameLayout llSearch;
    private ImageView ivRightImg;
    private TextView tvTitleRight;
    private ScrollView svArtistInfo;
    private LinearLayout llArtistInfo;
    private String tingUid;

    @Override
    public int getContentView() {
        return R.layout.activity_artist_info;
    }

    @Override
    public void initView() {
        initIntentData();


        toolbar = findViewById(R.id.toolbar);
        tvTitleLeft = findViewById(R.id.tv_title_left);
        llTitleMenu = findViewById(R.id.ll_title_menu);
        toolbarTitle = findViewById(R.id.toolbar_title);
        llSearch = findViewById(R.id.ll_search);
        ivRightImg = findViewById(R.id.iv_right_img);
        tvTitleRight = findViewById(R.id.tv_title_right);


        svArtistInfo = findViewById(R.id.sv_artist_info);
        llArtistInfo = findViewById(R.id.ll_artist_info);

    }

    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
    }

    @Override
    public void initData() {
        getArtistInfo(tingUid);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ll_title_menu) {
            finish();
        } else {

        }
    }


    private void initIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            tingUid = getIntent().getStringExtra("artist_id");
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressLint("CheckResult")
    private void getArtistInfo(String tingUid) {
        OnLineMusicModel model = OnLineMusicModel.getInstance();
        model.getArtistInfo(OnLineMusicModel.METHOD_ARTIST_INFO, tingUid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ArtistInfo>() {
                    @Override
                    public void accept(ArtistInfo artistInfo) throws Exception {
                        if (artistInfo != null) {
                            setData(artistInfo);
                        }
                    }
                });
    }

    private void setData(ArtistInfo artistInfo) {
        String name = artistInfo.getName();
        toolbarTitle.setText(name);
        String avatarUri = artistInfo.getAvatar_s1000();
        String country = artistInfo.getCountry();
        String constellation = artistInfo.getConstellation();
        float stature = artistInfo.getStature();
        float weight = artistInfo.getWeight();
        String birth = artistInfo.getBirth();
        String intro = artistInfo.getIntro();
        String url = artistInfo.getUrl();
        if (!TextUtils.isEmpty(avatarUri)) {
            ImageView ivAvatar = new ImageView(this);
            ivAvatar.setScaleType(ImageView.ScaleType.FIT_START);
            ImageUtils.loadImgByPicasso(this, avatarUri, R.drawable.image_default, ivAvatar);
            llArtistInfo.addView(ivAvatar);
        }
        if (!TextUtils.isEmpty(name)) {
            setTitle(name);
            TextView tvName = (TextView) LayoutInflater.from(this).inflate(R.layout.item_artist_info, llArtistInfo, false);
            tvName.setText(getString(R.string.artist_info_name, name));
            llArtistInfo.addView(tvName);
        }
        if (!TextUtils.isEmpty(country)) {
            TextView tvCountry = (TextView) LayoutInflater.from(this).inflate(R.layout.item_artist_info, llArtistInfo, false);
            tvCountry.setText(getString(R.string.artist_info_country, country));
            llArtistInfo.addView(tvCountry);
        }
        if (!TextUtils.isEmpty(constellation) && !TextUtils.equals(constellation, "未知")) {
            TextView tvConstellation = (TextView) LayoutInflater.from(this).inflate(R.layout.item_artist_info, llArtistInfo, false);
            tvConstellation.setText(getString(R.string.artist_info_constellation, constellation));
            llArtistInfo.addView(tvConstellation);
        }
        if (stature != 0f) {
            TextView tvStature = (TextView) LayoutInflater.from(this).inflate(R.layout.item_artist_info, llArtistInfo, false);
            tvStature.setText(getString(R.string.artist_info_stature, String.valueOf(stature)));
            llArtistInfo.addView(tvStature);
        }
        if (weight != 0f) {
            TextView tvWeight = (TextView) LayoutInflater.from(this).inflate(R.layout.item_artist_info, llArtistInfo, false);
            tvWeight.setText(getString(R.string.artist_info_weight, String.valueOf(weight)));
            llArtistInfo.addView(tvWeight);
        }
        if (!TextUtils.isEmpty(birth) && !TextUtils.equals(birth, "0000-00-00")) {
            TextView tvBirth = (TextView) LayoutInflater.from(this).inflate(R.layout.item_artist_info, llArtistInfo, false);
            tvBirth.setText(getString(R.string.artist_info_birth, birth));
            llArtistInfo.addView(tvBirth);
        }
        if (!TextUtils.isEmpty(intro)) {
            TextView tvIntro = (TextView) LayoutInflater.from(this).inflate(R.layout.item_artist_info, llArtistInfo, false);
            tvIntro.setText(getString(R.string.artist_info_intro, intro));
            llArtistInfo.addView(tvIntro);
        }
        if (!TextUtils.isEmpty(url)) {
            TextView tvUrl = (TextView) LayoutInflater.from(this).inflate(R.layout.item_artist_info, llArtistInfo, false);
            tvUrl.setLinkTextColor(ContextCompat.getColor(this, R.color.redTab));
            tvUrl.setMovementMethod(LinkMovementMethod.getInstance());
            SpannableString spannableString = new SpannableString("查看更多信息");
            spannableString.setSpan(new URLSpan(url), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvUrl.setText(spannableString);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
            tvUrl.setLayoutParams(layoutParams);
            llArtistInfo.addView(tvUrl);
        }

        /*if (llArtistInfo.getChildCount() == 0) {
            ViewUtils.changeViewState(svArtistInfo, llLoading, llLoadFail, LoadStateEnum.LOAD_FAIL);
            ((TextView) llLoadFail.findViewById(R.id.tv_load_fail_text)).setText(R.string.artist_info_empty);
        }*/
    }


}
