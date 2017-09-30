package com.ns.yc.lifehelper.ui.other.bookReader.bean;

import java.util.List;

/**
 * Created by PC on 2017/9/22.
 * 作者：PC
 */

public class ReaderDetailReviews {


    /**
     * total : 3
     * reviews : [{"_id":"59b95d2e982f723a69139bf3","rating":4,"author":{"_id":"57fdc07037bcee8e7b3f83fb","avatar":"/avatar/80/36/8036a70c0c91e59aae027016667f1e9b","nickname":"生夏如花却未闻花名","activityAvatar":"","type":"normal","lv":8,"gender":"male"},"helpful":{"total":4,"yes":6,"no":2},"likeCount":1,"state":"normal","updated":"2017-09-22T04:55:58.078Z","created":"2017-09-13T16:30:38.097Z","commentCount":3,"content":"作者的书基本都太监烂尾了，而且有时候实力提升太快了，推不动剧情了，然后作者好像还是公职人员，更新有时候会受到工作的影响，希望作者能够尽量客服这些问题吧，他的书的风格一直是我很喜欢的","title":"别太监，烂尾就好"},{"_id":"59c3c49672b61fad7a655690","rating":3,"author":{"_id":"593e075b3e01031d5ab5de73","avatar":"/avatar/46/05/4605901be488f642493d6f761a020a7e","nickname":"孤心","activityAvatar":"","type":"normal","lv":6,"gender":"male"},"helpful":{"total":0,"yes":0,"no":0},"likeCount":0,"state":"normal","updated":"2017-09-21T13:54:30.053Z","created":"2017-09-21T13:54:30.053Z","commentCount":0,"content":"看完简介，构思不错，值得看。但听楼下那位说作者经常烂尾，还太监？一名好的作者，对待作品就像骨肉一样，绝不许这种事发生在你自己身上啊！","title":"先打个三星，没看"}]
     * ok : true
     */

    private int total;
    private boolean ok;
    /**
     * _id : 59b95d2e982f723a69139bf3
     * rating : 4
     * author : {"_id":"57fdc07037bcee8e7b3f83fb","avatar":"/avatar/80/36/8036a70c0c91e59aae027016667f1e9b","nickname":"生夏如花却未闻花名","activityAvatar":"","type":"normal","lv":8,"gender":"male"}
     * helpful : {"total":4,"yes":6,"no":2}
     * likeCount : 1
     * state : normal
     * updated : 2017-09-22T04:55:58.078Z
     * created : 2017-09-13T16:30:38.097Z
     * commentCount : 3
     * content : 作者的书基本都太监烂尾了，而且有时候实力提升太快了，推不动剧情了，然后作者好像还是公职人员，更新有时候会受到工作的影响，希望作者能够尽量客服这些问题吧，他的书的风格一直是我很喜欢的
     * title : 别太监，烂尾就好
     */

    private List<ReviewsBean> reviews;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

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
        private int rating;
        /**
         * _id : 57fdc07037bcee8e7b3f83fb
         * avatar : /avatar/80/36/8036a70c0c91e59aae027016667f1e9b
         * nickname : 生夏如花却未闻花名
         * activityAvatar :
         * type : normal
         * lv : 8
         * gender : male
         */

        private AuthorBean author;
        /**
         * total : 4
         * yes : 6
         * no : 2
         */

        private HelpfulBean helpful;
        private int likeCount;
        private String state;
        private String updated;
        private String created;
        private int commentCount;
        private String content;
        private String title;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public int getRating() {
            return rating;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }

        public AuthorBean getAuthor() {
            return author;
        }

        public void setAuthor(AuthorBean author) {
            this.author = author;
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

        public int getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(int commentCount) {
            this.commentCount = commentCount;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public static class AuthorBean {
            private String _id;
            private String avatar;
            private String nickname;
            private String activityAvatar;
            private String type;
            private int lv;
            private String gender;

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getActivityAvatar() {
                return activityAvatar;
            }

            public void setActivityAvatar(String activityAvatar) {
                this.activityAvatar = activityAvatar;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public int getLv() {
                return lv;
            }

            public void setLv(int lv) {
                this.lv = lv;
            }

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
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
