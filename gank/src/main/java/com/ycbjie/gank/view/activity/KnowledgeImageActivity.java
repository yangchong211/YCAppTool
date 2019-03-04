package com.ycbjie.gank.view.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ycbjie.gank.R;
import com.ycbjie.library.base.glide.GlideApp;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.library.constant.Constant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import cn.ycbjie.ycstatusbarlib.bar.StateAppBar;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/6/9
 *     desc  : 生活福利图片页面
 *     revise:
 * </pre>
 */
public class KnowledgeImageActivity extends BaseActivity implements View.OnClickListener {

    private ViewPager vpImage;
    private TextView tvText;
    private TextView tvSaveImage;
    private ImageView largeMore;
    private ImageView largeStar;
    private ImageView largeDownload;
    private ImageView largeShare;

    private int code;                               //接收穿过来当前选择的图片的数量
    private int selector;                           //用于判断是头像还是文章图片 1:头像 2：文章大图
    private boolean isLocal;                        //用于判断是否是加载本地图片
    private int page;                               //当前页数
    private ArrayList<String> imageUri;             //接收传过来的uri地址
    private boolean isApp;                          //是否是本应用中的图片
    private int imageId;
    private Bitmap bitmap;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_view_big_image;
    }

    @Override
    public void initView() {
        StateAppBar.setStatusBarColor(this, Color.BLACK);
        initFindById();
        initIntentData();
        initViewPagerData();
    }

    private void initFindById() {
        vpImage = findViewById(R.id.vp_image);
        tvText = findViewById(R.id.tv_text);
        tvSaveImage = findViewById(R.id.tv_save_image);
        largeMore =findViewById(R.id.large_more);
        largeStar = findViewById(R.id.large_star);
        largeDownload = findViewById(R.id.large_download);
        largeShare = findViewById(R.id.large_share);
    }


    private void initIntentData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            code = bundle.getInt("code");
            selector = bundle.getInt("selector");
            isLocal = bundle.getBoolean("isLocal", false);
            imageUri = bundle.getStringArrayList("imageUri");
            /*是否是本应用中的图片*/
            isApp = bundle.getBoolean("isApp", false);
            /*本应用图片的id*/
            imageId = bundle.getInt("id", 0);
        }
    }

    @Override
    public void initListener() {
        tvSaveImage.setOnClickListener(this);
        largeMore.setOnClickListener(this);
        largeDownload.setOnClickListener(this);
        largeShare.setOnClickListener(this);
        largeStar.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_save_image) {
            toSaveImage();
        } else if (i == R.id.large_more) {
            showMoreDialog();
        }
    }


    /**
     * 给viewpager设置适配器
     */
    @SuppressLint("SetTextI18n")
    private void initViewPagerData() {
        if (isApp) {
            MyPageAdapter myPageAdapter = new MyPageAdapter();
            vpImage.setAdapter(myPageAdapter);
            vpImage.setEnabled(false);
        } else {
            ViewPagerAdapter adapter = new ViewPagerAdapter();
            vpImage.setAdapter(adapter);
            vpImage.setCurrentItem(code);
            page = code;
            vpImage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @SuppressLint("SetTextI18n")
                @Override
                public void onPageSelected(int position) {
                    // 每当页数发生改变时重新设定一遍当前的页数和总页数
                    tvText.setText((position + 1) + " / " + imageUri.size());
                    page = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            vpImage.setEnabled(false);
            // 设定当前的页数和总页数
            if (selector == 2) {
                tvText.setText((code + 1) + " / " + imageUri.size());
            }
        }
    }


    /**
     * 保存图片到本地文件
     */
    private void toSaveImage() {
        ToastUtils.showShort("开始下载图片");
        if (isApp) {            // 本地图片
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageId);
            if (bitmap != null) {
                saveImageToFile(KnowledgeImageActivity.this, bitmap);
                ToastUtils.showShort("保存成功");
            }
        } else {                // 网络图片
            final BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // 子线程获得图片路径
                    final String imagePath = getImagePath(imageUri.get(page));
                    // 主线程更新
                    KnowledgeImageActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (imagePath != null) {
                                bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
                                if (bitmap != null) {
                                    saveImageToFile(KnowledgeImageActivity.this, bitmap);
                                    ToastUtils.showShort("已保存至" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/生活助手");
                                }
                            }
                        }
                    });
                }
            }).start();
        }
    }


    /**
     * 更多功能
     */
    private int yourChoice;
    String[] items = {"设为壁纸", "剪辑图片", "举报不良图片"};
    private void showMoreDialog() {
        yourChoice = -1;
        AlertDialog.Builder singleChoiceDialog = new AlertDialog.Builder(this);
        singleChoiceDialog.setTitle("更多选项");
        // 第二个参数是默认选项，此处设置为0
        singleChoiceDialog.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                yourChoice = which;
            }
        });
        singleChoiceDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (yourChoice){
                    case 0:
                        toSaveImage();
                        if(bitmap!=null){
                            Observable<Bitmap> observable = Observable.just(bitmap);
                            observable.map(new Func1<Bitmap, Integer>() {
                                @Override
                                public Integer call(Bitmap bitmap) {
                                    WallpaperManager wallpaperManager = WallpaperManager.getInstance(KnowledgeImageActivity.this);
                                    try {
                                        wallpaperManager.setBitmap(bitmap);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        return Constant.status.error;
                                    }
                                    return Constant.status.success;
                                }
                            })
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(callbackSubscriber);
                        }
                        break;
                    case 1:

                        break;
                    case 2:

                        break;
                    default:
                        break;
                }
            }
        });
        singleChoiceDialog.show();
    }


    /**
     * 本应用图片适配器
     */
    class MyPageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = getLayoutInflater().inflate(R.layout.view_pager_very_image, container, false);
            ImageView iv_image = view.findViewById(R.id.iv_image);
            ProgressBar spinner = view.findViewById(R.id.loading);
            spinner.setVisibility(View.GONE);
            if (imageId != 0) {
                iv_image.setImageResource(imageId);
            }
            container.addView(view, 0);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }


    /**
     * ViewPager的适配器
     */
    class ViewPagerAdapter extends PagerAdapter {

        LayoutInflater inflater;

        ViewPagerAdapter() {
            inflater = getLayoutInflater();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = inflater.inflate(R.layout.view_pager_very_image, container, false);
            final ImageView ivImage = view.findViewById(R.id.iv_image);
            final ProgressBar spinner = view.findViewById(R.id.loading);
            // 保存网络图片的路径
            String adapterImageEntity = (String) getItem(position);
            String imageUrl;
            if (isLocal) {
                imageUrl = "file://" + adapterImageEntity;
                tvSaveImage.setVisibility(View.GONE);
            } else {
                imageUrl = adapterImageEntity;
            }

            spinner.setVisibility(View.VISIBLE);
            spinner.setClickable(false);
            GlideApp.with(KnowledgeImageActivity.this)
                    .asDrawable()
                    .load(imageUrl)
                    .override(700)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Toast.makeText(getApplicationContext(), "资源加载异常", Toast.LENGTH_SHORT).show();
                            spinner.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            spinner.setVisibility(View.GONE);

                            /*这里应该是加载成功后图片的高*/
                            int height = ivImage.getHeight();

                            int wHeight = getWindowManager().getDefaultDisplay().getHeight();
                            if (height > wHeight) {
                                ivImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            } else {
                                ivImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                            }
                            return false;
                        }
                    })
                    .into(ivImage);
            container.addView(view, 0);
            return view;
        }

        @Override
        public int getCount() {
            if (imageUri == null || imageUri.size() == 0) {
                return 0;
            }
            return imageUri.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View arg0, @NonNull Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            View view = (View) object;
            container.removeView(view);
        }

        Object getItem(int position) {
            return imageUri.get(position);
        }
    }


    /**
     * 保存图片至自定义路径，刷新相册
     */
    public static void saveImageToFile(Context context, Bitmap bmp) {
        // 首先保存图片，这个路径可以自定义
        File appDir = new File(Environment.getExternalStorageDirectory(), "yc");
        // 测试由此抽象路径名表示的文件或目录是否存在
        if (!appDir.exists()) {
            //如果不存在，则创建由此抽象路径名命名的目录
            //noinspection ResultOfMethodCallIgnored
            appDir.mkdir();
        }
        // 然后自定义图片的文件名称
        String fileName = System.currentTimeMillis() + ".jpg";
        // 创建file对象
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.parse("file://" + file.getAbsoluteFile()));
        context.sendBroadcast(intent);
    }


    /**
     * Glide 获得图片缓存路径
     */
    private String getImagePath(String imgUrl) {
        String path = null;
        FutureTarget<File> future = Glide
                .with(KnowledgeImageActivity.this)
                .load(imgUrl)
                .downloadOnly(500, 500);
        try {
            File cacheFile = future.get();
            path = cacheFile.getAbsolutePath();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return path;
    }

    /**
     * 设置事件发生后的消费该事件的观察者
     */
    Subscriber<Integer> callbackSubscriber = new Subscriber<Integer>() {
        @Override
        public void onNext(Integer integer) {
            if (integer == Constant.status.success) {
                ToastUtils.showShort("设置失败");
            } else {
                ToastUtils.showShort("设置成功");
            }
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }
    };

}
