package com.yc.crash.lib;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.yc.activitymanager.ActivityManager;
import com.yc.appcompress.AppCompress;
import com.yc.appfilelib.AppFileUtils;
import com.yc.appmediastore.FileShareUtils;
import com.yc.appscreenlib.AppShotsUtils;
import com.yc.crash.R;
import com.yc.easyexecutor.DelegateTaskExecutor;
import com.yc.fileiohelper.BufferIoUtils;
import com.yc.fileiohelper.FileIoUtils;
import com.yc.imagetoollib.ImageSaveUtils;
import com.yc.statusbar.bar.StateAppBar;
import com.yc.toastutils.ToastUtils;
import com.yc.toolutils.AppWindowUtils;

import java.io.File;
import java.util.List;


/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/7/10
 *     desc  : 崩溃详情页面展示
 *     revise:
 * </pre>
 */
public class CrashDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Intent 传递的文件路径
     */
    public static final String INTENT_KEY_FILE_PATH = "IntentKey_FilePath";
    /**
     * 文件路径
     */
    private String filePath;
    /**
     * 崩溃日志的内容
     */
    private String crashContent;
    /**
     * 具体的异常类型
     */
    private String matchErrorInfo;
    /**
     * 所有Activity的集合
     */
    private List<Class> activitiesClass;
    private LinearLayout mLlBack;
    private TextView mTvShare;
    private TextView mTvCopy;
    private TextView mTvScreenshot;
    private ScrollView mScrollViewCrashDetails;
    private TextView mTvContent;
    private ImageView mIvScreenShot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash_details);
        StateAppBar.setStatusBarColor(this,
                ContextCompat.getColor(this, R.color.crash_tool_bar_color));
        initFindViewById();
        initView();
        initIntent();
        initView();
        initListener();
        initDatas();
    }

    private void initFindViewById() {
        mLlBack = findViewById(R.id.ll_back);
        mTvShare = findViewById(R.id.tv_share);
        mTvCopy = findViewById(R.id.tv_copy);
        mTvScreenshot = findViewById(R.id.tv_screenshot);
        mScrollViewCrashDetails = findViewById(R.id.scrollViewCrashDetails);
        mTvContent = findViewById(R.id.tv_content);
        mIvScreenShot = findViewById(R.id.iv_screen_shot);

    }

    private void initDatas() {
        showProgressLoading("加载中...");
        DelegateTaskExecutor.getInstance().executeOnDiskIO(new Runnable() {
            @Override
            public void run() {
                dismissProgressLoading();
                //获取文件夹名字匹配异常信息高亮显示
                File file = new File(filePath);
                String[] splitNames = file.getName().replace(".txt", "").split("_");
                if (splitNames.length == 3) {
                    String errorMsg = splitNames[2];
                    if (!TextUtils.isEmpty(errorMsg)) {
                        matchErrorInfo = errorMsg;
                    }
                }
                //获取内容
                crashContent = BufferIoUtils.readFile2String1(filePath);
                //获取所有Activity
                activitiesClass = ActivityManager.getInstance().getActivitiesClass(CrashDetailsActivity.this, getPackageName(), null);

                //富文本显示
                Spannable spannable = Spannable.Factory.getInstance().newSpannable(crashContent);

                //匹配错误信息
                if (!TextUtils.isEmpty(matchErrorInfo)) {
                    CrashHelperUtils.addNewSpan(CrashDetailsActivity.this, spannable, crashContent, matchErrorInfo, Color.parseColor("#FF0006"), 18);
                }

                //匹配包名
                String packageName = getPackageName();
                CrashHelperUtils.addNewSpan(CrashDetailsActivity.this, spannable, crashContent, packageName, Color.parseColor("#0070BB"), 0);

                //匹配Activity
                if (activitiesClass != null && activitiesClass.size() > 0) {
                    for (int i = 0; i < activitiesClass.size(); i++) {
                        CrashHelperUtils.addNewSpan(CrashDetailsActivity.this, spannable, crashContent, activitiesClass.get(i).getSimpleName(), Color.parseColor("#55BB63"), 16);
                    }
                }

                //主线程处理
                DelegateTaskExecutor.getInstance().postToMainThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mTvContent != null) {
                            try {
                                mTvContent.setText(spannable);
                            } catch (Exception e) {
                                mTvContent.setText(crashContent);
                            }
                        }
                    }
                });
            }
        });
    }

    private void initView() {

    }

    private void initListener(){
        mLlBack.setOnClickListener(this);
        mTvShare.setOnClickListener(this);
        mTvCopy.setOnClickListener(this);
        mTvScreenshot.setOnClickListener(this);
    }

    private void initIntent() {
        filePath = getIntent().getStringExtra(INTENT_KEY_FILE_PATH);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 10086) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                shareLogs();
            } else {
                ToastUtils.showRoundRectToast("权限已拒绝");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 保存截图
     */
    private void saveScreenShot() {
        showProgressLoading("正在保存截图...");
        //生成截图
        final Bitmap bitmap = AppShotsUtils.measureSize(this,mScrollViewCrashDetails);
        DelegateTaskExecutor.getInstance().executeOnCpu(new Runnable() {
            @Override
            public void run() {
                savePicture(bitmap);
            }
        });
    }

    private void savePicture(Bitmap bitmap) {
        if (bitmap != null) {
            String crashPicPath = CrashHelperUtils.getCrashPicPath(CrashDetailsActivity.this) + "/crash_pic_" + System.currentTimeMillis() + ".jpg";
            boolean saveBitmap = ImageSaveUtils.saveBitmap(CrashDetailsActivity.this, bitmap, crashPicPath);
            if (saveBitmap) {
                showToast("保存截图成功，请到相册查看\n路径：" + crashPicPath);
                final Bitmap bitmapCompress = AppCompress.getInstance().compressSizePath(crashPicPath);
                DelegateTaskExecutor.getInstance().postToMainThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissProgressLoading();
                        //设置图片
                        mIvScreenShot.setImageBitmap(bitmapCompress);
                        //显示
                        mIvScreenShot.setVisibility(View.VISIBLE);
                        //设置宽高
                        ViewGroup.LayoutParams layoutParams = mIvScreenShot.getLayoutParams();
                        layoutParams.width = AppWindowUtils.getScreenWidth();
                        layoutParams.height = bitmapCompress.getHeight() * layoutParams.width / bitmapCompress.getWidth();
                        mIvScreenShot.setLayoutParams(layoutParams);
                        //设置显示动画
                        mIvScreenShot.setPivotX(0);
                        mIvScreenShot.setPivotY(0);
                        AnimatorSet animatorSetScale = new AnimatorSet();
                        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mIvScreenShot, "scaleX", 1, 0.2f);
                        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mIvScreenShot, "scaleY", 1, 0.2f);
                        animatorSetScale.setDuration(1000);
                        animatorSetScale.setInterpolator(new DecelerateInterpolator());
                        animatorSetScale.play(scaleX).with(scaleY);
                        animatorSetScale.start();

                        //三秒后消失
                        DelegateTaskExecutor.getInstance().postToMainThread(new Runnable() {
                            @Override
                            public void run() {
                                mIvScreenShot.setVisibility(View.GONE);
                            }
                        },3000);
                    }
                });
            } else {
                showToast("保存截图失败");
                dismissProgressLoading();
            }
        } else {
            showToast("保存截图失败");
            dismissProgressLoading();
        }
    }

    private void showToast(final String msg) {
        DelegateTaskExecutor.getInstance().postToMainThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showRoundRectToast(msg);
            }
        });
    }

    /**
     * 添加到剪切板
     */
    public void putTextIntoClip() {
        ClipboardManager clipboardManager = (ClipboardManager)
                CrashDetailsActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
        //创建ClipData对象
        ClipData clipData = ClipData.newPlainText("CrashLog", crashContent);
        //添加ClipData对象到剪切板中
        clipboardManager.setPrimaryClip(clipData);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ll_back) {
            finish();
        } else if (i == R.id.tv_share) {
            //先把文件转移到外部存储文件
            //请求权限
            //检查版本是否大于M
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10086);
            } else {
                shareLogs();
            }
        } else if (i == R.id.tv_copy) {
            //复制
            putTextIntoClip();
            ToastUtils.showRoundRectToast("复制内容成功");
        } else if (i == R.id.tv_screenshot) {
            //直接保存
            saveScreenShot();
        }
    }

    private void shareLogs() {
        //先把文件转移到外部存储文件
        String destFilePath = AppFileUtils.getExternalFilePath(this,"CrashShare.txt");
        File destFile = new File(destFilePath);
        boolean copy = FileIoUtils.copyFile2(filePath, destFilePath);
        if (copy) {
            //分享
            FileShareUtils.shareFile(CrashDetailsActivity.this, destFile);
        } else {
            ToastUtils.showRoundRectToast("文件保存失败");
        }
    }

    public void showProgressLoading(String msg) {
        LinearLayout progressView = findViewById(R.id.progress_view);
        TextView tvProgressbarMsg = findViewById(R.id.tv_progressbar_msg);
        if (progressView != null) {
            progressView.setVisibility(View.VISIBLE);
            tvProgressbarMsg.setText(msg);
            progressView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    public void dismissProgressLoading() {
        DelegateTaskExecutor.getInstance().postToMainThread(this,new Runnable() {
            @Override
            public void run() {
                LinearLayout progress_view = findViewById(R.id.progress_view);
                TextView tv_progressbar_msg = findViewById(R.id.tv_progressbar_msg);
                if (progress_view != null) {
                    progress_view.setVisibility(View.GONE);
                    tv_progressbar_msg.setText("加载中...");
                }
            }
        });
    }

}
