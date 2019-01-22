package com.ycbjie.zhihu.model;

import java.util.List;

/**
 * Created by PC on 2017/11/29.
 * 作者：PC
 */

public class ZhiHuHotBean {


    /**
     * news_id : 9658543
     * url : http://news-at.zhihu.com/api/2/news/9658543
     * thumbnail : https://pic1.zhimg.com/v2-24c105d3cbf220382e8a2e3952ae4fcc.jpg
     * title : 瞎扯 · 如何正确地吐槽
     */

    private List<RecentBean> recent;

    public List<RecentBean> getRecent() {
        return recent;
    }

    public void setRecent(List<RecentBean> recent) {
        this.recent = recent;
    }

    public static class RecentBean {
        private int news_id;
        private String url;
        private String thumbnail;
        private String title;
        private boolean readState;

        public int getNews_id() {
            return news_id;
        }

        public void setNews_id(int news_id) {
            this.news_id = news_id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isReadState() {
            return readState;
        }

        public void setReadState(boolean readState) {
            this.readState = readState;
        }
    }
}
