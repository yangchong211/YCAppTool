package com.ns.yc.lifehelper.ui.other.bookReader.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by PC on 2017/9/19.
 * 作者：PC
 */

public class SubHomeTopBean {


    /**
     * _id : 582ed5fc93b7e855163e707d
     * updated : 2017-09-18T21:20:11.864Z
     * title : 圣诞热搜榜
     * tag : manualRank
     * cover : /ranking-cover/147946444450686
     * icon : /cover/148945807649134
     * __v : 434
     * shortTitle : 圣诞榜
     * created : 2017-09-19T08:25:00.465Z
     * isSub : false
     * collapse : true
     * new : true
     * gender : male
     * priority : 1020
     * id : 582ed5fc93b7e855163e707d
     * total : 100
     */

    private RankingBean ranking;
    /**
     * ok : true
     */

    private boolean ok;

    public RankingBean getRanking() {
        return ranking;
    }

    public void setRanking(RankingBean ranking) {
        this.ranking = ranking;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public static class RankingBean {
        private String _id;
        private String updated;
        private String title;
        private String tag;
        private String cover;
        private String icon;
        private int __v;
        private String shortTitle;
        private String created;
        private boolean isSub;
        private boolean collapse;
        @SerializedName("new")
        private boolean newX;
        private String gender;
        private int priority;
        private String id;
        private int total;
        /**
         * _id : 58db11eef02f7d7e2ab97775
         * title : 黑道学生之校园狂少
         * author : 七十二翼天使
         * shortIntro : 绝不能让美女们在黑夜里寂寞地哭泣！
         时隔十二年，再开黑道学生，原汁原味，更热血，更搞笑，更暧昧！
         唯一官方群号：老剑书友群（1）： 216353060
         唯一新浪微博：煮剑焚酒本尊
         * cover : /agent/http%3A%2F%2Fimg.1391.com%2Fapi%2Fv1%2Fbookcenter%2Fcover%2F1%2F1469620%2F1469620_8daf9d0e2e4e4957aa75981772d29680.jpg%2F
         * site : zhuishuvip
         * banned : 0
         * latelyFollower : 14078
         * retentionRatio : 58.28
         */

        private List<BooksBean> books;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getUpdated() {
            return updated;
        }

        public void setUpdated(String updated) {
            this.updated = updated;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public int get__v() {
            return __v;
        }

        public void set__v(int __v) {
            this.__v = __v;
        }

        public String getShortTitle() {
            return shortTitle;
        }

        public void setShortTitle(String shortTitle) {
            this.shortTitle = shortTitle;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public boolean isIsSub() {
            return isSub;
        }

        public void setIsSub(boolean isSub) {
            this.isSub = isSub;
        }

        public boolean isCollapse() {
            return collapse;
        }

        public void setCollapse(boolean collapse) {
            this.collapse = collapse;
        }

        public boolean isNewX() {
            return newX;
        }

        public void setNewX(boolean newX) {
            this.newX = newX;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<BooksBean> getBooks() {
            return books;
        }

        public void setBooks(List<BooksBean> books) {
            this.books = books;
        }

        public static class BooksBean {
            private String _id;
            private String title;
            private String author;
            private String shortIntro;
            private String cover;
            private String site;
            public String cat;
            private int banned;
            private int latelyFollower;
            private String retentionRatio;

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
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

            public String getShortIntro() {
                return shortIntro;
            }

            public void setShortIntro(String shortIntro) {
                this.shortIntro = shortIntro;
            }

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public String getSite() {
                return site;
            }

            public void setSite(String site) {
                this.site = site;
            }

            public int getBanned() {
                return banned;
            }

            public void setBanned(int banned) {
                this.banned = banned;
            }

            public int getLatelyFollower() {
                return latelyFollower;
            }

            public void setLatelyFollower(int latelyFollower) {
                this.latelyFollower = latelyFollower;
            }

            public String getRetentionRatio() {
                return retentionRatio;
            }

            public void setRetentionRatio(String retentionRatio) {
                this.retentionRatio = retentionRatio;
            }

            public String getCat() {
                return cat;
            }

            public void setCat(String cat) {
                this.cat = cat;
            }
        }
    }
}
