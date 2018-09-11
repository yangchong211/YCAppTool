package com.ns.yc.lifehelper.ui.other.myPicture.view.activity;

import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.constantApi.ConstantImageApi;
import com.ns.yc.lifehelper.base.app.BaseApplication;
import com.ns.yc.lifehelper.base.mvp.BaseActivity;
import com.ns.yc.lifehelper.utils.bitmap.BitmapSaveUtils;
import com.ns.yc.lifehelper.utils.bitmap.BitmapUtils;
import com.ns.yc.lifehelper.weight.gridview.ImageGridView;
import com.ns.yc.lifehelper.weight.imageView.CircleImageView;
import com.pedaily.yc.ycdialoglib.customToast.ToastUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import butterknife.Bind;
import cn.ycbjie.ycthreadpoollib.PoolThread;
import cn.ycbjie.ycthreadpoollib.callback.AsyncCallback;


public class ImageGvActivity extends BaseActivity implements View.OnClickListener {


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
    @Bind(R.id.iv_author_img)
    CircleImageView ivAuthorImg;
    @Bind(R.id.tv_author)
    TextView tvAuthor;
    @Bind(R.id.tv_date)
    TextView tvDate;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.gv_img)
    ImageGridView gvImg;
    @Bind(R.id.iv_img)
    ImageView ivImg;
    @Bind(R.id.cv_recommend)
    CardView cvRecommend;
    @Bind(R.id.tv_save)
    TextView tvSave;
    @Bind(R.id.tv_share)
    TextView tvShare;

    private ArrayList<String> list;

    @Override
    public int getContentView() {
        return R.layout.activity_image_nine_gv;
    }

    @Override
    public void initView() {
        setToolBar(toolbar,"保存图片");
    }

    @Override
    public void initListener() {
        tvSave.setOnClickListener(this);
    }

    @Override
    public void initData() {
        list = initGvImageData();
        if(list!=null && list.size()>0){
            gvImg.setUri(list, new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });
        }
    }

    private void setToolBar(Toolbar toolbar, String title) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private ArrayList<String> initGvImageData() {
        ArrayList<String> list = new ArrayList<>();
        List<String> strings = Arrays.asList(ConstantImageApi.SPALSH_URLS);
        list.addAll(strings);
        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_save:
                ToastUtil.showToast(this,"开始保存");
                saveBitmapImage(list);
                break;
            default:
                break;
        }
    }


    private void saveBitmapImage(final ArrayList<String> list) {
        PoolThread executor = BaseApplication.getInstance().getExecutor();
        // 启动异步任务
        executor.async(new Callable<List<Bitmap>>(){
            @Override
            public List<Bitmap> call() throws Exception {
                List<Bitmap> bitmaps = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    String str = list.get(i);
                    Bitmap bitmap = BitmapUtils.returnBitMap(str);
                    bitmaps.add(bitmap);
                }
                return bitmaps;
            }
        }, new AsyncCallback<List<Bitmap>>() {
            @Override
            public void onSuccess(List<Bitmap> bitmaps) {
                Log.e("AsyncCallback","成功");
                for (Bitmap bitmap : bitmaps){
                    BitmapSaveUtils.saveBitmap(ImageGvActivity.this, bitmap, null, true);
                }
                ToastUtil.showToast(ImageGvActivity.this,"保存成功");
            }

            @Override
            public void onFailed(Throwable t) {
                Log.e("AsyncCallback","失败");
                ToastUtil.showToast(ImageGvActivity.this,"保存失败");
            }

            @Override
            public void onStart(String threadName) {
                Log.e("AsyncCallback","开始");
            }
        });



    }


}
