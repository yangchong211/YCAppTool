package com.yc.apptextview;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Layout;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/07/18
 *     desc  : 文本两端对齐
 *     revise:
 * </pre>
 */
public class BalanceTextView extends AppCompatTextView {

    private static final String TAG = BalanceTextView.class.getSimpleName();

    /**
     * 起始位置
     */
    private static final int GRAVITY_START = 1001;
    /**
     * 结尾位置
     */
    private static final int GRAVITY_END = 1002;

    /**
     * 中间位置
     */
    private static final int GRAVITY_CENTER = 1003;

    /**
     * 绘制文字的起始Y坐标
     */
    private float mLineY;

    /**
     * 文字的宽度
     */
    private int mViewWidth;

    /**
     * 段落间距
     */
    private int paragraphSpacing = TextViewUtils.dipToPx(getContext(), 15);

    /**
     * 行间距
     */
    private int lineSpacing = TextViewUtils.dipToPx(getContext(), 2);

    /**
     * 当前所有行数的集合
     */
    private ArrayList<List<List<String>>> mParagraphLineList;

    /**
     * 当前所有的行数
     */
    private int mLineCount;

    /**
     * 每一段单词的内容集合
     */
    private ArrayList<List<String>> mParagraphWordList;
    /**
     * 空格字符
     */
    private static final String BLANK = " ";
    /**
     * 英语单词元音字母
     */
    private String[] vowel = {"a", "e", "i", "o", "u"};

    /**
     * 英语单词元音字母集合
     */
    private List<String> vowels = Arrays.asList(vowel);
    /**
     * 当前测量的间距
     */
    private int measuredWidth;
    /**
     * 左padding
     */
    private int paddingStart;
    /**
     * 右padding
     */
    private int paddingEnd;
    /**
     * 顶padding
     */
    private int paddingTop;
    /**
     * 底padding
     */
    private int paddingBottom;
    /**
     * 布局的方向
     */
    private int textGravity;

    public BalanceTextView(Context context) {
        this(context,null);
    }

    public BalanceTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BalanceTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (isSpan()) {
            return;
        }
        mParagraphLineList = null;
        mParagraphWordList = null;
        mLineY = 0;
        //获取测量的宽度
        measuredWidth = getMeasuredWidth();
        //获取上下左右的padding值
        paddingStart = getPaddingStart();
        paddingEnd = getPaddingEnd();
        paddingTop = getPaddingTop();
        paddingBottom = getPaddingBottom();
        //这个是view的宽度 = 测量宽度 - 左间距 - 右间距
        mViewWidth = measuredWidth - paddingStart - paddingEnd;
        getParagraphList();
        for (List<String> frontList : mParagraphWordList) {
            mParagraphLineList.add(getLineList(frontList));
        }
        Log.i(TAG, "onMeasure----------");
        setMeasuredDimension(measuredWidth,
                (mParagraphLineList.size() - 1) * paragraphSpacing
                        + mLineCount * (getLineHeight() + lineSpacing) + paddingTop + paddingBottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isSpan()) {
            super.onDraw(canvas);
            return;
        }
        Log.i(TAG, "onDraw----------");
        TextPaint paint = getPaint();
        paint.setColor(getCurrentTextColor());
        paint.drawableState = getDrawableState();
        mLineY = 0;
        float textSize = getTextSize();
        mLineY += textSize + paddingTop;
        Layout layout = getLayout();
        if (layout == null) {
            return;
        }
        textGravity = getTextGravity();
        adjust(canvas, paint);
    }

    /**
     * 计算每一段绘制的内容
     * @param frontList
     * @return
     */
    private synchronized List<List<String>> getLineList(List<String> frontList) {
        Log.i(TAG, "getLineList-----");
        StringBuilder sb = new StringBuilder();
        List<List<String>> lineLists = new ArrayList<>();
        List<String> lineList = new ArrayList<>();
        float width = 0;
        String temp = "";
        String front = "";
        for (int i = 0; i < frontList.size(); i++) {
            front = frontList.get(i);
            if (!TextUtils.isEmpty(temp)) {
                sb.append(temp);
                lineList.add(temp);
                if (!TextViewUtils.isCN(temp)) {
                    sb.append(BLANK);
                }
                temp = "";
            }

            if (TextViewUtils.isCN(front)) {
                sb.append(front);
            } else {
                if ((i + 1) < frontList.size()) {
                    String nextFront = frontList.get(i + 1);
                    if (TextViewUtils.isCN(nextFront)) {
                        sb.append(front);
                    } else {
                        sb.append(front).append(BLANK);
                    }
                } else {
                    sb.append(front);
                }
            }

            lineList.add(front);
            width = StaticLayout.getDesiredWidth(sb.toString(), getPaint());

            if (width > mViewWidth) {
                // 先判断最后一个单词是否是英文的，是的话则切割，否则的话就移除最后一个
                int lastIndex = lineList.size() - 1;
                String lastWord = lineList.get(lastIndex);
                String lastTemp = "";
                lineList.remove(lastIndex);
                if (TextViewUtils.isCN(lastWord)) {
                    addLines(lineLists, lineList);
                    lastTemp = lastWord;
                } else {
                    // 否则的话则截取字符串
                    String substring = sb.substring(0, sb.length() - lastWord.length() - 1);
                    sb.delete(0, sb.toString().length());
                    sb.append(substring).append(BLANK);
                    String tempLastWord = "";
                    int length = lastWord.length();
                    if (length <= 3) {
                        addLines(lineLists, lineList);
                        lastTemp = lastWord;
                    } else {
                        int cutoffIndex = 0;
                        for (int j = 0; j < length; j++) {
                            tempLastWord = String.valueOf(lastWord.charAt(j));
                            sb.append(tempLastWord);
                            if (vowels.contains(tempLastWord)) {
                                // 根据元音字母来进行截断
                                if (j + 1 < length) {
                                    String nextTempLastWord = String.valueOf(lastWord.charAt(j + 1));
                                    sb.append(nextTempLastWord);
                                    width = StaticLayout.getDesiredWidth(sb.toString(), getPaint());
                                    cutoffIndex = j;
                                    if (width > mViewWidth) {
                                        if (j > 2 && j <= length - 2) {
                                            // 单词截断后，前面的字符小于2个时，则不进行截断
                                            String lastFinalWord = lastWord.substring(0, cutoffIndex + 2) + "-";
                                            lineList.add(lastFinalWord);
                                            addLines(lineLists, lineList);
                                            lastTemp = lastWord.substring(cutoffIndex + 2, length);

                                        } else {
                                            addLines(lineLists, lineList);
                                            lastTemp = lastWord;
                                        }
                                        break;
                                    }
                                } else {
                                    addLines(lineLists, lineList);
                                    lastTemp = lastWord;
                                    break;
                                }
                            }

                            width = StaticLayout.getDesiredWidth(sb.toString(), getPaint());

                            // 找不到元音，则走默认的逻辑
                            if (width > mViewWidth) {
                                if (j > 2 && j <= length - 2) {
                                    // 单词截断后，前面的字符小于2个时，则不进行截断
                                    String lastFinalWord = lastWord.substring(0, j) + "-";
                                    lineList.add(lastFinalWord);
                                    addLines(lineLists, lineList);
                                    lastTemp = lastWord.substring(j, length);

                                } else {
                                    addLines(lineLists, lineList);
                                    lastTemp = lastWord;
                                }
                                break;
                            }
                        }
                    }
                }
                sb.delete(0, sb.toString().length());
                temp = lastTemp;

            }

            if (lineList.size() > 0 && i == frontList.size() - 1) {
                addLines(lineLists, lineList);
            }
        }

        if (!TextUtils.isEmpty(temp)) {
            lineList.add(temp);
            addLines(lineLists, lineList);
        }

        mLineCount += lineLists.size();
        return lineLists;
    }

    /**
     * 添加一行到单词内容
     *
     * @param lineLists 总单词集合
     * @param lineList 当前要添加的集合
     */
    private void addLines(List<List<String>> lineLists, List<String> lineList) {
        if (lineLists == null || lineList == null) {
            return;
        }
        List<String> tempLines = new ArrayList<>(lineList);
        lineLists.add(tempLines);
        lineList.clear();
    }

    /**
     * 获取段落
     */
    private void getParagraphList() {
        CharSequence charSequence = getText();
        if (TextUtils.isEmpty(charSequence)) {
            return;
        }
        String text = charSequence.toString()
                .replaceAll("  ", "")
                .replaceAll("   ", "")
                .replaceAll("\\r", "").trim();
        mLineCount = 0;
        String[] items = text.split("\\n");
        mParagraphLineList = new ArrayList<>();
        mParagraphWordList = new ArrayList<>();
        for (String item : items) {
            if (item.length() != 0) {
                mParagraphWordList.add(getWordList(item));
            }
        }
    }

    /**
     * 截取每一段内容的每一个单词
     *
     * @param text
     * @return
     */
    private synchronized List<String> getWordList(String text) {
        if (TextUtils.isEmpty(text)) {
            return new ArrayList<>();
        }
        Log.i(TAG, "getWordList ");
        List<String> frontList = new ArrayList<>();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            String charAt = String.valueOf(text.charAt(i));
            if (!BLANK.equals(charAt)) {
                if (TextViewUtils.checkIsSymbol(charAt)) {
                    boolean isEmptyStr = str.length() == 0;
                    str.append(charAt);
                    if (!isEmptyStr) {
                        // 中英文都需要将字符串添加到这里；
                        frontList.add(str.toString());
                        str.delete(0, str.length());
                    }
                } else {
                    if (TextViewUtils.isCN(str.toString())){
                        frontList.add(str.toString());
                        str.delete(0, str.length());
                    }
                    str.append(charAt);
                }
            } else {
                if (!TextUtils.isEmpty(str.toString())) {
                    frontList.add(str.toString().replaceAll(BLANK, ""));
                    str.delete(0, str.length());
                }
            }
        }
        if (str.length() != 0) {
            frontList.add(str.toString());
            str.delete(0, str.length());
        }
        return frontList;
    }


    /**
     * 中英文排版效果
     *
     * @param canvas
     */
    private synchronized void adjust(Canvas canvas, TextPaint paint) {
        int size = mParagraphWordList.size();
        for (int j = 0; j < size; j++) {
            // 遍历每一段
            List<List<String>> lineList = mParagraphLineList.get(j);
            for (int i = 0; i < lineList.size(); i++) {
                // 遍历每一段的每一行
                List<String> lineWords = lineList.get(i);
                if (i == lineList.size() - 1) {
                    drawScaledEndText(canvas, lineWords, paint);
                } else {
                    drawScaledText(canvas, lineWords, paint);
                }
                mLineY += (getLineHeight() + lineSpacing);
            }
            mLineY += paragraphSpacing;
        }
    }

    /**
     * 绘制最后一行文字
     * @param canvas
     * @param lineWords
     * @param paint
     */
    private void drawScaledEndText(Canvas canvas, List<String> lineWords, TextPaint paint) {
        if (canvas == null || lineWords == null || paint == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (String aSplit : lineWords) {
            if (TextViewUtils.isCN(aSplit)) {
                sb.append(aSplit);
            } else {
                sb.append(aSplit).append(BLANK);
            }
        }
        /**
         * 最后一行适配布局方向
         * android:gravity=""
         * android:textAlignment=""
         * 默认不设置则为左边
         * 如果同时设置gravity和textAlignment属性，则以textAlignment的属性为准
         * 也就是说textAlignment的属性优先级大于gravity的属性
         */
        if (GRAVITY_START == textGravity) {
            canvas.drawText(sb.toString(), paddingStart, mLineY, paint);
        } else if (GRAVITY_END == textGravity) {
            float width = StaticLayout.getDesiredWidth(sb.toString(), getPaint());
            canvas.drawText(sb.toString(), measuredWidth - width - paddingStart, mLineY, paint);
        } else {
            float width = StaticLayout.getDesiredWidth(sb.toString(), getPaint());
            canvas.drawText(sb.toString(), (mViewWidth - width) / 2, mLineY, paint);
        }
    }

    /**
     * 获取布局的方向
     */
    private int getTextGravity() {
        final int layoutDirection = getLayoutDirection();
        final int absoluteGravity = Gravity.getAbsoluteGravity(getGravity(), layoutDirection);
        int lastGravity = absoluteGravity & Gravity.HORIZONTAL_GRAVITY_MASK;
        int textAlignment = getTextAlignment();
        if (TEXT_ALIGNMENT_TEXT_START == textAlignment
                || TEXT_ALIGNMENT_VIEW_START == textAlignment
                || Gravity.LEFT == lastGravity) {
            return GRAVITY_START;
        } else if (TEXT_ALIGNMENT_TEXT_END == textAlignment
                || TEXT_ALIGNMENT_VIEW_END == textAlignment
                || Gravity.RIGHT == lastGravity) {
            return GRAVITY_END;
        } else {
            return GRAVITY_CENTER;
        }
    }

    /**
     * 绘制左右对齐效果
     *
     * @param canvas
     * @param line
     * @param paint
     */
    private void drawScaledText(Canvas canvas, List<String> line, TextPaint paint) {
        if (canvas == null || line == null || paint == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (String aSplit : line) {
            sb.append(aSplit);
        }

        float lineWidth = StaticLayout.getDesiredWidth(sb, getPaint());
        float cw = 0;
        if (GRAVITY_START == textGravity) {
            cw = paddingStart;
        } else if (GRAVITY_END == textGravity){
            cw = paddingEnd;
        } else {
            cw = paddingStart;
        }
        float d = (mViewWidth - lineWidth) / (line.size() - 1);
        for (String aSplit : line) {
            canvas.drawText(aSplit, cw, mLineY, getPaint());
            cw += StaticLayout.getDesiredWidth(aSplit + "", paint) + d;
        }
    }

    /**
     * 判断是否是富文本
     */
    public boolean isSpan() {
        CharSequence charSequence = getText();
        return charSequence instanceof Spanned;
    }
}
