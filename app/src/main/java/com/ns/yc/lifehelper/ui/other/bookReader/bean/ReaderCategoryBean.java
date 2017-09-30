package com.ns.yc.lifehelper.ui.other.bookReader.bean;

import java.util.List;

/**
 * Created by PC on 2017/9/21.
 * 作者：PC
 */

public class ReaderCategoryBean {


    /**
     * male : [{"name":"玄幻","bookCount":478785,"monthlyCount":13370,"icon":"/icon/玄幻_.png"},{"name":"奇幻","bookCount":43876,"monthlyCount":1386,"icon":"/icon/奇幻_.png"},{"name":"武侠","bookCount":39264,"monthlyCount":947,"icon":"/icon/武侠_.png"},{"name":"仙侠","bookCount":126435,"monthlyCount":5300,"icon":"/icon/仙侠_.png"},{"name":"都市","bookCount":344343,"monthlyCount":9324,"icon":"/icon/都市_.png"},{"name":"职场","bookCount":15922,"monthlyCount":642,"icon":"/icon/职场_.png"},{"name":"历史","bookCount":68022,"monthlyCount":2200,"icon":"/icon/历史_.png"},{"name":"军事","bookCount":14532,"monthlyCount":1080,"icon":"/icon/军事_.png"},{"name":"游戏","bookCount":78935,"monthlyCount":1925,"icon":"/icon/游戏_.png"},{"name":"竞技","bookCount":5524,"monthlyCount":242,"icon":"/icon/竞技_.png"},{"name":"科幻","bookCount":111706,"monthlyCount":1640,"icon":"/icon/科幻_.png"},{"name":"灵异","bookCount":31851,"monthlyCount":2475,"icon":"/icon/灵异_.png"},{"name":"同人","bookCount":36328,"monthlyCount":326,"icon":"/icon/同人_.png"},{"name":"轻小说","bookCount":4742,"monthlyCount":207,"icon":"/icon/轻小说_.png"}]
     * female : [{"name":"古代言情","bookCount":440325,"monthlyCount":9392,"icon":"/icon/古代言情_.png"},{"name":"现代言情","bookCount":555287,"monthlyCount":14361,"icon":"/icon/现代言情_.png"},{"name":"青春校园","bookCount":105792,"monthlyCount":2507,"icon":"/icon/青春校园_.png"},{"name":"纯爱","bookCount":129383,"monthlyCount":1371,"icon":"/icon/耽美_.png"},{"name":"玄幻奇幻","bookCount":127865,"monthlyCount":420,"icon":"/icon/玄幻奇幻_.png"},{"name":"武侠仙侠","bookCount":62558,"monthlyCount":1308,"icon":"/icon/武侠仙侠_.png"},{"name":"科幻","bookCount":9139,"monthlyCount":252,"icon":"/icon/科幻_.png"},{"name":"游戏竞技","bookCount":5941,"monthlyCount":104,"icon":"/icon/游戏竞技_.png"},{"name":"悬疑灵异","bookCount":14141,"monthlyCount":484,"icon":"/icon/悬疑灵异_.png"},{"name":"同人","bookCount":122510,"monthlyCount":146,"icon":"/icon/同人_.png"},{"name":"女尊","bookCount":20565,"monthlyCount":880,"icon":"/icon/女尊_.png"},{"name":"莉莉","bookCount":25254,"monthlyCount":35,"icon":"/icon/百合_.png"}]
     * picture : [{"name":"热血","bookCount":330,"monthlyCount":0,"icon":"/icon/热血_.png"},{"name":"魔幻","bookCount":331,"monthlyCount":0,"icon":"/icon/魔幻_.png"},{"name":"科幻","bookCount":64,"monthlyCount":0,"icon":"/icon/科幻_.png"},{"name":"恋爱","bookCount":557,"monthlyCount":0,"icon":"/icon/恋爱_.png"},{"name":"搞笑","bookCount":562,"monthlyCount":0,"icon":"/icon/搞笑_.png"},{"name":"悬疑","bookCount":172,"monthlyCount":0,"icon":"/icon/悬疑_.png"},{"name":"少儿","bookCount":2610,"monthlyCount":0,"icon":"/icon/少儿_.png"}]
     * press : [{"name":"传记名著","bookCount":2801,"monthlyCount":0,"icon":"/icon/传记名著_.png"},{"name":"出版小说","bookCount":5377,"monthlyCount":0,"icon":"/icon/出版小说_.png"},{"name":"人文社科","bookCount":15085,"monthlyCount":0,"icon":"/icon/人文社科_.png"},{"name":"生活时尚","bookCount":1304,"monthlyCount":0,"icon":"/icon/生活时尚_.png"},{"name":"经管理财","bookCount":4720,"monthlyCount":0,"icon":"/icon/经管理财_.png"},{"name":"青春言情","bookCount":3651,"monthlyCount":0,"icon":"/icon/青春言情_.png"},{"name":"外文原版","bookCount":686,"monthlyCount":0,"icon":"/icon/外文原版_.png"},{"name":"政治军事","bookCount":312,"monthlyCount":0,"icon":"/icon/政治军事_.png"},{"name":"成功励志","bookCount":5636,"monthlyCount":0,"icon":"/icon/成功励志_.png"},{"name":"育儿健康","bookCount":4874,"monthlyCount":0,"icon":"/icon/育儿健康_.png"}]
     * ok : true
     */

    private boolean ok;
    /**
     * name : 玄幻
     * bookCount : 478785
     * monthlyCount : 13370
     * icon : /icon/玄幻_.png
     */

    private List<MaleBean> male;
    /**
     * name : 古代言情
     * bookCount : 440325
     * monthlyCount : 9392
     * icon : /icon/古代言情_.png
     */

    private List<FemaleBean> female;
    /**
     * name : 热血
     * bookCount : 330
     * monthlyCount : 0
     * icon : /icon/热血_.png
     */

    private List<PictureBean> picture;
    /**
     * name : 传记名著
     * bookCount : 2801
     * monthlyCount : 0
     * icon : /icon/传记名著_.png
     */

    private List<PressBean> press;

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public List<MaleBean> getMale() {
        return male;
    }

    public void setMale(List<MaleBean> male) {
        this.male = male;
    }

    public List<FemaleBean> getFemale() {
        return female;
    }

    public void setFemale(List<FemaleBean> female) {
        this.female = female;
    }

    public List<PictureBean> getPicture() {
        return picture;
    }

    public void setPicture(List<PictureBean> picture) {
        this.picture = picture;
    }

    public List<PressBean> getPress() {
        return press;
    }

    public void setPress(List<PressBean> press) {
        this.press = press;
    }

    public static class MaleBean {
        private String name;
        private int bookCount;
        private int monthlyCount;
        private String icon;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getBookCount() {
            return bookCount;
        }

        public void setBookCount(int bookCount) {
            this.bookCount = bookCount;
        }

        public int getMonthlyCount() {
            return monthlyCount;
        }

        public void setMonthlyCount(int monthlyCount) {
            this.monthlyCount = monthlyCount;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }

    public static class FemaleBean {
        private String name;
        private int bookCount;
        private int monthlyCount;
        private String icon;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getBookCount() {
            return bookCount;
        }

        public void setBookCount(int bookCount) {
            this.bookCount = bookCount;
        }

        public int getMonthlyCount() {
            return monthlyCount;
        }

        public void setMonthlyCount(int monthlyCount) {
            this.monthlyCount = monthlyCount;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }

    public static class PictureBean {
        private String name;
        private int bookCount;
        private int monthlyCount;
        private String icon;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getBookCount() {
            return bookCount;
        }

        public void setBookCount(int bookCount) {
            this.bookCount = bookCount;
        }

        public int getMonthlyCount() {
            return monthlyCount;
        }

        public void setMonthlyCount(int monthlyCount) {
            this.monthlyCount = monthlyCount;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }

    public static class PressBean {
        private String name;
        private int bookCount;
        private int monthlyCount;
        private String icon;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getBookCount() {
            return bookCount;
        }

        public void setBookCount(int bookCount) {
            this.bookCount = bookCount;
        }

        public int getMonthlyCount() {
            return monthlyCount;
        }

        public void setMonthlyCount(int monthlyCount) {
            this.monthlyCount = monthlyCount;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
}
