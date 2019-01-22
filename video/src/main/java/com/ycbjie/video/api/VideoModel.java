package com.ycbjie.video.api;


import com.ycbjie.library.http.RetrofitWrapper;
import com.ycbjie.video.model.MultiNewsArticleBean;
import com.ycbjie.video.model.VideoContentBean;

import io.reactivex.Observable;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/8/22
 *     desc  : 视频接口
 *     revise:
 * </pre>
 */
public class VideoModel {

    private static VideoModel model;
    private VideoApi mApiService;
    private static final String HOST = "http://toutiao.com/";

    private VideoModel() {
        mApiService = RetrofitWrapper
                .getInstance(HOST)
                .create(VideoApi.class);
    }

    public static VideoModel getInstance(){
        if(model == null) {
            model = new VideoModel();
        }
        return model;
    }

    public Observable<MultiNewsArticleBean> getVideoArticle(String category, String maxBehotTime) {
        return mApiService.getVideoArticle(category,maxBehotTime);
    }

    public Observable<VideoContentBean> getVideoContent(String url) {
        return mApiService.getVideoContent(url);
    }

}
