package com.ns.yc.lifehelper.weight.textSpan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/6/22
 * 描    述：TextSpan效果自定义控件
 * 修订历史：
 * ================================================
 */
public class AwesomeTextHandler {

    private final static int DEFAULT_RENDER_APPLY_MODE = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE;

    public interface ViewSpanRenderer {
        View getView(final String text, final Context context);
    }

    public interface ViewSpanClickListener {
        void onClick(String text, Context context);
    }

    private TextView view;
    private Context context;
    private Map<String, ViewSpanRenderer> renderers;

    public AwesomeTextHandler() {
        renderers = new HashMap<>();
    }

    public AwesomeTextHandler addViewSpanRenderer(String pattern, ViewSpanRenderer viewSpanRenderer) {
        renderers.put(pattern, viewSpanRenderer);
        if (view != null) {
            applyRenderers();
        }
        return this;
    }

    public Map<String, ViewSpanRenderer> getViewSpanRenderers() {
        return renderers;
    }

    public void setView(TextView view) {
        this.view = view;
        this.context = view.getContext();
        applyRenderers();
    }

    public void setText(String text) {
        if (view != null) {
            view.setText(text);
            applyRenderers();
        } else {
            throw new IllegalStateException("View mustn't be null");
        }
    }

    private void applyRenderers() {
        if (renderers != null) {
            Spannable spannableString = new SpannableString(view.getText());
            Set<String> spanPatterns = renderers.keySet();
            for (String spanPattern : spanPatterns) {
                Pattern pattern = Pattern.compile(spanPattern);
                Matcher matcher = pattern.matcher(spannableString);
                while (matcher.find()) {
                    int end = matcher.end();
                    int start = matcher.start();
                    ViewSpanRenderer renderer = renderers.get(spanPattern);
                    String text = matcher.group(0);
                    View view = renderer.getView(text, context);
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) convertViewToDrawable(view);
                    bitmapDrawable.setBounds(UPPER_LEFT_X, UPPER_LEFT_Y, bitmapDrawable.getIntrinsicWidth(), bitmapDrawable.getIntrinsicHeight());
                    spannableString.setSpan(new ImageSpan(bitmapDrawable), start, end, DEFAULT_RENDER_APPLY_MODE);
                    if (renderer instanceof ViewSpanClickListener) {
                        enableClickEvents();
                        ClickableSpan clickableSpan = getClickableSpan(text, (ViewSpanClickListener) renderer);
                        spannableString.setSpan(clickableSpan, start, end, DEFAULT_RENDER_APPLY_MODE);
                    }
                }
            }
            view.setText(spannableString);
        }
    }

    private void enableClickEvents() {
        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setHighlightColor(context.getResources().getColor(android.R.color.transparent));
    }

    private ClickableSpan getClickableSpan(final String text, final ViewSpanClickListener listener) {
        ClickableSpan clickableSpan = new ClickableSpanWithoutFormat() {
            @Override
            public void onClick(View view) {
                listener.onClick(text, context);
            }
        };
        return clickableSpan;
    }

    abstract class ClickableSpanWithoutFormat extends ClickableSpan {
        @Override
        public void updateDrawState(TextPaint ds) {

        }
    }

    private final static int UPPER_LEFT_X = 0;
    private final static int UPPER_LEFT_Y = 0;
    private Drawable convertViewToDrawable(View view) {
        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(spec, spec);
        view.layout(UPPER_LEFT_X, UPPER_LEFT_Y, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap b = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        c.translate(-view.getScrollX(), -view.getScrollY());
        view.draw(c);
        view.setDrawingCacheEnabled(true);
        Bitmap cacheBmp = view.getDrawingCache();
        Bitmap viewBmp = cacheBmp.copy(Bitmap.Config.ARGB_8888, true);
        view.destroyDrawingCache();
        return new BitmapDrawable(viewBmp);
    }

}

