package com.ycbjie.video.model;

import java.util.List;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/8/22
 *     desc  :
 *     revise:
 * </pre>
 */
public class MultiNewsArticleBean {

    /**
     * login_status : 0
     * total_number : 15
     * has_more : true
     * post_content_hint : 分享今日新鲜事
     * show_et_status : 0
     * feed_flag : 0
     * action_to_last_stick : 1
     * message : success
     * has_more_to_refresh : true
     * tips : {"display_info":"今日头条推荐引擎有15条更新","open_url":"","web_url":"","app_name":"今日头条","package_name":"","display_template":"今日头条推荐引擎有%s条更新","type":"app","display_duration":2,"download_url":""}
     */

    private int login_status;
    private int total_number;
    private boolean has_more;
    private String post_content_hint;
    private int show_et_status;
    private int feed_flag;
    private int action_to_last_stick;
    private String message;
    private boolean has_more_to_refresh;
    private TipsBean tips;
    private List<DataBean> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class TipsBean {
        /**
         * display_info : 今日头条推荐引擎有15条更新
         * open_url :
         * web_url :
         * app_name : 今日头条
         * package_name :
         * display_template : 今日头条推荐引擎有%s条更新
         * type : app
         * display_duration : 2
         * download_url :
         */

        private String display_info;
        private String open_url;
        private String web_url;
        private String app_name;
        private String package_name;
        private String display_template;
        private String type;
        private int display_duration;
        private String download_url;

        public String getDisplay_info() {
            return display_info;
        }

        public void setDisplay_info(String display_info) {
            this.display_info = display_info;
        }

        public String getOpen_url() {
            return open_url;
        }

        public void setOpen_url(String open_url) {
            this.open_url = open_url;
        }

        public String getWeb_url() {
            return web_url;
        }

        public void setWeb_url(String web_url) {
            this.web_url = web_url;
        }

        public String getApp_name() {
            return app_name;
        }

        public void setApp_name(String app_name) {
            this.app_name = app_name;
        }

        public String getPackage_name() {
            return package_name;
        }

        public void setPackage_name(String package_name) {
            this.package_name = package_name;
        }

        public String getDisplay_template() {
            return display_template;
        }

        public void setDisplay_template(String display_template) {
            this.display_template = display_template;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getDisplay_duration() {
            return display_duration;
        }

        public void setDisplay_duration(int display_duration) {
            this.display_duration = display_duration;
        }

        public String getDownload_url() {
            return download_url;
        }

        public void setDownload_url(String download_url) {
            this.download_url = download_url;
        }
    }

    public static class DataBean {
        /**
         * content : {"log_pb": {"impr_id": "201705182052000100040340182987C4"}, "read_count": 1068313, "media_name": "\u65b0\u534e\u7f51", "ban_comment": 0, "abstract": "\u65b0\u534e\u7f51\u8bb0\u8005 \u738b\u5b50\u6656\u3010\u5b66\u4e60\u8fdb\u884c\u65f6\u301115\u65e5\u95ed\u5e55\u7684\u201c\u4e00\u5e26\u4e00\u8def\u201d\u56fd\u9645\u5408\u4f5c\u9ad8\u5cf0\u8bba\u575b\u53d6\u5f97\u4e86\u4e3e\u4e16\u77a9\u76ee\u7684\u4e30\u7855\u6210\u679c\u3002\u65b0\u534e\u793e\u300a\u5b66\u4e60\u8fdb\u884c\u65f6\u300b\u539f\u521b\u54c1\u724c\u680f\u76ee\u201c\u8bb2\u4e60\u6240\u201d\u4eca\u5929\u63a8\u51fa\u6587\u7ae0\uff0c\u4ece\u201c\u627f\u8bfa\u201d\u548c\u201c\u8df5\u884c\u201d\u7684\u89d2\u5ea6\uff0c\u4e3a\u60a8\u89e3\u8bfb\u4e60\u8fd1\u5e73\u5728\u8fd9\u6b21\u8bba\u575b\u4e0a\u7684\u91cd\u8981\u6f14\u8bb2\u3002", "image_list": [], "ban_bury": 1, "has_video": false, "article_type": 0, "tag": "news_politics", "forward_info": {"forward_count": 34}, "has_m3u8_video": 0, "keywords": "\u591a\u8fb9\u4e3b\u4e49,\u4e1d\u7ef8\u4e4b\u8def,\u5171\u8d62,\u6784\u5efa\u4eba\u7c7b\u547d\u8fd0\u5171\u540c\u4f53,\u4e00\u5e26\u4e00\u8def", "rid": "201705182052000100040340182987C4", "label": "\u7f6e\u9876", "show_portrait_article": false, "user_verified": 0, "aggr_type": 1, "cell_type": 0, "article_sub_type": 0, "bury_count": 0, "title": "\u201c\u4e00\u5e26\u4e00\u8def\u201d\u2014\u2014\u4e60\u8fd1\u5e73\u4e4b\u8bfa", "ignore_web_transform": 1, "source_icon_style": 4, "tip": 0, "hot": 0, "share_url": "http://toutiao.com/group/6421262515992903938/?iid=0&app=news_article", "has_mp4_video": 0, "source": "\u65b0\u534e\u7f51", "comment_count": 14, "article_url": "http://news.xinhuanet.com/politics/2017-05/18/c_1120991501.htm", "filter_words": [], "share_count": 4806, "stick_label": "\u7f6e\u9876", "publish_time": 1495067173, "action_list": [{"action": 1, "extra": {}, "desc": ""}, {"action": 3, "extra": {}, "desc": ""}, {"action": 7, "extra": {}, "desc": ""}, {"action": 9, "extra": {}, "desc": ""}], "has_image": false, "cell_layout_style": 1, "tag_id": 6421262515992903938, "video_style": 0, "verified_content": "", "display_url": "http://toutiao.com/group/6421262515992903938/", "is_stick": true, "large_image_list": [], "item_id": 6421266422789308929, "is_subject": false, "stick_style": 1, "show_portrait": false, "repin_count": 11258, "cell_flag": 11, "user_info": {"verified_content": "", "avatar_url": "http://p3.pstatp.com/thumb/3658/7378365093", "user_id": 4377795668, "name": "\u65b0\u534e\u7f51", "follower_count": 0, "follow": false, "user_auth_info": "", "user_verified": false, "description": "\u4f20\u64ad\u4e2d\u56fd\uff0c\u62a5\u9053\u4e16\u754c\uff1b\u6743\u5a01\u58f0\u97f3\uff0c\u4eb2\u5207\u8868\u8fbe\u3002"}, "source_open_url": "sslocal://profile?uid=4377795668", "level": 0, "like_count": 3, "digg_count": 3, "behot_time": 1495111920, "cursor": 1495111920999, "url": "http://news.xinhuanet.com/politics/2017-05/18/c_1120991501.htm", "preload_web": 0, "user_repin": 0, "label_style": 1, "item_version": 0, "media_info": {"user_id": 4377795668, "verified_content": "", "avatar_url": "http://p2.pstatp.com/large/3658/7378365093", "media_id": 4377795668, "name": "\u65b0\u534e\u7f51", "recommend_type": 0, "follow": false, "recommend_reason": "", "is_star_user": false, "user_verified": false}, "group_id": 6421262515992903938, "middle_image": {}}
         * code :
         */

        private String content;
        private String code;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
