package com.ns.yc.lifehelper.ui.other.expressDelivery.bean;

import java.util.List;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/18
 * 描    述：快递搜索结果实体类
 * 修订历史：
 * ================================================
 */
public class ExpressDeliverySearchBean {


    /**
     * status : 0
     * msg : ok
     * result : {"number":"3336396833149","type":"sto","list":[{"time":"2017-08-21 14:49:26","status":"北京第一分公司-郭立强-派件中"},{"time":"2017-08-21 13:51:20","status":"已到达-北京第一分公司"},{"time":"2017-08-21 09:22:02","status":"北京中转部-已发往-北京第一分公司"},{"time":"2017-08-19 21:53:37","status":"广东广州航空部-已发往-北京中转部"},{"time":"2017-08-19 21:53:37","status":"广东广州航空部-已装包"},{"time":"2017-08-19 21:07:58","status":"已到达-广东广州航空部"},{"time":"2017-08-19 13:49:45","status":"广东广州大客户营业部-花山骆驼-已收件"}],"deliverystatus":"2","issign":"0"}
     */

    private String status;
    private String msg;
    /**
     * number : 3336396833149
     * type : sto
     * list : [{"time":"2017-08-21 14:49:26","status":"北京第一分公司-郭立强-派件中"},{"time":"2017-08-21 13:51:20","status":"已到达-北京第一分公司"},{"time":"2017-08-21 09:22:02","status":"北京中转部-已发往-北京第一分公司"},{"time":"2017-08-19 21:53:37","status":"广东广州航空部-已发往-北京中转部"},{"time":"2017-08-19 21:53:37","status":"广东广州航空部-已装包"},{"time":"2017-08-19 21:07:58","status":"已到达-广东广州航空部"},{"time":"2017-08-19 13:49:45","status":"广东广州大客户营业部-花山骆驼-已收件"}]
     * deliverystatus : 2
     * issign : 0
     */

    private ResultBean result;

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

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        private String number;
        private String type;
        private String deliverystatus;
        private String issign;
        /**
         * time : 2017-08-21 14:49:26
         * status : 北京第一分公司-郭立强-派件中
         */

        private List<ListBean> list;

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDeliverystatus() {
            return deliverystatus;
        }

        public void setDeliverystatus(String deliverystatus) {
            this.deliverystatus = deliverystatus;
        }

        public String getIssign() {
            return issign;
        }

        public void setIssign(String issign) {
            this.issign = issign;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private String time;
            private String status;

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }
        }
    }
}
