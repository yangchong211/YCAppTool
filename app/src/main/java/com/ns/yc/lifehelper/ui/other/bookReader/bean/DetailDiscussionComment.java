package com.ns.yc.lifehelper.ui.other.bookReader.bean;

import java.util.List;

/**
 * Created by PC on 2017/10/19.
 * 作者：PC
 */

public class DetailDiscussionComment {


    /**
     * comments
     * ok : true
     */

    private boolean ok;
    /**
     * _id : 59e8350cfedb292e5f21efe3
     * content : 签
     * author
     * floor : 3182
     * likeCount : 0
     * created : 2017-10-19T05:15:57.167Z
     * replyTo : null
     */

    private List<CommentsBean> comments;

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public List<CommentsBean> getComments() {
        return comments;
    }

    public void setComments(List<CommentsBean> comments) {
        this.comments = comments;
    }

    public static class CommentsBean {
        private String _id;
        private String content;
        /**
         * _id : 59e5c77b17f756010c0772df
         * avatar :
         * nickname : 超级马丽
         * activityAvatar :
         * type : normal
         * lv : 2
         * gender : male
         */

        private AuthorBean author;
        private int floor;
        private int likeCount;
        private String created;
        private ReplyToBean replyTo;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public AuthorBean getAuthor() {
            return author;
        }

        public void setAuthor(AuthorBean author) {
            this.author = author;
        }

        public int getFloor() {
            return floor;
        }

        public void setFloor(int floor) {
            this.floor = floor;
        }

        public int getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(int likeCount) {
            this.likeCount = likeCount;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public ReplyToBean getReplyTo() {
            return replyTo;
        }

        public void ReplyToBean(ReplyToBean replyTo) {
            this.replyTo = replyTo;
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

        public static class ReplyToBean {
            public String _id;
            public int floor;
            /**
             * _id : 5794ad7ffda61987396d6216
             * nickname : 从未改变
             */

            public AuthorBean author;

            public static class AuthorBean {
                public String _id;
                public String nickname;
            }
        }
    }
}
