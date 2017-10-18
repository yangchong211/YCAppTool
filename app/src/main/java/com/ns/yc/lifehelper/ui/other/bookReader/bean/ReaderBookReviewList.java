package com.ns.yc.lifehelper.ui.other.bookReader.bean;

import java.util.List;

/**
 * Created by PC on 2017/10/18.
 * 作者：PC
 */

public class ReaderBookReviewList {


    /**
     * reviews
     * ok : true
     */

    private boolean ok;
    /**
     * _id : 59e60daf815a709c45f4ba98
     * book
     * helpful : {"total":2,"yes":2,"no":0}
     * likeCount : 1
     * haveImage : false
     * state : normal
     * updated : 2017-10-18T01:43:03.291Z
     * created : 2017-10-17T14:03:27.737Z
     * title : 【读书会】中国经典悬疑小说精华
     */

    private List<ReviewsBean> reviews;

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public List<ReviewsBean> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewsBean> reviews) {
        this.reviews = reviews;
    }

    public static class ReviewsBean {
        private String _id;
        /**
         * _id : 53f0af1999650fc374f2ad7d
         * title : 蝴蝶公墓
         * cover : /agent/http%3A%2F%2Fimg.1391.com%2Fapi%2Fv1%2Fbookcenter%2Fcover%2F1%2F858107%2F_858107_718040.jpg%2F
         * site : zhuishuvip
         * type : khly
         * latelyFollower : null
         * retentionRatio : null
         */

        private BookBean book;
        /**
         * total : 2
         * yes : 2
         * no : 0
         */

        private HelpfulBean helpful;
        private int likeCount;
        private boolean haveImage;
        private String state;
        private String updated;
        private String created;
        private String title;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public BookBean getBook() {
            return book;
        }

        public void setBook(BookBean book) {
            this.book = book;
        }

        public HelpfulBean getHelpful() {
            return helpful;
        }

        public void setHelpful(HelpfulBean helpful) {
            this.helpful = helpful;
        }

        public int getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(int likeCount) {
            this.likeCount = likeCount;
        }

        public boolean isHaveImage() {
            return haveImage;
        }

        public void setHaveImage(boolean haveImage) {
            this.haveImage = haveImage;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getUpdated() {
            return updated;
        }

        public void setUpdated(String updated) {
            this.updated = updated;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public static class BookBean {
            private String _id;
            private String title;
            private String cover;
            private String site;
            private String type;
            private Object latelyFollower;
            private Object retentionRatio;

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

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public Object getLatelyFollower() {
                return latelyFollower;
            }

            public void setLatelyFollower(Object latelyFollower) {
                this.latelyFollower = latelyFollower;
            }

            public Object getRetentionRatio() {
                return retentionRatio;
            }

            public void setRetentionRatio(Object retentionRatio) {
                this.retentionRatio = retentionRatio;
            }
        }

        public static class HelpfulBean {
            private int total;
            private int yes;
            private int no;

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public int getYes() {
                return yes;
            }

            public void setYes(int yes) {
                this.yes = yes;
            }

            public int getNo() {
                return no;
            }

            public void setNo(int no) {
                this.no = no;
            }
        }
    }
}
