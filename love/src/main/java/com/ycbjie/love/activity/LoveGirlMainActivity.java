package com.ycbjie.love.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ycbjie.library.arounter.ARouterConstant;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.love.R;
import com.ycbjie.love.util.ClickUtils;
import com.ycbjie.love.util.DeviceInfo;
import com.ycbjie.love.util.ImageUtils;
import com.ycbjie.love.view.bluesnow.FlowerView;
import com.ycbjie.love.view.heart.HeartLayout;
import com.ycbjie.love.view.typewriter.TypeTextView;
import com.ycbjie.love.view.whitesnow.SnowView;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import cn.ycbjie.ycstatusbarlib.bar.StateAppBar;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/05/23
 *     desc  : love
 *     revise:
 * </pre>
 */
@Route(path = ARouterConstant.ACTIVITY_LOVE_ACTIVITY)
public class LoveGirlMainActivity extends BaseActivity {

    private static final String JPG = ".jpg";
    private static final String LOVE = "心动是等你的留言，渴望是常和你见面，甜蜜是和你小路流连，温馨是看着你清澈的双眼，爱你的感觉真的妙不可言！";
    private static final int SNOW_BLOCK = 1;
    public static final String URL = "file:///android_asset/index.html";
    private Canvas mCanvas;
    private int mCounter;
    private FlowerView mBlueSnowView;//蓝色的雪花
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            mBlueSnowView.inva();
        }
    };
    private HeartLayout mHeartLayout;//垂直方向的漂浮的红心
    private ImageView mImageView;//图片
    private Bitmap mManyBitmapSuperposition;
    private ProgressBar mProgressBar;
    private Random mRandom = new Random();
    private Random mRandom2 = new Random();
    private TimerTask mTask = null;
    private TypeTextView mTypeTextView;//打字机
    private WebView mWebView;
    private SnowView mWhiteSnowView;//白色的雪花
    private Timer myTimer = null;
    private FrameLayout mWebViwFrameLayout = null;
    private TextView textview = null;
    private Subscription subscribe;

    @Override
    public int getContentView() {
        return R.layout.activity_love_girl;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        WebSettings mWebSettings = mWebView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setBuiltInZoomControls(false);
        mWebSettings.setLightTouchEnabled(false);
        mWebSettings.setSupportZoom(false);
        mWebView.setHapticFeedbackEnabled(false);
    }

    public void initView() {
        DeviceInfo.getInstance().initializeScreenInfo(this);
        StateAppBar.setStatusBarLightMode(this, Color.WHITE);
        initFindViewById();
        initWebView();
    }

    private void initFindViewById() {
        mWebViwFrameLayout = findViewById(R.id.fl_webView_layout);
        textview = findViewById(R.id.textView);
        mWebView = new WebView(getApplicationContext());
        mWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        mWebView.setVisibility(View.GONE);
        //scrollbars
        FrameLayout.LayoutParams fp = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        fp.gravity = Gravity.CENTER;

        mWebViwFrameLayout.addView(mWebView);

        mHeartLayout = (HeartLayout) findViewById(R.id.heart_o_red_layout);
        mTypeTextView = (TypeTextView) findViewById(R.id.typeTextView);
        mWhiteSnowView = (SnowView) findViewById(R.id.whiteSnowView);
        mImageView = (ImageView) findViewById(R.id.image);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mBlueSnowView = (FlowerView) findViewById(R.id.flowerview);
        mBlueSnowView.setWH(DeviceInfo.mScreenWidthForPortrait, DeviceInfo.mScreenHeightForPortrait, DeviceInfo.mDensity);
        mBlueSnowView.loadFlower();
        mBlueSnowView.addRect();
        this.mTypeTextView.setText("");
    }

    @Override
    public void initListener() {
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ClickUtils.isFastDoubleClick()) {
                    delayShowAll(0L);
                }
            }
        });
        mTypeTextView.setOnTypeViewListener(new TypeTextView.OnTypeViewListener() {
            public void onTypeStart() {
            }

            public void onTypeOver() {
                delayShowTheSnow();
            }
        });
    }

    @Override
    public void initData() {
        myTimer = new Timer();
        mTask = new TimerTask() {
            public void run() {
                Message msg = new Message();
                msg.what = LoveGirlMainActivity.SNOW_BLOCK;
                mHandler.sendMessage(msg);
            }
        };
        rxJavaSolveMiZhiSuoJinAndNestedLoopAndCallbackHell();
        myTimer.schedule(this.mTask, 3000, 10);
        delayShowAll(3000L);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTimer();
        if (this.mWebView != null) {
            if (mWebViwFrameLayout != null) {
                this.mWebViwFrameLayout.removeAllViewsInLayout();
                this.mWebViwFrameLayout.removeAllViews();
            }
            this.mWebView.removeAllViews();
            this.mWebView.destroy();
            this.mWebView = null;
        }
        subscribe.unsubscribe();
        unBindDrawables(findViewById(R.id.root_fragment_layout));
        System.gc();
    }


    private void cancelTimer() {
        if (this.myTimer != null) {
            this.myTimer.cancel();
            this.myTimer = null;
        }
        if (this.mTask != null) {
            this.mTask.cancel();
            this.mTask = null;
        }
    }

    private void createSingleImageFromMultipleImages(Bitmap bitmap, int mCounter) {
        if (mCounter == 0) {
            try {
                this.mManyBitmapSuperposition = Bitmap.createBitmap(DeviceInfo.mScreenWidthForPortrait, DeviceInfo.mScreenHeightForPortrait, bitmap.getConfig());
                this.mCanvas = new Canvas(this.mManyBitmapSuperposition);
            } catch (OutOfMemoryError error) {
                error.printStackTrace();
                System.gc();
            } finally {

            }
        }
        if (this.mCanvas != null) {
            int number = DeviceInfo.mScreenHeightForPortrait / 64;
            if (mCounter >= (mCounter / number) * number && mCounter < ((mCounter / number) + SNOW_BLOCK) * number) {
                this.mCanvas.drawBitmap(bitmap, (float) ((mCounter / number) * 64), (float) ((mCounter % number) * 64), null);
            }
        }
    }

    private void rxJavaSolveMiZhiSuoJinAndNestedLoopAndCallbackHell() {
        subscribe = Observable.from(getAssetImageFolderName())
                .flatMap(new Func1<String, Observable<String>>() {
                    public Observable<String> call(String folderName) {
                        return Observable.from(ImageUtils.getAssetsImageNamePathList(getApplicationContext(), folderName));
                    }
                }).filter(new Func1<String, Boolean>() {
                    public Boolean call(String imagePathNameAll) {
                        return Boolean.valueOf(imagePathNameAll.endsWith(LoveGirlMainActivity.JPG));
                    }
                }).map(new Func1<String, Bitmap>() {
                    public Bitmap call(String imagePathName) {
                        return ImageUtils.getImageBitmapFromAssetsFolderThroughImagePathName(getApplicationContext(), imagePathName, DeviceInfo.mScreenWidthForPortrait, DeviceInfo.mScreenHeightForPortrait);
                    }
                }).map(new Func1<Bitmap, Void>() {
                    public Void call(Bitmap bitmap) {
                        createSingleImageFromMultipleImages(bitmap, mCounter);
                        mCounter = mCounter++;
                        return null;
                    }
                }).subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    public void call() {
                        mProgressBar.setVisibility(View.VISIBLE);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Void>() {
                    public void onCompleted() {
                        mImageView.setImageBitmap(mManyBitmapSuperposition);
                        mProgressBar.setVisibility(View.GONE);
                        showAllViews();
                    }

                    public void onError(Throwable e) {
                    }

                    public void onNext(Void aVoid) {
                    }
                });
    }

    private void showAllViews() {
        this.mImageView.setVisibility(View.VISIBLE);
        this.mWhiteSnowView.setVisibility(View.VISIBLE);
    }


    private void gotoNext() {
        mWebView.loadUrl(URL);
        delayDo();
    }

    private void delayShow(long time) {
        Observable.timer(time, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        mTypeTextView.start(LoveGirlMainActivity.LOVE);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Long aLong) {
                    }
                });
    }


    private void delayShowTheSnow() {
        Observable.timer(200, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    public void onCompleted() {
                        mBlueSnowView.setVisibility(View.VISIBLE);
                        showRedHeartLayout();
                    }

                    public void onError(Throwable e) {
                    }

                    public void onNext(Long aLong) {
                    }
                });

    }

    private void delayDo() {
        Observable.timer(0, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        mWhiteSnowView.setVisibility(View.GONE);
                        mWebView.setVisibility(View.VISIBLE);
                        textview.setVisibility(View.VISIBLE);
                        delayShow(5100);//延时显示显示打印机
                        mWebView.loadUrl(LoveGirlMainActivity.URL);
                        textview.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(
                                        LoveGirlMainActivity.this, LoveBrowserActivity.class));
                            }
                        });

                    }

                    public void onError(Throwable e) {
                    }

                    public void onNext(Long aLong) {
                    }
                });
    }

    private void delayShowAll(long time) {
        Observable.timer(time, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    public void onCompleted() {
                        gotoNext();
                    }

                    public void onError(Throwable e) {
                    }

                    public void onNext(Long aLong) {
                    }
                });
    }


    private int randomColor() {
        return Color.rgb(mRandom.nextInt(255),
                mRandom.nextInt(255), mRandom.nextInt(255));
    }

    private void showRedHeartLayout() {
        Observable.timer(400, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    public void onCompleted() {
                        mHeartLayout.setVisibility(View.VISIBLE);
                        delayDo2();
                    }

                    public void onError(Throwable e) {
                    }

                    public void onNext(Long aLong) {
                    }
                });
    }

    private void delayDo2() {
        Observable.timer((long) this.mRandom2.nextInt(200),
                TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    public void onCompleted() {
                        mHeartLayout.addHeart(randomColor());
                        delayDo2();
                    }

                    public void onError(Throwable e) {
                    }

                    public void onNext(Long aLong) {

                    }
                });
    }


    private void unBindDrawables(View view) {
        if (view != null) {
            try {
                Drawable drawable = view.getBackground();
                if (drawable != null) {
                    drawable.setCallback(null);
                } 
                if (view instanceof ViewGroup && !(view instanceof AdapterView)) {
                    ViewGroup viewGroup = (ViewGroup) view;
                    int viewGroupChildCount = viewGroup.getChildCount();
                    for (int j = 0; j < viewGroupChildCount; j++) {
                        unBindDrawables(viewGroup.getChildAt(j));
                    }
                    viewGroup.removeAllViews();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    private static final String FOLDER_NAME_ONE = "dongYu";
    /**
     * 获取asset目录下的文件夹的名称集合
     */
    public static ArrayList<String> getAssetImageFolderName() {
        ArrayList<String> assetsFolderNameList = new ArrayList();
        assetsFolderNameList.add(FOLDER_NAME_ONE);
        return assetsFolderNameList;
    }

}
