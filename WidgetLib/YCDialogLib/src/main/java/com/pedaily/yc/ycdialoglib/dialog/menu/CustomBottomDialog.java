package com.pedaily.yc.ycdialoglib.dialog.menu;

import android.app.Application;
import android.content.Context;

import androidx.recyclerview.widget.OrientationHelper;

import java.util.List;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/5/2
 *     desc  : CustomDialog
 *     revise:
 * </pre>
 */
public class CustomBottomDialog {

    private CustomDialog customDialog;
    public static final int HORIZONTAL = OrientationHelper.HORIZONTAL;
    public static final int VERTICAL = OrientationHelper.VERTICAL;

    public CustomBottomDialog(Context context) {
        //注意这里增加一个判断，不能是全局上下文
        if (context instanceof Application){
            throw new IllegalStateException("context must not be application");
        }
        if (customDialog==null){
            customDialog = new CustomDialog(context);
        }
    }

    public CustomBottomDialog title(String title) {
        customDialog.title(title);
        return this;
    }

    public CustomBottomDialog title(int title) {
        customDialog.title(title);
        return this;
    }

    public CustomBottomDialog setCancel(boolean isShow , String text) {
        customDialog.setCancel(isShow, text);
        return this;
    }

    public CustomBottomDialog background(int res) {
        customDialog.background(res);
        return this;
    }

    public CustomBottomDialog inflateMenu(int menu, OnItemClickListener onItemClickListener) {
        customDialog.inflateMenu(menu, onItemClickListener);
        return this;
    }

    public CustomBottomDialog layout(int layout) {
        customDialog.layout(layout);
        return this;
    }

    public CustomBottomDialog orientation(int orientation) {
        customDialog.orientation(orientation);
        return this;
    }

    public CustomBottomDialog addItems(List<CustomItem> items, OnItemClickListener onItemClickListener) {
        customDialog.addItems(items, onItemClickListener);
        return this;
    }

    public CustomBottomDialog itemClick(OnItemClickListener listener) {
        customDialog.setItemClick(listener);
        return this;
    }

    public void show() {
        customDialog.show();
    }

}
