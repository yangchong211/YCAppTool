/*
Copyright 2017 yangchong211（github.com/yangchong211）

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.yc.videosurface;

import android.view.View;
import androidx.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/9/21
 *     desc  : 帮助类
 *     revise:
 * </pre>
 */
public final class MeasureHelper {

    @IntDef({PlayerScreenScaleType.SCREEN_SCALE_DEFAULT,PlayerScreenScaleType.SCREEN_SCALE_16_9,
            PlayerScreenScaleType.SCREEN_SCALE_4_3,PlayerScreenScaleType.SCREEN_SCALE_MATCH_PARENT,
            PlayerScreenScaleType.SCREEN_SCALE_ORIGINAL,PlayerScreenScaleType.SCREEN_SCALE_CENTER_CROP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ScreenScaleType{}

    /**
     * 播放视频缩放类型
     */
    @Retention(RetentionPolicy.SOURCE)
    public @interface PlayerScreenScaleType {
        /**
         * 默认类型
         */
        int SCREEN_SCALE_DEFAULT = 0;
        /**
         * 16：9比例类型，最为常见
         */
        int SCREEN_SCALE_16_9 = 1;
        /**
         * 4：3比例类型，也比较常见
         */
        int SCREEN_SCALE_4_3 = 2;
        /**
         * 充满整个控件视图
         */
        int SCREEN_SCALE_MATCH_PARENT = 3;
        /**
         * 原始类型，指视频的原始类型
         */
        int SCREEN_SCALE_ORIGINAL = 4;
        /**
         * 剧中裁剪类型
         */
        int SCREEN_SCALE_CENTER_CROP = 5;
    }


    private int mVideoWidth;
    private int mVideoHeight;
    private int mCurrentScreenScale;
    private int mVideoRotationDegree;

    /**
     * 设置视频旋转角度
     * @param videoRotationDegree           角度值
     */
    public void setVideoRotation(int videoRotationDegree) {
        mVideoRotationDegree = videoRotationDegree;
    }

    /**
     * 设置视频宽高
     * @param width                         宽
     * @param height                        高
     */
    public void setVideoSize(int width, int height) {
        mVideoWidth = width;
        mVideoHeight = height;
    }

    public void setScreenScale(@ScreenScaleType int screenScale) {
        mCurrentScreenScale = screenScale;
    }

    /**
     * 注意：VideoView的宽高一定要定死，否者以下算法不成立
     * 借鉴于网络
     */
    public int[] doMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mVideoRotationDegree == 90 || mVideoRotationDegree == 270) {
            // 软解码时处理旋转信息，交换宽高
            widthMeasureSpec = widthMeasureSpec + heightMeasureSpec;
            heightMeasureSpec = widthMeasureSpec - heightMeasureSpec;
            widthMeasureSpec = widthMeasureSpec - heightMeasureSpec;
        }

        int width = View.MeasureSpec.getSize(widthMeasureSpec);
        int height = View.MeasureSpec.getSize(heightMeasureSpec);

        if (mVideoHeight == 0 || mVideoWidth == 0) {
            return new int[]{width, height};
        }

        //如果设置了比例
        switch (mCurrentScreenScale) {
            //默认正常类型
            case PlayerScreenScaleType.SCREEN_SCALE_DEFAULT:
            default:
                if (mVideoWidth * height < width * mVideoHeight) {
                    width = height * mVideoWidth / mVideoHeight;
                } else if (mVideoWidth * height > width * mVideoHeight) {
                    height = width * mVideoHeight / mVideoWidth;
                }
                break;
            //原始类型，指视频的原始类型
            case PlayerScreenScaleType.SCREEN_SCALE_ORIGINAL:
                width = mVideoWidth;
                height = mVideoHeight;
                break;
            //16：9比例类型，最为常见
            case PlayerScreenScaleType.SCREEN_SCALE_16_9:
                if (height > width / 16 * 9) {
                    height = width / 16 * 9;
                } else {
                    width = height / 9 * 16;
                }
                break;
            //4：3比例类型，也比较常见
            case PlayerScreenScaleType.SCREEN_SCALE_4_3:
                if (height > width / 4 * 3) {
                    height = width / 4 * 3;
                } else {
                    width = height / 3 * 4;
                }
                break;
            //充满整个控件视图
            case PlayerScreenScaleType.SCREEN_SCALE_MATCH_PARENT:
                width = widthMeasureSpec;
                height = heightMeasureSpec;
                break;
            //剧中裁剪类型
            case PlayerScreenScaleType.SCREEN_SCALE_CENTER_CROP:
                if (mVideoWidth * height > width * mVideoHeight) {
                    width = height * mVideoWidth / mVideoHeight;
                } else {
                    height = width * mVideoHeight / mVideoWidth;
                }
                break;
        }
        return new int[]{width, height};
    }
}
