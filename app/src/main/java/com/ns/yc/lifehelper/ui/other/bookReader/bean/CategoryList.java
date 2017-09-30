package com.ns.yc.lifehelper.ui.other.bookReader.bean;

import java.util.List;

/**
 * Created by PC on 2017/9/21.
 * 作者：PC
 */

public class CategoryList {

    /**
     * male : [{"name":"玄幻","bookCount":188244},{"name":"奇幻","bookCount":24183}]
     * ok : true
     */

    public List<MaleBean> male;
    /**
     * name : 古代言情
     * bookCount : 125103
     */

    public List<MaleBean> female;

    public static class MaleBean {
        public String name;
        public int bookCount;

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
    }

}
