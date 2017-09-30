package com.ns.yc.lifehelper.ui.other.myKnowledge.bean;

import java.util.List;

/**
 * Created by PC on 2017/8/28.
 * 作者：PC
 */

public class SearchHotBean {


    /**
     * code : 1
     * data : {"data":[{"title":"众筹"},{"title":"早期投资"},{"title":"微信"},{"title":"产品经理"},{"title":"p2p"},{"title":"o2o"},{"title":"创业"}]}
     */

    private int code;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * title : 众筹
         */

        private List<DataBeanHot> data;

        public List<DataBeanHot> getData() {
            return data;
        }

        public void setData(List<DataBeanHot> data) {
            this.data = data;
        }

        public static class DataBeanHot {
            private String title;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }
    }
}
