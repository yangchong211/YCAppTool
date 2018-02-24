package com.ns.yc.lifehelper.weight.textSpan;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.ns.yc.lifehelper.R;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/6/22
 * 描    述：TextSpan效果自定义控件
 * 修订历史：
 * ================================================
 */
public class TagsSpanRenderer implements AwesomeTextHandler.ViewSpanRenderer {

    private final static float textSizeInDips = 14.0f;
    private final static int backgroundResource = R.color.colorTransparent;
    private final static int textColorResource = android.R.color.holo_red_dark;

    @Override
    public View getView(final String text, final Context context) {
        TextView view = new TextView(context);
        view.setText(text.substring(1));
        view.setTextSize(SizeUtils.dp2px(textSizeInDips));
        view.setBackgroundResource(backgroundResource);
        int textColor = context.getResources().getColor(textColorResource);
        view.setTextColor(textColor);
        return view;
    }
}
