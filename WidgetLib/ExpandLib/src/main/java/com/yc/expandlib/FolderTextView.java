package com.yc.expandlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211/YCWidgetLib
 *     time  : 2019/10/24
 *     desc  : 自定义折叠文本
 *     revise:
 * </pre>
 */
public class FolderTextView extends AppCompatTextView {

    private static final String ELLIPSIS="...";
    private String foldText = "收起";
    private String unfoldText = "查看详情";
    /**
     * 收起状态
     */
    private boolean isFold = false;
    /**
     * 绘制，防止重复进行绘制
     */
    private boolean isDraw = false;
    /**
     * 内部绘制
     */
    private boolean isInner = false;
    /**
     * 折叠行数
     */
    private int foldLine;
    /**
     * 全文本
     */
    private String fullText;
    /**
     * link文本的颜色
     */
    private int linkColor = 0;
    private float mSpacingMult = 1.0f;
    private float mSpacingAdd = 0.0f;
    
    public FolderTextView(Context context) {
        this(context, null);
    }

    public FolderTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FolderTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context);
    }

    private void initAttrs(Context context) {
        TypedArray a = context.obtainStyledAttributes(R.styleable.FolderTextView);
        foldLine = a.getInt(R.styleable.FolderTextView_foldline, 3);
        a.recycle();
    }

    /**
     * 不更新全文本下，进行展开和收缩操作
     * @param text                  内容
     */
    private void setUpdateText(CharSequence text){
        isInner = true;
        setText(text);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if(TextUtils.isEmpty(fullText) || !isInner){
            isDraw = false;
            fullText = String.valueOf(text);
        }
        super.setText(text, type);
    }

    @Override
    public void setLineSpacing(float add, float mult) {
        mSpacingAdd = add;
        mSpacingMult = mult;
        super.setLineSpacing(add, mult);
    }
    
    private Layout makeTextLayout(String text) {
        return new StaticLayout(text, getPaint(), getWidth() - getPaddingLeft() - getPaddingRight(),
                Layout.Alignment.ALIGN_NORMAL, mSpacingMult, mSpacingAdd, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(!isDraw){
            resetText();
        }
        super.onDraw(canvas);
        isDraw = true;
        isInner = false;
    }

    private void resetText() {
        String spanText = fullText;
        SpannableString spanStr;
        //收缩状态
        if(isFold){
            spanStr = createUnFoldSpan(spanText);
        }else{ 
            //展开状态
            spanStr = createFoldSpan(spanText);
        }
        setUpdateText(spanStr);
        setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * 创建展开状态下的Span
     * @param text                  源文本
     * @return                      字符串
     */
    private SpannableString createUnFoldSpan(String text) {
        String destStr = text + getFoldText();
        int start = destStr.length() - getFoldText().length();
        int end = destStr.length();
        SpannableString spanStr = new SpannableString(destStr);
        spanStr.setSpan(clickSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanStr;
    }

    /**
     * 创建收缩状态下的Span
     * @param text                  源文本
     * @return                      字符串
     */
    private SpannableString createFoldSpan(String text) {
        String destStr = tailorText(text);
        int start = destStr.length() - getUnfoldText().length();
        int end = destStr.length();
        SpannableString spanStr = new SpannableString(destStr);
        spanStr.setSpan(clickSpan,start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanStr;
    }

    /**
     * 裁剪文本至固定行数
     * @param text                  源文本
     * @return                      字符串
     */
    private String tailorText(String text){
        String destStr = text + ELLIPSIS + getUnfoldText();
        Layout layout = makeTextLayout(destStr);
        //如果行数大于固定行数
        if(layout.getLineCount() > getFoldLine()){
            int index = layout.getLineEnd(getFoldLine());
            if(text.length() < index){
                index = text.length();
            }
            //从最后一位逐渐试错至固定行数
            String subText = text.substring(0, index-1); 
            return tailorText(subText);
        }else{
            return destStr;
        }
    }

    private ClickableSpan clickSpan = new ClickableSpan() {
        @Override
        public void onClick(@NonNull View widget) {
            isFold = !isFold;
            isDraw = false;
            invalidate();
        }

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
            if (getLinkColor()==0){
                ds.setColor(ds.linkColor);
            } else {
                ds.setColor(getLinkColor());
            }
        }
    };

    /**------------------------------下面是设置属性方法-----------------------------------------*/

    public int getFoldLine() {
        return foldLine;
    }

    public void setFoldLine(int foldLine) {
        this.foldLine = foldLine;
    }

    public String getFoldText() {
        return foldText;
    }

    public void setFoldText(String foldText) {
        this.foldText = foldText;
    }

    public String getUnfoldText() {
        return unfoldText;
    }

    public void setUnfoldText(String unfoldText) {
        this.unfoldText = unfoldText;
    }

    public int getLinkColor() {
        return linkColor;
    }

    public void setLinkColor(@ColorInt int linkColor) {
        this.linkColor = linkColor;
    }
}
