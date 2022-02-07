package com.yc.toollib.crash;

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
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.text.Spannable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.yc.toollib.R;
import com.yc.toollib.tool.CompressUtils;
import com.yc.toollib.tool.ScreenShotsUtils;
import com.yc.toollib.tool.ToolFileUtils;
import com.yc.toollib.tool.ToolAppManager;

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
    public static final String IntentKey_FilePath = "IntentKey_FilePath";
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


    private Handler handler = new Handler();
    private LinearLayout mActivityCrashList;
    private LinearLayout mLlBack;
    private TextView mTvShare;
    private TextView mTvCopy;
    private TextView mTvScreenshot;
    private ScrollView mScrollViewCrashDetails;
    private TextView mTvContent;
    private ImageView mIvScreenShot;
    private LinearLayout mProgressView;
    private TextView mTvProgressbarMsg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash_details);
        initFindViewById();
        initView();
        initIntent();
        initView();
        initListener();
        initDatas();
    }

    private void initFindViewById() {
        mActivityCrashList = findViewById(R.id.activity_crash_list);
        mLlBack = findViewById(R.id.ll_back);
        mTvShare = findViewById(R.id.tv_share);
        mTvCopy = findViewById(R.id.tv_copy);
        mTvScreenshot = findViewById(R.id.tv_screenshot);
        mScrollViewCrashDetails = findViewById(R.id.scrollViewCrashDetails);
        mTvContent = findViewById(R.id.tv_content);
        mIvScreenShot = findViewById(R.id.iv_screen_shot);
        mProgressView = findViewById(R.id.progress_view);
        mTvProgressbarMsg = findViewById(R.id.tv_progressbar_msg);

    }

    private void initDatas() {
        showProgressLoading("加载中...");
        new Thread(new Runnable() {
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
                crashContent = ToolFileUtils.readFile2String(filePath);
                if (handler == null) {
                    return;
                }
                //获取所有Activity
                activitiesClass = ToolAppManager.getActivitiesClass(CrashDetailsActivity.this, getPackageName(), null);

                //富文本显示
                Spannable spannable = Spannable.Factory.getInstance().newSpannable(crashContent);

                //匹配错误信息
                if (!TextUtils.isEmpty(matchErrorInfo)) {
                    spannable = CrashLibUtils.addNewSpan(CrashDetailsActivity.this, spannable, crashContent, matchErrorInfo, Color.parseColor("#FF0006"), 18);
                }

                //匹配包名
                String packageName = getPackageName();
                spannable = CrashLibUtils.addNewSpan(CrashDetailsActivity.this, spannable, crashContent, packageName, Color.parseColor("#0070BB"), 0);

                //匹配Activity
                if (activitiesClass != null && activitiesClass.size() > 0) {
                    for (int i = 0; i < activitiesClass.size(); i++) {
                        spannable = CrashLibUtils.addNewSpan(CrashDetailsActivity.this, spannable, crashContent, activitiesClass.get(i).getSimpleName(), Color.parseColor("#55BB63"), 16);
                    }
                }

                //主线程处理
                final Spannable finalSpannable = spannable;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mTvContent != null) {
                            try {
                                mTvContent.setText(finalSpannable);
                            } catch (Exception e) {
                                mTvContent.setText(crashContent);
                            }
                        }
                    }
                });
            }
        }).start();
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
        filePath = getIntent().getStringExtra(IntentKey_FilePath);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 10086) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                shareLogs();
            } else {
                Toast.makeText(CrashDetailsActivity.this, "权限已拒绝", Toast.LENGTH_SHORT).show();
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
        final Bitmap bitmap = ScreenShotsUtils.measureSize(this,mScrollViewCrashDetails);
        new Thread(new Runnable() {
            @Override
            public void run() {
                savePicture(bitmap);
            }
        }).start();
    }

    private void savePicture(Bitmap bitmap) {
        if (bitmap != null) {
            String crashPicPath = ToolFileUtils.getCrashPicPath(CrashDetailsActivity.this) + "/crash_pic_" + System.currentTimeMillis() + ".jpg";
            boolean saveBitmap = CrashLibUtils.saveBitmap(CrashDetailsActivity.this, bitmap, crashPicPath);
            if (saveBitmap) {
                showToast("保存截图成功，请到相册查看\n路径：" + crashPicPath);
                final Bitmap bitmapCompress = CompressUtils.getBitmap(new File(crashPicPath), 200, 200);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        dismissProgressLoading();
                        //设置图片
                        mIvScreenShot.setImageBitmap(bitmapCompress);
                        //显示
                        mIvScreenShot.setVisibility(View.VISIBLE);
                        //设置宽高
                        ViewGroup.LayoutParams layoutParams = mIvScreenShot.getLayoutParams();
                        layoutParams.width = CrashLibUtils.getScreenWidth(CrashDetailsActivity.this);
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
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mIvScreenShot.setVisibility(View.GONE);
                            }
                        }, 3000);
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
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(CrashDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
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
    protected void onDestroy() {
        super.onDestroy();
        if (handler!=null){
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
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
            Toast.makeText(CrashDetailsActivity.this, "复制内容成功", Toast.LENGTH_SHORT).show();
        } else if (i == R.id.tv_screenshot) {
            //直接保存
            saveScreenShot();
        }
    }

    private void shareLogs() {
        //先把文件转移到外部存储文件
        File srcFile = new File(filePath);
        String destFilePath = ToolFileUtils.getCrashSharePath() + "/CrashShare.txt";
        File destFile = new File(destFilePath);
        boolean copy = ToolFileUtils.copyFile(srcFile, destFile);
        if (copy) {
            //分享
            CrashLibUtils.shareFile(CrashDetailsActivity.this, destFile);
        } else {
            Toast.makeText(CrashDetailsActivity.this, "文件保存失败", Toast.LENGTH_SHORT).show();
        }
    }

    public void showProgressLoading(String msg) {
        LinearLayout progress_view = findViewById(R.id.progress_view);
        TextView tv_progressbar_msg = findViewById(R.id.tv_progressbar_msg);
        if (progress_view != null) {
            progress_view.setVisibility(View.VISIBLE);
            tv_progressbar_msg.setText(msg);
            progress_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    public void dismissProgressLoading() {
        handler.post(new Runnable() {
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
