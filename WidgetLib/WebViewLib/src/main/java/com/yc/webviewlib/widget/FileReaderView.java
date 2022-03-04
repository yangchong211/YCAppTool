package com.yc.webviewlib.widget;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.tencent.smtt.sdk.TbsReaderView;
import com.yc.webviewlib.utils.X5LogUtils;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/10
 *     desc  : 文件阅读器  文件打开核心类
 *     revise:
 * </pre>
 */
public class FileReaderView extends FrameLayout implements TbsReaderView.ReaderCallback {

    private TbsReaderView mTbsReaderView;
    private Context context;

    public FileReaderView(Context context) {
        this(context, null, 0);
    }

    public FileReaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FileReaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTbsReaderView = getTbsReaderView(context);
        this.addView(mTbsReaderView, new LinearLayout.LayoutParams(-1, -1));
        this.context = context;
    }


    private TbsReaderView getTbsReaderView(Context context) {
        return new TbsReaderView(context, this);
    }

    /**
     * 初始化完布局调用此方法浏览文件
     * @param filePath  文件路径
     */
    public void show(String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                //加载文件
                Bundle localBundle = new Bundle();
                localBundle.putString("filePath", filePath);
                localBundle.putString("tempPath", Environment.getExternalStorageDirectory() + "/" + "TbsReaderTemp");
                if (this.mTbsReaderView == null) {
                    this.mTbsReaderView = getTbsReaderView(context);
                }
                boolean bool = this.mTbsReaderView.preOpen(getFileType(filePath), false);
                if (bool) {
                    this.mTbsReaderView.openFile(localBundle);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        } else {
            X5LogUtils.d("TAG"+ "文件路径无效！");
        }
    }

    @Override
    public void onCallBackAction(Integer integer, Object o, Object o1) {

    }

    /**
     * 务必在onDestroy方法中调用此方法，否则第二次打开无法浏览
     */
    public void stop() {
        if (mTbsReaderView != null) {
            mTbsReaderView.onStop();
        }
    }

    /***
     * 获取文件类型
     */
    private String getFileType(String paramString) {
        String str = "";
        if (TextUtils.isEmpty(paramString)) {
            return str;
        }
        int i = paramString.lastIndexOf('.');
        if (i <= -1) {
            return str;
        }
        str = paramString.substring(i + 1);
        return str;
    }

}
