package com.ycbjie.note.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cheoo.photo.Matisse;
import com.cheoo.photo.MimeType;
import com.cheoo.photo.MyGlideEngine;
import com.google.gson.Gson;
import com.ns.yc.yccustomtextlib.edit.inter.ImageLoader;
import com.ns.yc.yccustomtextlib.edit.inter.OnHyperEditListener;
import com.ns.yc.yccustomtextlib.edit.manager.HyperManager;
import com.ns.yc.yccustomtextlib.edit.model.HyperEditData;
import com.ns.yc.yccustomtextlib.edit.view.HyperImageView;
import com.ns.yc.yccustomtextlib.edit.view.HyperTextEditor;
import com.ns.yc.yccustomtextlib.utils.HyperLibUtils;
import com.ns.yc.yccustomtextlib.utils.HyperLogUtils;
import com.pedaily.yc.ycdialoglib.fragment.CustomDialogFragment;
import com.pedaily.yc.ycdialoglib.toast.ToastUtils;
import com.yc.configlayer.arounter.RouterConfig;
import com.yc.imageserver.transformations.TransformationScale;
import com.ycbjie.note.R;
import com.ycbjie.note.utils.ModelStorage;

import java.util.ArrayList;
import java.util.List;

import cn.ycbjie.ycstatusbarlib.bar.StateAppBar;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


@Route(path = RouterConfig.Note.ACTIVITY_OTHER_ARTICLE)
public class NewArticleActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CHOOSE = 520;
    private TextView mTvTitle;
    private TextView mTvBack;
    private TextView mTvSaveArticle;
    private TextView mTvNext;
    private HyperTextEditor mHteContent;
    private TextView mTvInsertImage;
    private TextView mTvLength;
    private TextView mTvSoft;
    private Disposable subsInsert;
    /**
     * 是否已经成功保存草稿
     * 注意：如果内容发生了变化，则需要设置成false，调用接口成功后，才设置成true
     */
    private boolean isSave = false;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //判断键盘是否弹出
        boolean softInputVisible = HyperLibUtils.isSoftInputVisible(this);
        if (softInputVisible){
            HyperLibUtils.hideSoftInput(this);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        StateAppBar.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary));
        initFindViewById();
        initListener();
        initHyper();
        //解决点击EditText弹出收起键盘时出现的黑屏闪现现象
        View rootView = mHteContent.getRootView();
        rootView.setBackgroundColor(Color.WHITE);
        mHteContent.postDelayed(new Runnable() {
            @Override
            public void run() {
                EditText lastFocusEdit = mHteContent.getLastFocusEdit();
                lastFocusEdit.requestFocus();
                //打开软键盘显示
                //HyperLibUtils.openSoftInput(NewArticleActivity.this);
            }
        },300);
    }

    private void initFindViewById() {
        mTvTitle = findViewById(R.id.tv_title);
        mTvBack = findViewById(R.id.tv_back);
        mTvSaveArticle = findViewById(R.id.tv_save_article);
        mTvNext = findViewById(R.id.tv_next);
        mHteContent = findViewById(R.id.hte_content);
        mTvInsertImage = findViewById(R.id.tv_insert_image);
        mTvLength = findViewById(R.id.tv_length);
        mTvSoft = findViewById(R.id.tv_soft);

    }

    private void initListener() {
        mTvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mHteContent.setOnHyperListener(new OnHyperEditListener() {
            @Override
            public void onImageClick(View view, String imagePath) {
                //图片点击事件
                ToastUtils.showRoundRectToast("图片点击"+imagePath);
            }

            @Override
            public void onRtImageDelete(String imagePath) {
                //图片删除成功事件
                ToastUtils.showRoundRectToast("图片删除成功");
            }

            @Override
            public void onImageCloseClick(final View view) {
                //图片删除图片点击事件
                CustomDialogFragment
                        .create((NewArticleActivity.this).getSupportFragmentManager())
                        .setTitle("删除图片")
                        .setContent("确定要删除该图片吗?")
                        .setCancelContent("取消")
                        .setOkContent("确定")
                        .setDimAmount(0.5f)
                        .setOkColor(NewArticleActivity.this.getResources().getColor(R.color.color_000000))
                        .setCancelOutside(true)
                        .setCancelListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CustomDialogFragment.dismissDialogFragment();
                            }
                        })
                        .setOkListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CustomDialogFragment.dismissDialogFragment();
                                mHteContent.onImageCloseClick(view);
                            }
                        })
                        .show();
            }
        });
        mHteContent.setOnHyperChangeListener((contentLength, imageLength) -> {
            //富文本的文字数量，图片数量统计
            mTvLength.setText("文字共"+contentLength+"个字，图片共"+imageLength+"张");
            isSave = false;
        });
        mTvSaveArticle.setOnClickListener(v -> {
            if (startCheckContent()){
                if (HyperLibUtils.isSoftInputVisible(NewArticleActivity.this)){
                    //关闭键盘
                    HyperLibUtils.hideSoftInput(NewArticleActivity.this);
                }
                if (isSave){
                    ToastUtils.showRoundRectToast("您已经保存文章内容呢");
                } else {
                    saveNewsContent();
                }
            }
        });
        mTvInsertImage.setOnClickListener(v -> {
            //插入图片
            if (HyperLibUtils.isSoftInputVisible(NewArticleActivity.this)){
                //关闭键盘
                HyperLibUtils.hideSoftInput(NewArticleActivity.this);
            }
            callGallery();
        });
        mTvNext.setOnClickListener(v -> {
            if (startCheckContent()){
                //开始下一步
                List<HyperEditData> hyperEditData = mHteContent.buildEditData();
                ModelStorage.getInstance().setHyperEditData(hyperEditData);
                startActivity(new Intent(NewArticleActivity.this,PreviewArticleActivity.class));
            }
        });
        mTvSoft.setOnClickListener(v -> {
            if (HyperLibUtils.isSoftInputVisible(NewArticleActivity.this)){
                //关闭键盘
                HyperLibUtils.hideSoftInput(NewArticleActivity.this);
            } else {
                //打开键盘
                HyperLibUtils.openSoftInput(NewArticleActivity.this);
            }
        });
    }


    private boolean startCheckContent() {
        if (mHteContent.getContentLength()==0){
            ToastUtils.showRoundRectToast("内容不能为空");
            return false;
        }
        if (mHteContent.getContentLength()<25 || mHteContent.getContentLength()>10000){
            ToastUtils.showRoundRectToast("正文应为25-10000字");
            return false;
        }
        return true;
    }


    private void saveNewsContent() {
        if (startCheckContent()){
            try {
                List<HyperEditData> hyperEditData = mHteContent.buildEditData();
                Gson gson = new Gson();
                String content = gson.toJson(hyperEditData);
                LogUtils.json("content---"+content);
            } catch (Exception e){
                e.printStackTrace();
            } finally {
                //todo 提交接口
            }
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        try {
            if (subsInsert != null && subsInsert.isDisposed()){
                subsInsert.dispose();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.insert_image) {//插入图片
            HyperLibUtils.hideSoftInput(this);
            callGallery();
        } else if (itemId == R.id.save) {//保存
            List<HyperEditData> hyperEditData = mHteContent.buildEditData();
            ModelStorage.getInstance().setHyperEditData(hyperEditData);
            startActivity(new Intent(this, PreviewArticleActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                if (requestCode == REQUEST_CODE_CHOOSE){
                    //异步方式插入图片
                    List<Uri> uriList = Matisse.obtainResult(data);
                    List<String> imageList = new ArrayList<>();
                    for (Uri imageUri : uriList) {
                        String imagePath = HyperLibUtils.getFilePathFromUri(NewArticleActivity.this,  imageUri);
                        imageList.add(imagePath);
                    }
                    //异步插入单张或者多张图片到富文本，速度快。体验效果好一些
                    insertImagesSync(imageList);
                    //异步提交单张或者多张图片到网络服务器，然后返回链接。
                    //为啥不等返回链接在设置图片到富文本，提交多张图片上传服务器比较慢
                    //insertImagesSyncNet(imageList);
                }
            }
        }
    }


    /**
     * 异步方式插入图片，需要测试一下多张图片按顺序——压缩——上传——插入图片耗时
     */
    private void insertImagesSync(final List<String> imageList){
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                try{
                    // 可以同时插入多张图片
                    for (int i=0 ; i<imageList.size() ; i++){
                        String imagePath = imageList.get(i);
                        emitter.onNext(imagePath);
                    }
                    // 测试插入网络图片
                    //emitter.onNext("https://img-blog.csdnimg.cn/2019122518174761.jpeg");
                    //emitter.onNext("https://img-blog.csdnimg.cn/20191225181807586.jpeg");
                    emitter.onComplete();
                }catch (Exception e){
                    e.printStackTrace();
                    emitter.onError(e);
                }
            }
        })
                //.onBackpressureBuffer()
                //生产事件在io
                .subscribeOn(Schedulers.io())
                //消费事件在UI线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onComplete() {
                        //BaseToast.showRoundRectToast("图片插入成功onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        //BaseToast.showRoundRectToast("图片插入失败:"+e.getMessage());
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        subsInsert = d;
                    }

                    @Override
                    public void onNext(String imagePath) {
                        if (imagePath!=null){
                            HyperLogUtils.d("图片插入成功-------"+imagePath+"-----");
                            //这个主要是为了保证插入图片的顺序
                            mHteContent.insertImage(imagePath);
                        }
                    }
                });
    }




    /**
     * 调用图库选择
     */
    private void callGallery(){
        int screenWidth = ScreenUtils.getScreenWidth();
        //参考案例：
        Matisse.from(NewArticleActivity.this)
                .choose(MimeType.ofImage())//照片视频全部显示MimeType.allOf()
                .capture(false)//不启用拍照功能
                .theme(R.style.AppTheme)//设置theme主题
                .countable(true)//true:选中后显示数字;false:选中后显示对号
                .maxSelectable(9)//最大选择数量为9
                //.addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(screenWidth/4)//图片显示表格的大小
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)//图像选择和预览活动所需的方向
                .setOnSelectedListener((uriList, pathList) -> {
                    LogUtils.e("onSelected", "onSelected: pathList=" + pathList);
                })
                .setOnCheckedListener(isChecked -> {
                    LogUtils.e("isChecked", "onCheck: isChecked=" + isChecked);
                })
                .thumbnailScale(0.85f)//缩放比例
                .maxOriginalSize(2)//限制设置最大图片为2MB，大于则提示图片大于2MB无法上传
                .originalEnable(false)//让用户选择后决定是否使用原图
                .theme(R.style.Matisse_Zhihu)//主题  暗色主题 R.style.Matisse_Dracula
                .imageEngine(new MyGlideEngine())//图片加载方式，Glide4需要自定义实现
                //.capture(true) //是否提供拍照功能，兼容7.0系统需要下面的配置
                //这个可以不用打开，直接关闭拍照的功能
                //参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                //.captureStrategy(new CaptureStrategy(true,"com.cheoo.photo"))//存储到哪里，这个具体看FileProvider配置
                .forResult(REQUEST_CODE_CHOOSE);//请求码
    }

    private void initHyper(){
        HyperManager.getInstance().setImageLoader(new ImageLoader() {
            @Override
            public void loadImage(final String imagePath, final HyperImageView imageView, final int imageHeight) {
                Log.e("---", "imageHeight: "+imageHeight);
                //如果是网络图片
                if (imagePath.startsWith("http://") || imagePath.startsWith("https://")){
                    Glide.with(getApplicationContext()).asBitmap()
                            .load(imagePath)
                            .dontAnimate()
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    if (imageHeight > 0) {//固定高度
                                        if (imageView.getLayoutParams() instanceof RelativeLayout.LayoutParams){
                                            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                                                    FrameLayout.LayoutParams.MATCH_PARENT, imageHeight);//固定图片高度，记得设置裁剪剧中
                                            lp.bottomMargin = 10;//图片的底边距
                                            imageView.setLayoutParams(lp);
                                        } else if (imageView.getLayoutParams() instanceof FrameLayout.LayoutParams){
                                            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                                                    FrameLayout.LayoutParams.MATCH_PARENT, imageHeight);//固定图片高度，记得设置裁剪剧中
                                            lp.bottomMargin = 10;//图片的底边距
                                            imageView.setLayoutParams(lp);
                                        }
                                        Glide.with(getApplicationContext()).asBitmap().load(imagePath).centerCrop()
                                                .placeholder(R.drawable.img_load_fail).error(R.drawable.img_load_fail).into(imageView);
                                    } else {//自适应高度
                                        Glide.with(getApplicationContext()).asBitmap().load(imagePath)
                                                .placeholder(R.drawable.img_load_fail).error(R.drawable.img_load_fail).into(new TransformationScale(imageView));
                                    }
                                }
                            });
                } else { //如果是本地图片
                    if (imageHeight>0){
                        Glide.with(getApplicationContext())
                                .asBitmap()
                                .load(imagePath)
                                .centerCrop()
                                .placeholder(R.drawable.img_load_fail)
                                .error(R.drawable.img_load_fail)
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                        int width = resource.getWidth();
                                        int height = resource.getHeight();
                                        HyperLogUtils.d("本地图片--3--"+height+"----"+width);
                                        imageView.setImageBitmap(resource);
                                        ViewParent parent = imageView.getParent();
                                        int imageHeight = 0;
                                        //单个图片最高200，最小高度100，图片按高度宽度比例，通过改变夫布局来控制动态高度
                                        if (height> HyperLibUtils.dip2px(NewArticleActivity.this,200)){
                                            if (parent instanceof RelativeLayout){
                                                ViewGroup.LayoutParams layoutParams = ((RelativeLayout) parent).getLayoutParams();
                                                layoutParams.height = HyperLibUtils.dip2px(NewArticleActivity.this,200);
                                                ((RelativeLayout) parent).setLayoutParams(layoutParams);
                                            } else if (parent instanceof FrameLayout){
                                                ViewGroup.LayoutParams layoutParams = ((FrameLayout) parent).getLayoutParams();
                                                layoutParams.height = HyperLibUtils.dip2px(NewArticleActivity.this,200);
                                                ((FrameLayout) parent).setLayoutParams(layoutParams);
                                                HyperLogUtils.d("本地图片--4--");
                                            }
                                            imageHeight = HyperLibUtils.dip2px(NewArticleActivity.this,200);
                                        } else if (height> HyperLibUtils.dip2px(NewArticleActivity.this,100)
                                                && height< HyperLibUtils.dip2px(NewArticleActivity.this,200)){
                                            //自是因高度
                                            HyperLogUtils.d("本地图片--5--");
                                            imageHeight = height;
                                        } else {
                                            if (parent instanceof RelativeLayout){
                                                ViewGroup.LayoutParams layoutParams = ((RelativeLayout) parent).getLayoutParams();
                                                layoutParams.height = HyperLibUtils.dip2px(NewArticleActivity.this,100);
                                                ((RelativeLayout) parent).setLayoutParams(layoutParams);
                                            } else if (parent instanceof FrameLayout){
                                                ViewGroup.LayoutParams layoutParams = ((FrameLayout) parent).getLayoutParams();
                                                layoutParams.height = HyperLibUtils.dip2px(NewArticleActivity.this,100);
                                                ((FrameLayout) parent).setLayoutParams(layoutParams);
                                                HyperLogUtils.d("本地图片--6--");
                                            }
                                            imageHeight = HyperLibUtils.dip2px(NewArticleActivity.this,100);
                                        }
                                        //设置图片的属性，图片的底边距，以及图片的动态高度
                                        if (imageView.getLayoutParams() instanceof RelativeLayout.LayoutParams){
                                            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                                                    FrameLayout.LayoutParams.MATCH_PARENT, imageHeight);//固定图片高度，记得设置裁剪剧中
                                            lp.bottomMargin = 10;//图片的底边距
                                            imageView.setLayoutParams(lp);
                                        } else if (imageView.getLayoutParams() instanceof FrameLayout.LayoutParams){
                                            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                                                    FrameLayout.LayoutParams.MATCH_PARENT, imageHeight);//固定图片高度，记得设置裁剪剧中
                                            lp.bottomMargin = 10;//图片的底边距
                                            imageView.setLayoutParams(lp);
                                        }
                                    }
                                });
                    } else {
                        Glide.with(getApplicationContext())
                                .asBitmap()
                                .load(imagePath)
                                .placeholder(R.drawable.img_load_fail)
                                .error(R.drawable.img_load_fail)
                                .into(new TransformationScale(imageView));
                    }
                }
            }
        });
    }

    /**
     * 点击空白区域隐藏键盘.
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {
        if (me.getAction() == MotionEvent.ACTION_DOWN) {
            //把操作放在用户点击的时候
            View v = getCurrentFocus();
            //得到当前页面的焦点,ps:有输入框的页面焦点一般会被输入框占据
            if (isShouldHideKeyboard(v, me)) {
                //判断用户点击的是否是输入框以外的区域
                hideKeyboard(v.getWindowToken());
                //收起键盘
            }
        }
        return super.dispatchTouchEvent(me);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v     view
     * @param event event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            //判断得到的焦点控件是否包含EditText
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    //得到输入框在屏幕中上下左右的位置
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击位置如果是EditText的区域，忽略它，不收起键盘。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


}
