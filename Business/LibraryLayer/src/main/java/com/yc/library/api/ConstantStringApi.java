package com.yc.library.api;

import com.yc.library.bean.ListNewsData;
import com.yc.library.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ConstantStringApi {

    private static final String[] StringTitles = new String[]{
            "关于我的github网站，欢迎访问",
            "技术博客笔记大汇总【15年10月到至今】，包括Java基础及深入知识点，Android技术博客",
            "基础封装视频播放器player，可以在ExoPlayer、MediaPlayer原生MediaPlayer可以自由切换内核",
            "基于腾讯x5开源库，提高webView开发效率，大概要节约你百分之六十的时间成本。",
            "自定义支持上拉加载更多，下拉刷新，可以自定义头部和底部，可以添加多个headerView。",
            "组件之间的通信，很友好起到隔离效果，接口+实现类，使用注解生成代码方式，无需手动注册",
            "关于状态栏方案总结案例，适合于绝大多数的使用场景……",
            "轻量级线程池封装库，支持线程执行过程中状态回调监测(包含成功，失败，异常等多种状态)",
            "自定义折叠布局，自定义折叠和展开布局，在不用改变原控件的基础上，就可以实现折叠展开功能，入侵性极低。",
    };

    public static List<String> createStringTitle() {
        return Arrays.asList(StringTitles);
    }

    private static final String[] StringTimes = new String[]{
            "2017年6月至今",
            "关于我的github网站，欢迎访问",
            "关于我的github网站",
            "关于我的github网站",
            "关于我的github网站",
            "关于我的github网站",
            "关于我的github网站",
            "关于我的github网站",
            "关于我的github网站",
    };

    private static final String[] StringAuthors = new String[]{
            "杨充",
            "杨充",
            "杨充",
            "杨充",
            "杨充",
            "杨充",
            "杨充",
            "杨充",
            "杨充",
    };

    public static List<ListNewsData> getNewsList(){
        List<ListNewsData> newsDataList = new ArrayList<>();
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(0),"", ConstantImageApi.createSmallImage().get(0)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(1),"", ConstantImageApi.createSmallImage().get(1)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(2),"", ConstantImageApi.createSmallImage().get(2)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(3),"", ConstantImageApi.createSmallImage().get(3)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(4),"", ConstantImageApi.createSmallImage().get(4)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(5),"", ConstantImageApi.createSmallImage().get(5)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(0),"", ConstantImageApi.createSmallImage().get(0)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(1),"", ConstantImageApi.createSmallImage().get(1)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(2),"", ConstantImageApi.createSmallImage().get(2)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(3),"", ConstantImageApi.createSmallImage().get(3)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(4),"", ConstantImageApi.createSmallImage().get(4)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(5),"", ConstantImageApi.createSmallImage().get(5)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(0),"", ConstantImageApi.createSmallImage().get(0)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(1),"", ConstantImageApi.createSmallImage().get(1)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(2),"", ConstantImageApi.createSmallImage().get(2)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(3),"", ConstantImageApi.createSmallImage().get(3)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(4),"", ConstantImageApi.createSmallImage().get(4)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(5),"", ConstantImageApi.createSmallImage().get(5)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(0),"", ConstantImageApi.createSmallImage().get(0)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(1),"", ConstantImageApi.createSmallImage().get(1)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(2),"", ConstantImageApi.createSmallImage().get(2)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(3),"", ConstantImageApi.createSmallImage().get(3)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(4),"", ConstantImageApi.createSmallImage().get(4)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(5),"", ConstantImageApi.createSmallImage().get(5)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(0),"", ConstantImageApi.createSmallImage().get(0)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(1),"", ConstantImageApi.createSmallImage().get(1)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(2),"", ConstantImageApi.createSmallImage().get(2)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(3),"", ConstantImageApi.createSmallImage().get(3)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(4),"", ConstantImageApi.createSmallImage().get(4)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(5),"", ConstantImageApi.createSmallImage().get(5)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(0),"", ConstantImageApi.createSmallImage().get(0)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(1),"", ConstantImageApi.createSmallImage().get(1)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(2),"", ConstantImageApi.createSmallImage().get(2)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(3),"", ConstantImageApi.createSmallImage().get(3)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(4),"", ConstantImageApi.createSmallImage().get(4)));
        newsDataList.add(new ListNewsData(ConstantStringApi.createStringTitle().get(5),"", ConstantImageApi.createSmallImage().get(5)));
        return newsDataList;
    }

}
