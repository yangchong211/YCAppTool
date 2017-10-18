package com.ns.yc.lifehelper.ui.other.mobilePlayer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.ui.other.mobilePlayer.bean.Lyric;
import com.ns.yc.lifehelper.ui.other.mobilePlayer.utils.LyricLoader;

import java.util.ArrayList;


/**
 * 歌词View
 * 面向对象设计LyricView
 * Created by dzl on 2016/10/6.
 */

public class LyricView extends View {

    /** 音乐当前的播放位置 */
    private int currentPosition;
    /** 高亮行索引 */
    private int highlightIndex;
    /** 歌词数据 */
    private ArrayList<Lyric> lyrics;
    /** 画笔，它可以决定画的内容的线粗细、颜色等等的属性 */
    private final Paint paint;
    /** 高亮歌词颜色 */
    private int highlightColor = Color.GREEN;
    /** 默认歌词颜色 */
    private int defaultColor = Color.WHITE;
    /** 高亮歌词大小 */
    private float highlightSize;
    /** 默认歌词大小 */
    private float defaultSize;
    /** 高亮行的y坐标 */
    private int highlightY;
    /** 行高 */
    private final int rowHeight;

    public LyricView(Context context, AttributeSet attrs) {
        super(context, attrs);
        highlightSize = getResources().getDimension(R.dimen.highlightSize);
        defaultSize = getResources().getDimension(R.dimen.defaultSize);


        paint = new Paint();
        paint.setColor(defaultColor);   // 指定画笔颜色
        paint.setTextSize(defaultSize); // 指定画文本的大小
        paint.setAntiAlias(true);       // 抗锯齿（让文本的边缘比较平滑）

        rowHeight = getTextHeight("哈哈") + 10;
    }

    /**
     * 设置音乐路径，加载音乐下面对应的歌词文件，并且解析
     * @param musicPath 音乐路径
     */
    public void setMusicPath(String musicPath) {
        // 模拟歌词数据
        highlightIndex = 0;
        /*lyrics = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            lyrics.add(new Lyric("我是歌词" + i, 2000 * i));
        }*/

        lyrics = LyricLoader.loadLyric(musicPath);

        invalidate();   // 刷新View
    }

    /**
     * 设置音乐当前正在播放的位置
     * @param currentPosition 正在播放的位置
     */
    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;

        if (lyrics == null || lyrics.isEmpty()) {
            return;
        }

        // 查找高亮行
        for(int i = 0; i < lyrics.size(); i++) {
            Lyric lyric = lyrics.get(i);
            if (currentPosition >= lyric.startShowTime) {
                if(i == lyrics.size() - 1) {
                    highlightIndex = i;
                    break;
                } else if(currentPosition < lyrics.get(i + 1).startShowTime) {
                    highlightIndex = i;
                    break;
                }
            }
        }

        // 重新调用onDraw
        invalidate();
    }

    /**
     *
     * @param canvas 画布，可以在这上面画东西
     */
    @Override
    protected void onDraw(Canvas canvas) {
        if (lyrics == null || lyrics.isEmpty()) {
            drawCenterText(canvas, "正在加载歌词。。。");
            return;
        }

        Lyric heighlightLyric = lyrics.get(highlightIndex); // 取出高亮行歌词

        // 如果没有显示到最后一行，则需要平滑的往上滑
        if (highlightIndex != lyrics.size() - 1) {

            // 高亮行已显示的时间 = 当前播放时间 - 高亮行开始显示时间
            int showedTime = currentPosition - heighlightLyric.startShowTime;

            // 总显示时间 = 高亮行的下一行的开始显示时间 - 高亮行开始显示时间
            int totalShowTime = lyrics.get(highlightIndex + 1).startShowTime - heighlightLyric.startShowTime;

            // 比例（高亮行已显示的时间和高亮行总显示时间的比例）
            float scale = (float) showedTime / totalShowTime;

            // translationY = 行高 * 比例（高亮行已显示的时间和高亮行总显示时间的比例）
            float translationY = rowHeight * scale;
            canvas.translate(0, -translationY); // 因为要往上滑，所以给一个负数
        }



        String text = heighlightLyric.text;

        // 画高亮行
        drawCenterText(canvas, text);

        // 画高亮行上面的歌词
        for(int i = 0; i < highlightIndex; i++) {
            // y = 高亮行y - 行差距（亮行行索引 - i) * 行高;
            int y = highlightY - (highlightIndex - i) * rowHeight;
            drawHorizontal(canvas, lyrics.get(i).text, y, false);
        }

        // 画高亮行下面的歌词
        for(int i = highlightIndex + 1; i < lyrics.size(); i++) {
            // y = 高亮行y + 行差距（i - 亮行行索引) * 行高;
            int y = highlightY + (i - highlightIndex) * rowHeight;
            drawHorizontal(canvas, lyrics.get(i).text, y, false);
        }
    }

    /** 画水平和垂直都居中的文本 */
    private void drawCenterText(Canvas canvas, String text) {
        int textHeight = getTextHeight(text);

        // y = 歌词View高/2 + 歌词文本高/2
        highlightY = getHeight() / 2 + textHeight / 2;
        drawHorizontal(canvas, text, highlightY, true);
    }

    /** 画水平居中的文本 */
    private void drawHorizontal(Canvas canvas, String text, int y, boolean drawHighlight) {
        paint.setTextSize(drawHighlight ? highlightSize : defaultSize);
        paint.setColor(drawHighlight ? highlightColor : defaultColor);

        int textWidth = getTextWidth(text);

        // 画高亮行
        // x = 歌词View宽/2 - 歌词文本宽/2
        int x = getWidth() / 2 - textWidth / 2;

        canvas.drawText(text, x, y, paint); // 画文本
    }

    /** 获取文本的高 */
    private int getTextHeight(String text) {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.height();
    }

    /** 获取文本的宽 */
    private int getTextWidth(String text) {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.width();
    }
}
