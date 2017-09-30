package com.ns.yc.lifehelper.ui.other.clock;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;

public class SimpleDialog extends Dialog {

    protected static int default_width = WindowManager.LayoutParams.WRAP_CONTENT; // 默认宽度
    protected static int default_height = WindowManager.LayoutParams.WRAP_CONTENT;// 默认高度
    public static int TYPE_TWO_BT = 2;
    public static int TYPE_NO_BT = 0;
    public TextView dialog_title;
    public EditText dialog_message;
    public TextView dialog_confirm;
    protected Context mContext;
    private View.OnClickListener listener;
    private View customView;
    ImageView icon;



    public SimpleDialog(Context context, int style) {
        super(context, R.style.FullScreenDialog);
        mContext = context;
        customView = LayoutInflater.from(context).inflate(R.layout.dialog_clock_simple, null);
        icon = (ImageView) customView.findViewById(R.id.icon);
        dialog_confirm = (TextView) customView.findViewById(R.id.dialog_confirm);
        dialog_title = (TextView) customView.findViewById(R.id.dialog_title);
        setTitle("提示信息");
        dialog_message = (EditText) customView.findViewById(R.id.dialog_message);
        dialog_message.clearFocus();
    }

    @Override
    public boolean onKeyDown(int keyCode,@NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(customView);
    }

    public SimpleDialog setClickListener(View.OnClickListener listener) {
        this.listener = listener;
        dialog_confirm.setOnClickListener(listener);
        return this;
    }

    public SimpleDialog setMessage(String message) {
        dialog_message.setText(message);
        return this;
    }

    public SimpleDialog setTitle(String title) {
        dialog_title.setText(title);
        return this;
    }

    public SimpleDialog setIcon(int iconResId) {
        dialog_title.setVisibility(View.GONE);
        icon.setVisibility(View.VISIBLE);
        icon.setBackgroundResource(iconResId);
        return this;
    }

}
