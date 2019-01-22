package com.ycbjie.gank.bean.bean;

import java.util.List;

/**
 * Created by PC on 2017/9/25.
 * 作者：PC
 */

public class GanKEveryDay {


    /**
     * category : ["iOS","拓展资源","Android","休息视频","福利"]
     * error : false
     * results : {"Android":[{"_id":"56cc6d23421aa95caa707bba","createdAt":"2015-08-06T02:05:32.826Z","desc":"一个查看设备规格的库，并且可以计算哪一年被定为\u201c高端\u201d机","publishedAt":"2015-08-06T04:16:55.575Z","type":"Android","url":"https://github.com/facebook/device-year-class","used":true,"who":"有时放纵"},{"_id":"56cc6d23421aa95caa707c67","createdAt":"2015-08-05T16:36:19.591Z","desc":"一个好玩的自定义字体库","publishedAt":"2015-08-06T04:16:55.805Z","type":"Android","url":"https://github.com/vsvankhede/easyfonts","used":true,"who":"有时放纵"},{"_id":"56cc6d23421aa95caa707c6a","createdAt":"2015-08-06T02:07:23.290Z","desc":"一个小清新spinner","publishedAt":"2015-08-06T04:16:55.582Z","type":"Android","url":"https://github.com/arcadefire/nice-spinner","used":true,"who":"有时放纵"}],"iOS":[{"_id":"56cc6d1d421aa95caa70777e","createdAt":"2015-08-06T01:55:36.30Z","desc":"iOS 核心动画高级技巧","publishedAt":"2015-08-06T04:16:55.592Z","type":"iOS","url":"http://zsisme.gitbooks.io/ios-/content/","used":true,"who":"Andrew Liu"},{"_id":"56cc6d23421aa95caa707a65","createdAt":"2015-08-05T03:45:12.537Z","desc":"设定label，text等视图的属性，你喜欢这种方式么？","publishedAt":"2015-08-06T04:16:55.584Z","type":"iOS","url":"http://natashatherobot.com/ios-a-beautiful-way-of-styling-iboutlets-in-swift/?utm_campaign=This%2BWeek%2Bin%2BSwift&utm_medium=web&utm_source=This_Week_in_Swift_47","used":true,"who":"Huan"},{"_id":"56cc6d23421aa95caa707c62","createdAt":"2015-08-06T01:56:32.207Z","desc":"iOS开发之block终极篇","publishedAt":"2015-08-06T04:16:55.589Z","type":"iOS","url":"http://www.90159.com/2015/08/05/ios-block-ultimate/","used":true,"who":"Andrew Liu"},{"_id":"56cc6d23421aa95caa707c70","createdAt":"2015-08-05T12:17:08.481Z","desc":"Status Board 2 - Panic 公司推出的 iOS App，可以生成可视化数据图表，用以监测项目效率、Bug量，团队动态、销售情况等多种数据（稀土圈）","publishedAt":"2015-08-06T04:16:55.601Z","type":"iOS","url":"https://www.panic.com/statusboard/","used":true,"who":"LHF"}],"休息视频":[{"_id":"56cc6d23421aa95caa707c2b","createdAt":"2015-08-06T03:55:07.175Z","desc":"重温字幕版倒鸭子~~~","publishedAt":"2015-08-06T04:16:55.578Z","type":"休息视频","url":"http://video.weibo.com/show?fid=1034:0c79a69b1bafe17df62e750391d92118","used":true,"who":"代码家"}],"拓展资源":[{"_id":"56cc6d1d421aa95caa707781","createdAt":"2015-08-06T00:53:43.851Z","desc":"node express 源码接卸","publishedAt":"2015-08-06T04:16:55.601Z","type":"拓展资源","url":"https://gist.github.com/dlutwuwei/3faf88d535ac81c4e263","used":true,"who":"YJX"},{"_id":"56cc6d23421aa95caa707a66","createdAt":"2015-08-06T00:49:19.237Z","desc":"MongoDB从入门到精通系列","publishedAt":"2015-08-06T04:16:55.579Z","type":"拓展资源","url":"http://codefrom.com/t/mongodb%E4%BB%8E%E5%85%A5%E9%97%A8%E5%88%B0%E7%B2%BE%E9%80%9A%E7%B3%BB%E5%88%97%E4%B8%93%E9%A2%98","used":true,"who":"YJX"}],"福利":[{"_id":"56cc6d23421aa95caa707c6f","createdAt":"2015-08-06T01:33:55.463Z","desc":"8.6","publishedAt":"2015-08-06T04:16:55.601Z","type":"福利","url":"http://ww4.sinaimg.cn/large/7a8aed7bgw1eusn3niy2bj20hs0qo0wb.jpg","used":true,"who":"张涵宇"}]}
     */

    private boolean error;
    private ResultsBean results;
    private List<String> category;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public ResultsBean getResults() {
        return results;
    }

    public void setResults(ResultsBean results) {
        this.results = results;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public static class ResultsBean {
        /**
         * _id : 56cc6d23421aa95caa707bba
         * createdAt : 2015-08-06T02:05:32.826Z
         * desc : 一个查看设备规格的库，并且可以计算哪一年被定为“高端”机
         * publishedAt : 2015-08-06T04:16:55.575Z
         * type : Android
         * url : https://github.com/facebook/device-year-class
         * used : true
         * who : 有时放纵
         */

        private List<AndroidBean> Android;
        /**
         * _id : 56cc6d1d421aa95caa70777e
         * createdAt : 2015-08-06T01:55:36.30Z
         * desc : iOS 核心动画高级技巧
         * publishedAt : 2015-08-06T04:16:55.592Z
         * type : iOS
         * url : http://zsisme.gitbooks.io/ios-/content/
         * used : true
         * who : Andrew Liu
         */

        private List<AndroidBean> iOS;
        /**
         * _id : 56cc6d23421aa95caa707c2b
         * createdAt : 2015-08-06T03:55:07.175Z
         * desc : 重温字幕版倒鸭子~~~
         * publishedAt : 2015-08-06T04:16:55.578Z
         * type : 休息视频
         * url : http://video.weibo.com/show?fid=1034:0c79a69b1bafe17df62e750391d92118
         * used : true
         * who : 代码家
         */
        private List<AndroidBean> front;
        private List<AndroidBean> app;
        private List<AndroidBean> recommend;
        private List<AndroidBean> restMovie;
        /**
         * _id : 56cc6d1d421aa95caa707781
         * createdAt : 2015-08-06T00:53:43.851Z
         * desc : node express 源码接卸
         * publishedAt : 2015-08-06T04:16:55.601Z
         * type : 拓展资源
         * url : https://gist.github.com/dlutwuwei/3faf88d535ac81c4e263
         * used : true
         * who : YJX
         */

        private List<AndroidBean> resource;
        /**
         * _id : 56cc6d23421aa95caa707c6f
         * createdAt : 2015-08-06T01:33:55.463Z
         * desc : 8.6
         * publishedAt : 2015-08-06T04:16:55.601Z
         * type : 福利
         * url : http://ww4.sinaimg.cn/large/7a8aed7bgw1eusn3niy2bj20hs0qo0wb.jpg
         * used : true
         * who : 张涵宇
         */

        private List<AndroidBean> welfare;

        public List<AndroidBean> getAndroid() {
            return Android;
        }

        public void setAndroid(List<AndroidBean> android) {
            Android = android;
        }

        public List<AndroidBean> getiOS() {
            return iOS;
        }

        public void setiOS(List<AndroidBean> iOS) {
            this.iOS = iOS;
        }

        public List<AndroidBean> getRestMovie() {
            return restMovie;
        }

        public void setRestMovie(List<AndroidBean> restMovie) {
            this.restMovie = restMovie;
        }

        public List<AndroidBean> getResource() {
            return resource;
        }

        public void setResource(List<AndroidBean> resource) {
            this.resource = resource;
        }

        public List<AndroidBean> getWelfare() {
            return welfare;
        }

        public void setWelfare(List<AndroidBean> welfare) {
            this.welfare = welfare;
        }

        public List<AndroidBean> getFront() {
            return front;
        }

        public void setFront(List<AndroidBean> front) {
            this.front = front;
        }

        public List<AndroidBean> getApp() {
            return app;
        }

        public void setApp(List<AndroidBean> app) {
            this.app = app;
        }

        public List<AndroidBean> getRecommend() {
            return recommend;
        }

        public void setRecommend(List<AndroidBean> recommend) {
            this.recommend = recommend;
        }

        public static class AndroidBean {

            /**
             * _id : 59957ec7421aa9672f354ddd
             * createdAt : 2017-08-17T19:32:23.785Z
             * desc : 让你明明白白的使用RecyclerView——SnapHelper详解
             * images : ["http://img.gank.io/55147d8b-513d-4bbc-ad67-ba20a3ec297c"]
             * publishedAt : 2017-08-23T12:12:15.166Z
             * source : web
             * type : Android
             * url : http://www.jianshu.com/p/e54db232df62
             * used : true
             * who : null
             */

            private String _id;
            private String createdAt;
            private String desc;
            private String publishedAt;
            private String source;
            private String type;
            private String url;
            private boolean used;
            private String who;
            private List<String> images;

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getPublishedAt() {
                return publishedAt;
            }

            public void setPublishedAt(String publishedAt) {
                this.publishedAt = publishedAt;
            }

            public String getSource() {
                return source;
            }

            public void setSource(String source) {
                this.source = source;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public boolean isUsed() {
                return used;
            }

            public void setUsed(boolean used) {
                this.used = used;
            }

            public String getWho() {
                return who;
            }

            public void setWho(String who) {
                this.who = who;
            }

            public List<String> getImages() {
                return images;
            }

            public void setImages(List<String> images) {
                this.images = images;
            }
        }
    }
}
