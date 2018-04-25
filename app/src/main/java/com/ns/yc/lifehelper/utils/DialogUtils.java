package com.ns.yc.lifehelper.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.ui.me.view.activity.MeFeedBackActivity;
import com.ns.yc.lifehelper.ui.webView.view.WebViewActivity;
import com.pedaily.yc.ycdialoglib.selectDialog.CustomSelectDialog;

import org.yczbj.ycvideoplayerlib.VideoPlayerUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/9/27
 *     desc  : 对话框工具类
 *     revise:
 * </pre>
 */
public class DialogUtils {

    private static final String QQ_URL = "http://android.myapp.com/myapp/detail.htm?apkName=com.zero2ipo.harlanhu.pedaily";

    private static ProgressDialog dialog;
    public static void showProgressDialog(Activity activity) {
        if (dialog == null) {
            dialog = new ProgressDialog(activity);
            dialog.setMessage("玩命加载中……");
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(false);
        }
        dialog.show();
    }

    public static void dismissProgressDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    /**
     * 自定义PopupWindow
     */
    public static void showCustomPopupWindow(final Activity activity){
        if(VideoPlayerUtils.isActivityLiving(activity)){
            View popMenuView = activity.getLayoutInflater().inflate(R.layout.dialog_star_feedback_view, null);
            final PopupWindow popMenu = new PopupWindow(popMenuView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
            popMenu.setClippingEnabled(false);
            popMenu.setFocusable(true);
            popMenu.showAtLocation(popMenuView, Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
            AppUtil.setBackgroundAlpha(activity,0.5f);

            TextView tvStar = (TextView) popMenuView.findViewById(R.id.tv_star);
            TextView tvFeedback = (TextView) popMenuView.findViewById(R.id.tv_feedback);
            TextView tvLook = (TextView) popMenuView.findViewById(R.id.tv_look);
            tvStar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //吐槽跳转意见反馈页面
                    activity.startActivity(new Intent(activity, MeFeedBackActivity.class));
                    popMenu.dismiss();
                }
            });
            tvFeedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(AppUtil.isPkgInstalled(activity,"com.tencent.android.qqdownloader")){
                        ArrayList<String> installAppMarkets = GoToScoreUtils.getInstallAppMarkets(activity);
                        ArrayList<String> filterInstallMarkets = GoToScoreUtils.getFilterInstallMarkets(activity, installAppMarkets);
                        GoToScoreUtils.launchAppDetail(activity,"com.zero2ipo.harlanhu.pedaily",filterInstallMarkets.get(0));
                    }else {
                        Intent intent = new Intent(activity, WebViewActivity.class);
                        intent.putExtra("url", QQ_URL);
                        activity.startActivity(intent);
                    }
                    popMenu.dismiss();
                }
            });
            tvLook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popMenu.dismiss();
                }
            });
            popMenu.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    AppUtil.setBackgroundAlpha(activity,1.0f);
                }
            });
        }
    }


    /**
     * 展示对话框视图，构造方法创建对象
     */
    public static CustomSelectDialog showDialog(Activity activity ,
                                                CustomSelectDialog.SelectDialogListener listener,
                                                List<String> names) {
        CustomSelectDialog dialog = new CustomSelectDialog(activity,
                R.style.transparentFrameWindowStyle, listener, names);
        if(VideoPlayerUtils.isActivityLiving(activity)){
            dialog.show();
        }
        return dialog;
    }




}
