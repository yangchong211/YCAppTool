package com.ns.yc.lifehelper.ui.find.model.bean;

import java.util.List;

/**
 * Created by PC on 2017/9/1.
 * 作者：PC
 */

public class GanKNicePicBean {


    /**
     * error : false
     * results : [{"_id":"598a5478421aa90ca3bb6bfc","createdAt":"2017-08-09T08:16:56.373Z","desc":"8-9","publishedAt":"2017-08-09T13:49:27.260Z","source":"chrome","type":"福利","url":"https://ws1.sinaimg.cn/large/610dc034ly1fid5poqfznj20u011imzm.jpg","used":true,"who":"daimajia"},{"_id":"598886d9421aa90ca209c570","createdAt":"2017-08-07T23:27:21.296Z","desc":"8-8","publishedAt":"2017-08-08T11:34:20.37Z","source":"chrome","type":"福利","url":"https://ws1.sinaimg.cn/large/610dc034ly1fibksd2mbmj20u011iacx.jpg","used":true,"who":"daimajia"},{"_id":"59826564421aa90ca3bb6bda","createdAt":"2017-08-03T07:51:00.249Z","desc":"8-3","publishedAt":"2017-08-03T12:08:07.258Z","source":"chrome","type":"福利","url":"https://ws1.sinaimg.cn/large/610dc034gy1fi678xgq1ij20pa0vlgo4.jpg","used":true,"who":"代码家"},{"_id":"59810747421aa90ca3bb6bcc","createdAt":"2017-08-02T06:57:11.207Z","desc":"8-2","publishedAt":"2017-08-02T12:21:45.220Z","source":"chrome","type":"福利","url":"https://ws1.sinaimg.cn/large/610dc034gy1fi502l3eqjj20u00hz41j.jpg","used":true,"who":"代码家"},{"_id":"597e622f421aa97de5c7c9ea","createdAt":"2017-07-31T06:48:15.386Z","desc":"7-31","publishedAt":"2017-08-01T11:48:20.466Z","source":"chrome","type":"福利","url":"https://ws1.sinaimg.cn/large/610dc034gy1fi2okd7dtjj20u011h40b.jpg","used":true,"who":"daimajia"},{"_id":"5979848e421aa90ca209c4f7","createdAt":"2017-07-27T14:13:34.914Z","desc":"7-27","publishedAt":"2017-07-27T14:16:33.773Z","source":"chrome","type":"福利","url":"http://ww1.sinaimg.cn/large/610dc034ly1fhyeyv5qwkj20u00u0q56.jpg","used":true,"who":"代码家"},{"_id":"597858e3421aa97de5c7c9b5","createdAt":"2017-07-26T16:54:59.321Z","desc":"7-26","publishedAt":"2017-07-26T16:57:39.343Z","source":"chrome","type":"福利","url":"http://ww1.sinaimg.cn/large/610dc034ly1fhxe0hfzr0j20u011in1q.jpg","used":true,"who":"daimajia"},{"_id":"59761946421aa90ca209c4d5","createdAt":"2017-07-24T23:59:02.992Z","desc":"7-25","publishedAt":"2017-07-25T15:25:42.391Z","source":"chrome","type":"福利","url":"https://ws1.sinaimg.cn/large/610dc034gy1fhvf13o2eoj20u011hjx6.jpg","used":true,"who":"daimajia"},{"_id":"59754e41421aa97de5c7c99d","createdAt":"2017-07-24T09:32:49.583Z","desc":"7-24","publishedAt":"2017-07-24T12:13:11.280Z","source":"chrome","type":"福利","url":"https://ws1.sinaimg.cn/large/610dc034gy1fhupzs0awwj20u00u0tcf.jpg","used":true,"who":"daimajia"},{"_id":"5971760e421aa90ca209c4af","createdAt":"2017-07-21T11:33:34.104Z","desc":"7-21","publishedAt":"2017-07-21T12:39:43.370Z","source":"chrome","type":"福利","url":"http://ww1.sinaimg.cn/large/610dc034ly1fhrcmgo6p0j20u00u00uu.jpg","used":true,"who":"daimajia"}]
     */

    private boolean error;
    /**
     * _id : 598a5478421aa90ca3bb6bfc
     * createdAt : 2017-08-09T08:16:56.373Z
     * desc : 8-9
     * publishedAt : 2017-08-09T13:49:27.260Z
     * source : chrome
     * type : 福利
     * url : https://ws1.sinaimg.cn/large/610dc034ly1fid5poqfznj20u011imzm.jpg
     * used : true
     * who : daimajia
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
    }
}
