package com.ycbjie.video.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.view.View;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.LogUtils;
import com.ycbjie.library.base.mvp.BaseFragment;
import com.ycbjie.library.http.ExceptionUtils;
import com.ycbjie.video.R;
import com.ycbjie.video.api.VideoModel;
import com.ycbjie.video.model.VideoContentBean;

import org.yczbj.ycvideoplayerlib.constant.ConstantKeys;
import org.yczbj.ycvideoplayerlib.controller.VideoPlayerController;
import org.yczbj.ycvideoplayerlib.inter.listener.OnCompletedListener;
import org.yczbj.ycvideoplayerlib.manager.VideoPlayerManager;
import org.yczbj.ycvideoplayerlib.player.VideoPlayer;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/12/11
 *     desc  : 视频播放页面
 *     revise:
 * </pre>
 */
public class VideoPlayerFragment extends BaseFragment {

    private FragmentActivity activity;
    private VideoPlayer videoPlayer;
    private LinearLayout loading;
    private String url;
    private VideoPlayerController controller;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (FragmentActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        VideoPlayerManager.instance().releaseVideoPlayer();
    }

    public static VideoPlayerFragment showFragment(String url){
        VideoPlayerFragment fragment = new VideoPlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url",url);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            Bundle arguments = getArguments();
            url = arguments.getString("url");
        }
    }

    @Override
    public int getContentView() {
        return R.layout.base_video_player;
    }

    @Override
    public void initView(View view) {
        videoPlayer = view.findViewById(R.id.video_player);
        loading = view.findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        VideoModel model = VideoModel.getInstance();
        getVideoData(model,url);
    }


    @SuppressLint("CheckResult")
    private void getVideoData(VideoModel model, String url) {
        model.getVideoContent(url)
                .subscribeOn(Schedulers.io())
                .map(new Function<VideoContentBean, String>() {
                    @Override
                    public String apply(@io.reactivex.annotations.NonNull VideoContentBean videoContentBean) {
                        VideoContentBean.DataBean.VideoListBean videoList = videoContentBean.getData().getVideo_list();
                        if (videoList.getVideo_3() != null) {
                            String base64 = videoList.getVideo_3().getMain_url();
                            String url = (new String(Base64.decode(base64.getBytes(), Base64.DEFAULT)));
                            LogUtils.d("getVideoUrls: " + url);
                            return url;
                        }

                        if (videoList.getVideo_2() != null) {
                            String base64 = videoList.getVideo_2().getMain_url();
                            String url = (new String(Base64.decode(base64.getBytes(), Base64.DEFAULT)));
                            LogUtils.d("getVideoUrls: " + url);
                            return url;
                        }

                        if (videoList.getVideo_1() != null) {
                            String base64 = videoList.getVideo_1().getMain_url();
                            String url = (new String(Base64.decode(base64.getBytes(), Base64.DEFAULT)));
                            LogUtils.d( "getVideoUrls: " + url);
                            return url;
                        }
                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull String s) {
                        initVideo(s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable e) {
                        ExceptionUtils.handleException(e);
                    }
                });
    }



    private void initVideo(String videoUrl) {
        if (videoUrl==null){
            return;
        }
        videoPlayer.setPlayerType(ConstantKeys.IjkPlayerType.TYPE_IJK);
        //网络视频地址
        //设置视频地址和请求头部
        videoPlayer.setUp(videoUrl, null);
        //是否从上一次的位置继续播放
        videoPlayer.continueFromLastPosition(false);
        //设置播放速度，默认是1.0f
        videoPlayer.setSpeed(1.0f);
        //设置音量
        videoPlayer.setVolume(50);
        //创建视频控制器
        if (controller==null){
            controller = new VideoPlayerController(activity);
            //设置视频标题
            //controller.setTitle("高仿优酷视频播放页面");
            //设置不操作后，5秒自动隐藏头部和底部布局
            controller.setHideTime(5000);
            //设置背景图片
            controller.imageView().setBackgroundResource(R.color.black);
            //监听播放与暂停
            //监听视频播放完成逻辑
            controller.setOnCompletedListener(new OnCompletedListener() {
                @Override
                public void onCompleted() {
                    LogUtils.e("播放结束-------------------");
                    //关闭当前页面
                }
            });
            //设置视频控制器
            videoPlayer.setController(controller);
        }
        videoPlayer.start();
        loading.setVisibility(View.GONE);
    }


}
