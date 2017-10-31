package com.ns.yc.lifehelper.ui.other.bookReader.bean;

import java.util.List;

/**
 * Created by PC on 2017/10/25.
 * 作者：PC
 */

public class DetailBookSubjectList {


    /**
     * _id : 59e87199814d32644db73bc1
     * updated : 2017-10-19T09:34:17.692Z
     * title : 走肾💋十年老司机力荐种马爽
     * author
     * desc : 全是小爽书！据说lv5以下收藏不了？恁试试看！
     * gender : male
     * created : 2017-10-19T09:34:17.675Z
     * tags : ["都市","都市生活"]
     * stickStopTime : null
     * isDraft : false
     * isDistillate : null
     * collectorCount : 1705
     * books
     * shareLink : http://share.zhuishushenqi.com/booklist/59e87199814d32644db73bc1
     * id : 59e87199814d32644db73bc1
     * total : 36
     */

    private BookListBean bookList;
    /**
     * bookList
     * ok : true
     */

    private boolean ok;

    public BookListBean getBookList() {
        return bookList;
    }

    public void setBookList(BookListBean bookList) {
        this.bookList = bookList;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public static class BookListBean {
        private String _id;
        private String updated;
        private String title;
        /**
         * _id : 59183093da83da7e4ff94218
         * avatar : /avatar/f7/f8/f7f80b0d766dd564ae40d701d8fae90a
         * nickname : 闪更半夜
         * type : normal
         * lv : 7
         */

        private AuthorBean author;
        private String desc;
        private String gender;
        private String created;
        private Object stickStopTime;
        private boolean isDraft;
        private Object isDistillate;
        private int collectorCount;
        private String shareLink;
        private String id;
        private int total;
        private List<String> tags;
        /**
         * book
         * comment :
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

        public AuthorBean getAuthor() {
            return author;
        }

        public void setAuthor(AuthorBean author) {
            this.author = author;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public Object getStickStopTime() {
            return stickStopTime;
        }

        public void setStickStopTime(Object stickStopTime) {
            this.stickStopTime = stickStopTime;
        }

        public boolean isIsDraft() {
            return isDraft;
        }

        public void setIsDraft(boolean isDraft) {
            this.isDraft = isDraft;
        }

        public Object getIsDistillate() {
            return isDistillate;
        }

        public void setIsDistillate(Object isDistillate) {
            this.isDistillate = isDistillate;
        }

        public int getCollectorCount() {
            return collectorCount;
        }

        public void setCollectorCount(int collectorCount) {
            this.collectorCount = collectorCount;
        }

        public String getShareLink() {
            return shareLink;
        }

        public void setShareLink(String shareLink) {
            this.shareLink = shareLink;
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

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }

        public List<BooksBean> getBooks() {
            return books;
        }

        public void setBooks(List<BooksBean> books) {
            this.books = books;
        }

        public static class AuthorBean {
            private String _id;
            private String avatar;
            private String nickname;
            private String type;
            private int lv;

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
        }

        public static class BooksBean {
            /**
             * cat : 都市生活
             * _id : 59cf4904c3aa10de76bd7b96
             * title : 躁动吧，青春
             * author : 一织
             * longIntro : 美女校花约我去她家，到了之后她竟然……公司的呆萌小美女，学校的漂亮校花，还有女王美女上司，不同的人设，同样的纯真……《我们纯真的青春》姊妹篇，又名《即使如此还是喜欢你》，关于青春的朦胧、暗恋、心动以及成长，千万“芝麻”必读。因为有你，所以青春！
             * majorCate : 都市
             * minorCate : 都市生活
             * cover : /agent/http%3A%2F%2Fimg.1391.com%2Fapi%2Fv1%2Fbookcenter%2Fcover%2F1%2F2144057%2F2144057_5a4ca3bd54ee4e41b7d93a48b69ec359.jpg%2F
             * site : zhuishuvip
             * banned : 0
             * latelyFollower : 12097
             * wordCount : 128599
             * retentionRatio : 10.56
             */

            private BookBean book;
            private String comment;

            public BookBean getBook() {
                return book;
            }

            public void setBook(BookBean book) {
                this.book = book;
            }

            public String getComment() {
                return comment;
            }

            public void setComment(String comment) {
                this.comment = comment;
            }

            public static class BookBean {
                private String cat;
                private String _id;
                private String title;
                private String author;
                private String longIntro;
                private String majorCate;
                private String minorCate;
                private String cover;
                private String site;
                private int banned;
                private int latelyFollower;
                private int wordCount;
                private double retentionRatio;

                public String getCat() {
                    return cat;
                }

                public void setCat(String cat) {
                    this.cat = cat;
                }

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

                public String getLongIntro() {
                    return longIntro;
                }

                public void setLongIntro(String longIntro) {
                    this.longIntro = longIntro;
                }

                public String getMajorCate() {
                    return majorCate;
                }

                public void setMajorCate(String majorCate) {
                    this.majorCate = majorCate;
                }

                public String getMinorCate() {
                    return minorCate;
                }

                public void setMinorCate(String minorCate) {
                    this.minorCate = minorCate;
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

                public int getWordCount() {
                    return wordCount;
                }

                public void setWordCount(int wordCount) {
                    this.wordCount = wordCount;
                }

                public double getRetentionRatio() {
                    return retentionRatio;
                }

                public void setRetentionRatio(double retentionRatio) {
                    this.retentionRatio = retentionRatio;
                }
            }
        }
    }
}
