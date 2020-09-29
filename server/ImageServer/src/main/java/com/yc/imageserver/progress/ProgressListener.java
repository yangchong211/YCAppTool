package com.yc.imageserver.progress;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/10/24
 *     desc  : glide加载进度工具
 *     revise: 自定义加载进度监听listener
 *
 * </pre>
 */
public interface ProgressListener {
    /**
     * 进度
     * @param progress              进度之
     */
    void onProgress(int progress);
}
