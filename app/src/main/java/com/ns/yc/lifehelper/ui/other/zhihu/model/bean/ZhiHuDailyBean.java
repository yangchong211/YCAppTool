package com.ns.yc.lifehelper.ui.other.zhihu.model.bean;

import java.util.List;

/**
 * Created by PC on 2017/11/29.
 * 作者：PC
 */

public class ZhiHuDailyBean {


    /**
     * date : 20171129
     * stories : [{"images":["https://pic3.zhimg.com/v2-e63eeda7df16f282a10321775065b922.jpg"],"type":0,"id":9659012,"ga_prefix":"112917","title":"快被遗忘的迅雷成为话题中心：迅雷金融是骗局，跟我没关系"},{"images":["https://pic2.zhimg.com/v2-e380e4f6c65914b904890b000fcef63d.jpg"],"type":0,"id":9659009,"ga_prefix":"112916","title":"她是拥有星星的女人"},{"title":"每次去野外考察，什么形象都顾不得了","ga_prefix":"112915","images":["https://pic2.zhimg.com/v2-084b23d69a4d24db34bdfd012383c8d9.jpg"],"multipic":true,"type":0,"id":9658993},{"images":["https://pic1.zhimg.com/v2-3d1c20a9ac72c0010f741d321f991c78.jpg"],"type":0,"id":9658789,"ga_prefix":"112914","title":"偷偷蹭了别人拿手机开的热点，算盗窃吗？"},{"images":["https://pic4.zhimg.com/v2-ae3eb39c8469a98dce1ed1111e433c8f.jpg"],"type":0,"id":9658957,"ga_prefix":"112913","title":"总听说学美术的去搞音乐，为什么很少有学音乐的去搞美术？"},{"images":["https://pic2.zhimg.com/v2-016c7a1ad4ef091dd4846393bfa627a5.jpg"],"type":0,"id":9656358,"ga_prefix":"112912","title":"大误 · 我仍未知道那天所看见的刀的名字"},{"images":["https://pic4.zhimg.com/v2-bcd7e09301b7fc09c669b85b1d29cbe7.jpg"],"type":0,"id":9658813,"ga_prefix":"112911","title":"别搞错了，汽车头枕从来不是给你用来靠的"},{"images":["https://pic2.zhimg.com/v2-164dc4d202800b6206ba090fb26960ed.jpg"],"type":0,"id":9658612,"ga_prefix":"112910","title":"想判断自己能不能打得过对方，首先你要偷瞄下 Ta 的脖子"},{"images":["https://pic2.zhimg.com/v2-abe847cc4b0b8e83a72be321a5271fb1.jpg"],"type":0,"id":9658806,"ga_prefix":"112909","title":"一夜之间，美国忽然多出了很多高血压患者"},{"images":["https://pic3.zhimg.com/v2-72bb39c4dc9fa01edf775c473f7cc6aa.jpg"],"type":0,"id":9658712,"ga_prefix":"112908","title":"作为儿科大夫，这些忠告一般人我都会告诉，可人家就是不听\u2026\u2026"},{"images":["https://pic4.zhimg.com/v2-df6adc3af89fc2d7b926627ca9fc6d9f.jpg"],"type":0,"id":9658863,"ga_prefix":"112907","title":"《寻梦环游记》这段隐藏剧情，发现它才算真正看懂这部电影"},{"images":["https://pic1.zhimg.com/v2-d48cdce3c7f6f40396fd73767fda4114.jpg"],"type":0,"id":9658872,"ga_prefix":"112907","title":"孩子 2200 元的耳机在学校参加活动时被同学抢走弄断，该索赔吗？"},{"images":["https://pic1.zhimg.com/v2-3bd0c79d6bfc92eab0ab83d3621731fc.jpg"],"type":0,"id":9658794,"ga_prefix":"112907","title":"线上卖电影票平台只剩两巨头了：猫眼向左，淘票票向右"},{"images":["https://pic2.zhimg.com/v2-d21b6989fc33fb86a8fec5f3998c42c1.jpg"],"type":0,"id":9658788,"ga_prefix":"112906","title":"瞎扯 · 如何正确地吐槽"}]
     * top_stories : [{"image":"https://pic3.zhimg.com/v2-4fa9c6abe4729459aff34cdd29c4282e.jpg","type":0,"id":9659012,"ga_prefix":"112917","title":"快被遗忘的迅雷成为话题中心：迅雷金融是骗局，跟我没关系"},{"image":"https://pic4.zhimg.com/v2-6b34751926ee8b64b93cb030ad883cd3.jpg","type":0,"id":9659009,"ga_prefix":"112916","title":"她是拥有星星的女人"},{"image":"https://pic1.zhimg.com/v2-06b5c3a90e393444d91cc1025cb2ec98.jpg","type":0,"id":9658794,"ga_prefix":"112907","title":"线上卖电影票平台只剩两巨头了：猫眼向左，淘票票向右"},{"image":"https://pic3.zhimg.com/v2-26e0fa03b769b6251f476ab64d28a9c2.jpg","type":0,"id":9658806,"ga_prefix":"112909","title":"一夜之间，美国忽然多出了很多高血压患者"},{"image":"https://pic1.zhimg.com/v2-13b2973c3ab79d9b0dd754c9d11ad430.jpg","type":0,"id":9658872,"ga_prefix":"112907","title":"孩子 2200 元的耳机在学校参加活动时被同学抢走弄断，该索赔吗？"}]
     */

    private String date;
    /**
     * images : ["https://pic3.zhimg.com/v2-e63eeda7df16f282a10321775065b922.jpg"]
     * type : 0
     * id : 9659012
     * ga_prefix : 112917
     * title : 快被遗忘的迅雷成为话题中心：迅雷金融是骗局，跟我没关系
     */

    private List<StoriesBean> stories;
    /**
     * image : https://pic3.zhimg.com/v2-4fa9c6abe4729459aff34cdd29c4282e.jpg
     * type : 0
     * id : 9659012
     * ga_prefix : 112917
     * title : 快被遗忘的迅雷成为话题中心：迅雷金融是骗局，跟我没关系
     */

    private List<TopStoriesBean> top_stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<StoriesBean> getStories() {
        return stories;
    }

    public void setStories(List<StoriesBean> stories) {
        this.stories = stories;
    }

    public List<TopStoriesBean> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(List<TopStoriesBean> top_stories) {
        this.top_stories = top_stories;
    }

    public static class StoriesBean {
        private int type;
        private int id;
        private String ga_prefix;
        private String title;
        private List<String> images;
        private boolean readState;              //添加字段，是否阅读

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public boolean isReadState() {
            return readState;
        }

        public void setReadState(boolean readState) {
            this.readState = readState;
        }
    }

    public static class TopStoriesBean {
        private String image;
        private int type;
        private int id;
        private String ga_prefix;
        private String title;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
