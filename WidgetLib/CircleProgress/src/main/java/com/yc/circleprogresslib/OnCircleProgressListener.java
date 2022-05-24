package com.yc.circleprogresslib;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/2/10
 *     desc  : CircleProgressbar进度监听
 *     revise: 参考案例：夏安明博客http://blog.csdn.net/xiaanming/article/details/10298163
 *             案例地址：https://github.com/yangchong211
 * </pre>
 */
public interface OnCircleProgressListener {

    /**
     * 进度通知。
     *
     * @param progress 进度值。
     */
    void onProgress(int what, int progress);

}
