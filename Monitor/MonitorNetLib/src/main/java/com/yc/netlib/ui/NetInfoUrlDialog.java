package com.yc.netlib.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.yc.netlib.R;
import com.yc.netlib.utils.NetWorkUtils;


public class NetInfoUrlDialog extends Dialog {

    private Context mContext;
    private Config mConfig;
    private TextView tvTitle;
    private TextView tvContent;
    private TextView tvNoSave;
    private TextView tvSave;

    public NetInfoUrlDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
        mConfig = new Config();
    }

    public NetInfoUrlDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        mConfig = new Config();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_net_url);
        initFindViewById();
        initListener();
        setCancelable(mConfig.canCancel);
        setCanceledOnTouchOutside(mConfig.canCancelOnTouchOutside);
        initData();
    }

    private void initFindViewById() {
        tvTitle = findViewById(R.id.tv_title);
        tvContent = findViewById(R.id.tv_content);
        tvNoSave = findViewById(R.id.tv_no_save);
        tvSave = findViewById(R.id.tv_save);
    }

    private void initListener() {
        tvNoSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mConfig.content!=null){
                    NetWorkUtils.copyToClipBoard(mContext, mConfig.content);
                }
                dismiss();
            }
        });
    }

    private void initData() {
        if (mConfig.title!=null && mConfig.title.length()>0){
            tvTitle.setText(mConfig.title);
        } else {
            tvTitle.setText("复制下面数据");
        }
        if (mConfig.content!=null){
            tvContent.setText(mConfig.content);
        } else {
            dismiss();
        }
    }

    /**
     * url
     */
    public NetInfoUrlDialog setData(String content) {
        mConfig.content = content;
        return this;
    }

    public NetInfoUrlDialog setTitle(String title) {
        mConfig.title = title;
        return this;
    }

    /**
     * 点击外部是否可以取消
     */
    public NetInfoUrlDialog shouldCancelOnTouchOutside(boolean flag) {
        mConfig.canCancelOnTouchOutside = flag;
        return this;
    }

    /**
     * 点击返回键是否可以取消
     */
    public NetInfoUrlDialog shouldCancelOnBackKeyDown(boolean flag) {
        mConfig.canCancel = flag;
        return this;
    }

    /**
     * 配置类
     */
    private static class Config {
        //是否可以取消
        boolean canCancel = true;
        //点击外部是否可以取消
        boolean canCancelOnTouchOutside = true;
        String content;
        String title;
    }

    /**
     * show方法显示弹窗
     */
    @Override
    public void show() {
        if (mContext ==null){
            return;
        }
        if (mContext instanceof Activity) {
            if (((Activity) mContext).isFinishing()) {
                return;
            }
        }
        if (isShowing()){
            return;
        }
        super.show();
    }


}
