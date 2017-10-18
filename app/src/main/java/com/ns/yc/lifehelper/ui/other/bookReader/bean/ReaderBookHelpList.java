package com.ns.yc.lifehelper.ui.other.bookReader.bean;

import java.util.List;

/**
 * Created by PC on 2017/10/18.
 * 作者：PC
 */

public class ReaderBookHelpList {


    /**
     * helps
     * ok : true
     */

    private boolean ok;
    /**
     * _id : 59e0535fe7a1e07408c63134
     * likeCount : 225
     * haveImage : false
     * state : normal
     * updated : 2017-10-18T02:51:38.770Z
     * created : 2017-10-13T05:47:11.271Z
     * commentCount : 932
     * title : 求表扬！求点赞！\(•ㅂ•)/♥我又在这里拯救大家的书荒了~
     */

    private List<HelpsBean> helps;

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public List<HelpsBean> getHelps() {
        return helps;
    }

    public void setHelps(List<HelpsBean> helps) {
        this.helps = helps;
    }

    public static class HelpsBean {
        private String _id;
        /**
         * _id : 56283d24b185d12c3f5e50f6
         * avatar : /avatar/6c/0c/6c0c6d561028d4fb1d77329fb0d31ac4
         * nickname : 追书白小妹
         * activityAvatar : /activities/20170120/1.jpg
         * type : official
         * lv : 6
         * gender : female
         */

        private AuthorBean author;
        private int likeCount;
        private boolean haveImage;
        private String state;
        private String updated;
        private String created;
        private int commentCount;
        private String title;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public AuthorBean getAuthor() {
            return author;
        }

        public void setAuthor(AuthorBean author) {
            this.author = author;
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

        public int getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(int commentCount) {
            this.commentCount = commentCount;
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
    }
}
