package com.ns.yc.lifehelper.ui.other.bookReader.bean;

import java.util.List;

/**
 * Created by PC on 2017/9/22.
 * 作者：PC
 */

public class ReaderDetailRecommend {


    /**
     * booklists : [{"id":"5992af5e3b08cff279345181","title":"各种类型，书荒可看！求收藏","author":"┏ (^ω^)=☞新手","desc":"0-0","bookCount":71,"collectorCount":4,"cover":"/agent/http%3A%2F%2Fimg.1391.com%2Fapi%2Fv1%2Fbookcenter%2Fcover%2F1%2F2078199%2F2078199_c1d1000b23a041a19f02138df8b225df.jpg%2F"},{"id":"57ee3232d29652e33a82758d","title":"各种各样小说","author":"夜★难眠♪♫","desc":"各种各样书单","bookCount":434,"collectorCount":55,"cover":"/agent/http%3A%2F%2Fimg.1391.com%2Fapi%2Fv1%2Fbookcenter%2Fcover%2F1%2F874912%2F_874912_545372.jpg%2F"},{"id":"576bc3958fc973fe6ed65ef8","title":"修道2.","author":"把握现在","desc":"这是老书虫的推荐！","bookCount":405,"collectorCount":839,"cover":"/agent/http%3A%2F%2Fimg.1391.com%2Fapi%2Fv1%2Fbookcenter%2Fcover%2F1%2F1189131%2F_1189131_067422.jpg%2F"}]
     * ok : true
     */

    private boolean ok;
    /**
     * id : 5992af5e3b08cff279345181
     * title : 各种类型，书荒可看！求收藏
     * author : ┏ (^ω^)=☞新手
     * desc : 0-0
     * bookCount : 71
     * collectorCount : 4
     * cover : /agent/http%3A%2F%2Fimg.1391.com%2Fapi%2Fv1%2Fbookcenter%2Fcover%2F1%2F2078199%2F2078199_c1d1000b23a041a19f02138df8b225df.jpg%2F
     */

    private List<BooklistsBean> booklists;

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public List<BooklistsBean> getBooklists() {
        return booklists;
    }

    public void setBooklists(List<BooklistsBean> booklists) {
        this.booklists = booklists;
    }

    public static class BooklistsBean {
        private String id;
        private String title;
        private String author;
        private String desc;
        private int bookCount;
        private int collectorCount;
        private String cover;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getBookCount() {
            return bookCount;
        }

        public void setBookCount(int bookCount) {
            this.bookCount = bookCount;
        }

        public int getCollectorCount() {
            return collectorCount;
        }

        public void setCollectorCount(int collectorCount) {
            this.collectorCount = collectorCount;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }
    }
}
