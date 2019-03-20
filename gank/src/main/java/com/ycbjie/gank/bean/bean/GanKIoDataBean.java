package com.ycbjie.gank.bean.bean;

import java.util.List;


public class GanKIoDataBean {

    /**
     * error : false
     * results : [{"_id":"599e2d51421aa901c85e5fbd","createdAt":"2017-08-24T09:35:13.750Z","desc":"物联网来了，你还不会蓝牙开发？","publishedAt":"2017-08-24T12:43:10.124Z","source":"web","type":"Android","url":"http://url.cn/5ymK3Ps","used":true,"who":"陈宇明"},{"_id":"599e3422421aa901bcb7db93","createdAt":"2017-08-24T10:04:18.356Z","desc":"Android 实现 dialog 的 3D 翻转","publishedAt":"2017-08-24T12:43:10.124Z","source":"web","type":"Android","url":"http://mp.weixin.qq.com/s/LEOB83WC5Jn_LT-dWVPSqw","used":true,"who":null},{"_id":"599e488a421aa901c1c0a87f","createdAt":"2017-08-24T11:31:22.71Z","desc":"支持持续性滑动动画的图片组件，用来做背景效果 应该不错。","images":["http://img.gank.io/e15705f1-fc7e-4865-ba80-640e6ff388f1"],"publishedAt":"2017-08-24T12:43:10.124Z","source":"chrome","type":"Android","url":"https://github.com/Cutta/ContinuousScrollableImageView","used":true,"who":"代码家"},{"_id":"599e489e421aa901c1c0a880","createdAt":"2017-08-24T11:31:42.213Z","desc":"Android 地理位置追踪器。","images":["http://img.gank.io/db601311-13bd-4a09-a3b5-7abafc5bf2ab"],"publishedAt":"2017-08-24T12:43:10.124Z","source":"chrome","type":"Android","url":"https://github.com/frankodoom/MotionVehicleTracker","used":true,"who":"Allen"},{"_id":"59939d78421aa9672cdf0813","createdAt":"2017-08-16T09:18:48.540Z","desc":"如何造 Android Studio 插件？","publishedAt":"2017-08-23T12:12:15.166Z","source":"web","type":"Android","url":"http://url.cn/4E9wbHo","used":true,"who":"陈宇明"},{"_id":"59957ec7421aa9672f354ddd","createdAt":"2017-08-17T19:32:23.785Z","desc":"让你明明白白的使用RecyclerView\u2014\u2014SnapHelper详解","images":["http://img.gank.io/55147d8b-513d-4bbc-ad67-ba20a3ec297c"],"publishedAt":"2017-08-23T12:12:15.166Z","source":"web","type":"Android","url":"http://www.jianshu.com/p/e54db232df62","used":true,"who":null},{"_id":"5998ed57421aa96729c57267","createdAt":"2017-08-20T10:00:55.665Z","desc":"类似 Stetho 的 Layout 资源视察工具","images":["http://img.gank.io/37550fa9-01ab-43b9-98c6-8e2d3c78745a"],"publishedAt":"2017-08-22T12:02:15.769Z","source":"web","type":"Android","url":"https://github.com/nekocode/ResourceInspector#","used":true,"who":"nekocode"},{"_id":"599ae3b9421aa901c85e5faa","createdAt":"2017-08-21T21:44:25.612Z","desc":"腰果Cashew，做最有质感的gank客户端！除了网络，图片加载等必要的部分，基本不依赖其他开源库，UI使用原生，手动实现上拉加载，并使用了Databinding","images":["http://img.gank.io/f9eebf8a-9a52-4598-8b7d-38f9d98c08c9"],"publishedAt":"2017-08-22T12:02:15.769Z","source":"web","type":"Android","url":"https://github.com/wheat7/Cashew","used":true,"who":"麦田哥"},{"_id":"599b9726421aa901c85e5fad","createdAt":"2017-08-22T10:29:58.859Z","desc":"腾讯开源 H5 加速加载方案。","images":["http://img.gank.io/9c5efe90-607a-45fb-9345-b553fc4c3f7c"],"publishedAt":"2017-08-22T12:02:15.769Z","source":"chrome","type":"Android","url":"https://github.com/Tencent/VasSonic","used":true,"who":"代码家"},{"_id":"599b9918421aa901bcb7db80","createdAt":"2017-08-22T10:38:16.886Z","desc":"支持 prefix 前缀模式的 EditText。","images":["http://img.gank.io/220e48c6-ae30-436b-b3ca-690b91e37943"],"publishedAt":"2017-08-22T12:02:15.769Z","source":"chrome","type":"Android","url":"https://github.com/bachors/Android-Prefix-Input","used":true,"who":"代码家"}]
     */

    private boolean error;
    /**
     * _id : 599e2d51421aa901c85e5fbd
     * createdAt : 2017-08-24T09:35:13.750Z
     * desc : 物联网来了，你还不会蓝牙开发？
     * publishedAt : 2017-08-24T12:43:10.124Z
     * source : web
     * type : Android
     * url : http://url.cn/5ymK3Ps
     * used : true
     * who : 陈宇明
     */

    private List<ResultsBean> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean {
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
