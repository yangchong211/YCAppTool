package com.ycbjie.douban.bean;


import java.util.List;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/22
 * 描    述：豆瓣电影详情实体类
 * 修订历史：
 * ================================================
 */
public class DouMovieDetailBean {


    /**
     * max : 10
     * average : 7.4
     * stars : 40
     * min : 0
     */

    private RatingBean rating;
    /**
     * rating : {"max":10,"average":7.4,"stars":"40","min":0}
     * reviews_count : 8525
     * wish_count : 58403
     * douban_site :
     * year : 2017
     * images : {"small":"https://img3.doubanio.com/view/movie_poster_cover/ipst/public/p2485983612.jpg","large":"https://img3.doubanio.com/view/movie_poster_cover/lpst/public/p2485983612.jpg","medium":"https://img3.doubanio.com/view/movie_poster_cover/spst/public/p2485983612.jpg"}
     * alt : https://movie.douban.com/subject/26363254/
     * id : 26363254
     * mobile_url : https://movie.douban.com/subject/26363254/mobile
     * title : 战狼2
     * do_count : null
     * share_url : https://m.douban.com/movie/subject/26363254
     * seasons_count : null
     * schedule_url : https://movie.douban.com/subject/26363254/cinema/
     * episodes_count : null
     * countries : ["中国大陆"]
     * genres : ["动作"]
     * collect_count : 353840
     * casts : [{"alt":"https://movie.douban.com/celebrity/1000525/","avatars":{"small":"https://img3.doubanio.com/img/celebrity/small/39105.jpg","large":"https://img3.doubanio.com/img/celebrity/large/39105.jpg","medium":"https://img3.doubanio.com/img/celebrity/medium/39105.jpg"},"name":"吴京","id":"1000525"},{"alt":"https://movie.douban.com/celebrity/1100321/","avatars":{"small":"https://img1.doubanio.com/img/celebrity/small/1415801312.29.jpg","large":"https://img1.doubanio.com/img/celebrity/large/1415801312.29.jpg","medium":"https://img1.doubanio.com/img/celebrity/medium/1415801312.29.jpg"},"name":"弗兰克·格里罗","id":"1100321"},{"alt":"https://movie.douban.com/celebrity/1274840/","avatars":{"small":"https://img3.doubanio.com/img/celebrity/small/1401440361.14.jpg","large":"https://img3.doubanio.com/img/celebrity/large/1401440361.14.jpg","medium":"https://img3.doubanio.com/img/celebrity/medium/1401440361.14.jpg"},"name":"吴刚","id":"1274840"},{"alt":"https://movie.douban.com/celebrity/1031500/","avatars":{"small":"https://img1.doubanio.com/img/celebrity/small/1408604480.79.jpg","large":"https://img1.doubanio.com/img/celebrity/large/1408604480.79.jpg","medium":"https://img1.doubanio.com/img/celebrity/medium/1408604480.79.jpg"},"name":"张翰","id":"1031500"}]
     * current_season : null
     * original_title : 战狼2
     * summary : 故事发生在非洲附近的大海上，主人公冷锋（吴京 饰）遭遇人生滑铁卢，被“开除军籍”，本想漂泊一生的他，正当他打算这么做的时候，一场突如其来的意外打破了他的计划，突然被卷入了一场非洲国家叛乱，本可以安全撤离，却因无法忘记曾经为军人的使命，孤身犯险冲回沦陷区，带领身陷屠杀中的同胞和难民，展开生死逃亡。随着斗争的持续，体内的狼性逐渐复苏，最终孤身闯入战乱区域，为同胞而战斗。
     * subtype : movie
     * directors : [{"alt":"https://movie.douban.com/celebrity/1000525/","avatars":{"small":"https://img3.doubanio.com/img/celebrity/small/39105.jpg","large":"https://img3.doubanio.com/img/celebrity/large/39105.jpg","medium":"https://img3.doubanio.com/img/celebrity/medium/39105.jpg"},"name":"吴京","id":"1000525"}]
     * comments_count : 181537
     * ratings_count : 343119
     * aka : ["新战狼","新战死沙场","Wolf Warriors 2"]
     */

    private int reviews_count;
    private int wish_count;
    private String douban_site;
    private String year;
    /**
     * small : https://img3.doubanio.com/view/movie_poster_cover/ipst/public/p2485983612.jpg
     * large : https://img3.doubanio.com/view/movie_poster_cover/lpst/public/p2485983612.jpg
     * medium : https://img3.doubanio.com/view/movie_poster_cover/spst/public/p2485983612.jpg
     */

    private ImagesBean images;
    private String alt;
    private String id;
    private String mobile_url;
    private String title;
    private Object do_count;
    private String share_url;
    private Object seasons_count;
    private String schedule_url;
    private Object episodes_count;
    private int collect_count;
    private Object current_season;
    private String original_title;
    private String summary;
    private String subtype;
    private int comments_count;
    private int ratings_count;
    private List<String> countries;
    private List<String> genres;
    /**
     * alt : https://movie.douban.com/celebrity/1000525/
     * avatars : {"small":"https://img3.doubanio.com/img/celebrity/small/39105.jpg","large":"https://img3.doubanio.com/img/celebrity/large/39105.jpg","medium":"https://img3.doubanio.com/img/celebrity/medium/39105.jpg"}
     * name : 吴京
     * id : 1000525
     */

    private List<CastsBean> casts;
    /**
     * alt : https://movie.douban.com/celebrity/1000525/
     * avatars : {"small":"https://img3.doubanio.com/img/celebrity/small/39105.jpg","large":"https://img3.doubanio.com/img/celebrity/large/39105.jpg","medium":"https://img3.doubanio.com/img/celebrity/medium/39105.jpg"}
     * name : 吴京
     * id : 1000525
     */

    private List<DirectorsBean> directors;
    private List<String> aka;

    public RatingBean getRating() {
        return rating;
    }

    public void setRating(RatingBean rating) {
        this.rating = rating;
    }

    public int getReviews_count() {
        return reviews_count;
    }

    public void setReviews_count(int reviews_count) {
        this.reviews_count = reviews_count;
    }

    public int getWish_count() {
        return wish_count;
    }

    public void setWish_count(int wish_count) {
        this.wish_count = wish_count;
    }

    public String getDouban_site() {
        return douban_site;
    }

    public void setDouban_site(String douban_site) {
        this.douban_site = douban_site;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public ImagesBean getImages() {
        return images;
    }

    public void setImages(ImagesBean images) {
        this.images = images;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile_url() {
        return mobile_url;
    }

    public void setMobile_url(String mobile_url) {
        this.mobile_url = mobile_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getDo_count() {
        return do_count;
    }

    public void setDo_count(Object do_count) {
        this.do_count = do_count;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public Object getSeasons_count() {
        return seasons_count;
    }

    public void setSeasons_count(Object seasons_count) {
        this.seasons_count = seasons_count;
    }

    public String getSchedule_url() {
        return schedule_url;
    }

    public void setSchedule_url(String schedule_url) {
        this.schedule_url = schedule_url;
    }

    public Object getEpisodes_count() {
        return episodes_count;
    }

    public void setEpisodes_count(Object episodes_count) {
        this.episodes_count = episodes_count;
    }

    public int getCollect_count() {
        return collect_count;
    }

    public void setCollect_count(int collect_count) {
        this.collect_count = collect_count;
    }

    public Object getCurrent_season() {
        return current_season;
    }

    public void setCurrent_season(Object current_season) {
        this.current_season = current_season;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public int getComments_count() {
        return comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public int getRatings_count() {
        return ratings_count;
    }

    public void setRatings_count(int ratings_count) {
        this.ratings_count = ratings_count;
    }

    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public List<CastsBean> getCasts() {
        return casts;
    }

    public void setCasts(List<CastsBean> casts) {
        this.casts = casts;
    }

    public List<DirectorsBean> getDirectors() {
        return directors;
    }

    public void setDirectors(List<DirectorsBean> directors) {
        this.directors = directors;
    }

    public List<String> getAka() {
        return aka;
    }

    public void setAka(List<String> aka) {
        this.aka = aka;
    }

    public static class RatingBean {
        private int max;
        private double average;
        private String stars;
        private int min;

        public int getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }

        public double getAverage() {
            return average;
        }

        public void setAverage(double average) {
            this.average = average;
        }

        public String getStars() {
            return stars;
        }

        public void setStars(String stars) {
            this.stars = stars;
        }

        public int getMin() {
            return min;
        }

        public void setMin(int min) {
            this.min = min;
        }
    }

    public static class ImagesBean {
        private String small;
        private String large;
        private String medium;

        public String getSmall() {
            return small;
        }

        public void setSmall(String small) {
            this.small = small;
        }

        public String getLarge() {
            return large;
        }

        public void setLarge(String large) {
            this.large = large;
        }

        public String getMedium() {
            return medium;
        }

        public void setMedium(String medium) {
            this.medium = medium;
        }
    }

    public static class CastsBean {
        private String alt;
        /**
         * small : https://img3.doubanio.com/img/celebrity/small/39105.jpg
         * large : https://img3.doubanio.com/img/celebrity/large/39105.jpg
         * medium : https://img3.doubanio.com/img/celebrity/medium/39105.jpg
         */

        private AvatarsBean avatars;
        private String name;
        private String id;

        public String getAlt() {
            return alt;
        }

        public void setAlt(String alt) {
            this.alt = alt;
        }

        public AvatarsBean getAvatars() {
            return avatars;
        }

        public void setAvatars(AvatarsBean avatars) {
            this.avatars = avatars;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public static class AvatarsBean {
            private String small;
            private String large;
            private String medium;

            public String getSmall() {
                return small;
            }

            public void setSmall(String small) {
                this.small = small;
            }

            public String getLarge() {
                return large;
            }

            public void setLarge(String large) {
                this.large = large;
            }

            public String getMedium() {
                return medium;
            }

            public void setMedium(String medium) {
                this.medium = medium;
            }
        }
    }

    public static class DirectorsBean {
        private String alt;
        /**
         * small : https://img3.doubanio.com/img/celebrity/small/39105.jpg
         * large : https://img3.doubanio.com/img/celebrity/large/39105.jpg
         * medium : https://img3.doubanio.com/img/celebrity/medium/39105.jpg
         */

        private AvatarsBean avatars;
        private String name;
        private String id;

        public String getAlt() {
            return alt;
        }

        public void setAlt(String alt) {
            this.alt = alt;
        }

        public AvatarsBean getAvatars() {
            return avatars;
        }

        public void setAvatars(AvatarsBean avatars) {
            this.avatars = avatars;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public static class AvatarsBean {
            private String small;
            private String large;
            private String medium;

            public String getSmall() {
                return small;
            }

            public void setSmall(String small) {
                this.small = small;
            }

            public String getLarge() {
                return large;
            }

            public void setLarge(String large) {
                this.large = large;
            }

            public String getMedium() {
                return medium;
            }

            public void setMedium(String medium) {
                this.medium = medium;
            }
        }
    }
}
