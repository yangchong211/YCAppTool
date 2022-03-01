package com.pedaily.yc.ycdialoglib.popup;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.pedaily.yc.ycdialoglib.utils.DialogUtils;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/06/4
 *     desc  : PopupWindow抽象类
 *     revise:
 * </pre>
 */
public abstract class BasePopDialog extends PopupWindow {

    private Context mContext;
    private View contentView;


    public BasePopDialog(Context context) {
        super(context);
        mContext=context;
        init();
        initData(contentView);
    }


    private void init(){
        contentView = LayoutInflater.from(mContext).inflate(getViewResId(), null);
        setContentView(contentView);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new BitmapDrawable());
        /*ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);*/
        setOutsideTouchable(false);
        // 设置PopupWindow可获得焦点
        setFocusable(true);
        // 设置PopupWindow可触摸
        setTouchable(true);
    }

    /**
     * 这两个抽象方法子类必须实现
     */
    public abstract int getViewResId();
    public abstract void initData(View contentView);

    /**
     * 设置屏幕透明度
     * @param bgAlpha               透明度
     */
    public void setBgAlpha(float bgAlpha){
        DialogUtils.setBackgroundAlpha((Activity) mContext,bgAlpha);
    }

    /**
     * 设置延迟
     * @param time 毫秒
     */
    public void setDelayedMsDismiss(long time){
        mHandler.postDelayed(delayedRun,time);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        DialogUtils.setBackgroundAlpha((Activity) mContext,1f);
        if(mHandler!=null){
            if(delayedRun!=null){
                mHandler.removeCallbacks(delayedRun);
            }else {
                mHandler.removeCallbacksAndMessages(null);
            }
            mHandler = null;
        }
    }

    @Override
    public int getWidth(){
        if(contentView==null){
            return 0;
        }
        contentView.measure(0   ,0);
        return contentView.getMeasuredWidth();
    }

    @Override
    public int getHeight(){
        if(contentView==null){
            return 0;
        }
        contentView.measure(0   ,0);
        return contentView.getMeasuredHeight();
    }


    private Runnable delayedRun =  new Runnable() {
        @Override
        public void run() {
            dismiss();
        }
    };

    private Handler mHandler = new Handler();
}
