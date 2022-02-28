package com.yc.widget.loadingDialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;


import com.yc.widget.R;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/1/5
 * 描    述：自定义加载框
 * 修订历史：
 *          17年11月18日修改，添加dissMiss方法
 * ================================================
 */
public class LoadDialog extends Dialog {

    private static LoadDialog loadDialog;
    private boolean canNotCancel;
    private String tipMsg;

    /**
     * @param ctx                   Context
     * @param canNotCancel          boolean
     * @param tipMsg                String
     */
    private LoadDialog(final Context ctx, boolean canNotCancel, boolean showMsg, String tipMsg) {
        super(ctx);
        this.canNotCancel = canNotCancel;
        this.tipMsg = tipMsg;
        this.getContext().setTheme(android.R.style.Theme_InputMethod);
        setContentView(R.layout.layout_dialog_loading);

        TextView mShowMessage = (TextView) findViewById(R.id.message);
        if(showMsg){
            mShowMessage.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(this.tipMsg)) {
                mShowMessage.setText(this.tipMsg);
            }
        }else {
            mShowMessage.setVisibility(View.GONE);
        }

        Window window = getWindow();
        if(window!=null){
            WindowManager.LayoutParams attributesParams = window.getAttributes();
            attributesParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            attributesParams.dimAmount = 0.5f;
            window.setAttributes(attributesParams);
            window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (canNotCancel) {
                Toast.makeText(getContext(),tipMsg,Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    public static void show(Context context) {
        show(context, null,false, false);
    }


    public static void show(Context context, String message) {
        show(context, message,false, false);
    }

    /**
     * 展示加载窗
     * @param context               上下文
     * @param message               内容
     * @param showMsg               是否展示文字
     * @param isCancel              是否可以取消
     */
    public static void show(Context context, String message,boolean showMsg, boolean isCancel) {
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }
        if (loadDialog != null && loadDialog.isShowing()) {
            return;
        }
        loadDialog = new LoadDialog(context, isCancel,showMsg, message);
        loadDialog.show();
    }


    /**
     * 销毁加载窗
     * @param context               上下文
     */
    public static void dismiss(Context context) {
        try {
            if (context instanceof Activity) {
                if (((Activity) context).isFinishing()) {
                    loadDialog = null;
                    return;
                }
            }
            if (loadDialog != null && loadDialog.isShowing()) {
                Context loadContext = loadDialog.getContext();
                if (loadContext instanceof Activity) {
                    if (((Activity) loadContext).isFinishing()) {
                        loadDialog = null;
                        return;
                    }
                }
                loadDialog.dismiss();
                loadDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            loadDialog = null;
        }
    }

}
