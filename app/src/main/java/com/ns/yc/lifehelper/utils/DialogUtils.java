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
import com.ycbjie.library.arounter.ARouterConstant;
import com.ycbjie.library.arounter.ARouterUtils;
import com.ycbjie.library.utils.WindowUtils;
import com.ycbjie.library.web.view.WebViewActivity;
import com.pedaily.yc.ycdialoglib.dialog.CustomSelectDialog;
import com.ycbjie.library.utils.AppUtils;


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
        if(AppUtils.isActivityLiving(activity)){
            View popMenuView = activity.getLayoutInflater().inflate(R.layout.dialog_custom_window, null);
            final PopupWindow popMenu = new PopupWindow(popMenuView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
            popMenu.setClippingEnabled(false);
            popMenu.setFocusable(true);
            popMenu.showAtLocation(popMenuView, Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
            WindowUtils.setBackgroundAlpha(activity,0.5f);

            TextView tvStar = popMenuView.findViewById(R.id.tv_star);
            TextView tvFeedback = popMenuView.findViewById(R.id.tv_feedback);
            TextView tvLook = popMenuView.findViewById(R.id.tv_look);
            tvStar.setOnClickListener(v -> {
                //吐槽跳转意见反馈页面
                ARouterUtils.navigation(ARouterConstant.ACTIVITY_OTHER_FEEDBACK);
                popMenu.dismiss();
            });
            tvFeedback.setOnClickListener(v -> {
                if(AppUtils.isPkgInstalled(activity,"com.tencent.android.qqdownloader")){
                    ArrayList<String> installAppMarkets = GoToScoreUtils.getInstallAppMarkets(activity);
                    ArrayList<String> filterInstallMarkets = GoToScoreUtils.getFilterInstallMarkets(activity, installAppMarkets);
                    GoToScoreUtils.launchAppDetail(activity,"com.zero2ipo.harlanhu.pedaily",filterInstallMarkets.get(0));
                }else {
                    Intent intent = new Intent(activity, WebViewActivity.class);
                    intent.putExtra("url", QQ_URL);
                    activity.startActivity(intent);
                }
                popMenu.dismiss();
            });
            tvLook.setOnClickListener(v -> popMenu.dismiss());
            popMenu.setOnDismissListener(() -> WindowUtils.setBackgroundAlpha(activity,1.0f));
        }
    }


    /**
     * 展示对话框视图，构造方法创建对象
     */
    public static CustomSelectDialog showDialog(Activity activity ,
                                                CustomSelectDialog.SelectDialogListener listener,
                                                List<String> names) {
        CustomSelectDialog dialog = new CustomSelectDialog(activity,
                R.style.TransparentFrameWindowStyle, listener, names);
        if(AppUtils.isActivityLiving(activity)){
            dialog.show();
        }
        return dialog;
    }




}
