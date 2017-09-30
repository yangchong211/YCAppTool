package com.ns.yc.lifehelper.ui.other.myTsSc.bean;

import java.util.List;

/**
 * Created by PC on 2017/8/29.
 * 作者：PC
 */

public class TangShiChapter {


    private String status;
    private String msg;
    /**
     * detailid : 1
     * name : 行宫
     * author : 元稹
     */

    private List<ResultBean> result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        private String detailid;
        private String name;
        private String author;

        public String getDetailid() {
            return detailid;
        }

        public void setDetailid(String detailid) {
            this.detailid = detailid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }
    }
}
