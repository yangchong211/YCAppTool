package com.ns.yc.lifehelper.ui.other.myNews.weChat.model.bean;

import java.util.List;

/**
 * Created by yc on 2018/2/27.
 */

public class WxNewsDetailBean {

    /**
     * status : 0
     * msg : ok
     */

    private String status;
    private String msg;
    /**
     * channel : 推荐
     * channelid : 2
     * total : 20382
     * num : 10
     * start : 5
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
        private String channel;
        private String channelid;
        private String total;
        private String num;
        private String start;
        /**
         * title : 迎风踏浪游菊岛 | 2017澎湖跨海马拉松即将迎风开跑
         * time : 2017-08-30
         * weixinname : 台湾达人
         * weixinaccount : taiwandaren
         * weixinsummary : 台湾达人订阅号。关注台湾达人，获取台湾美食、景点、娱乐、购物……好吃的、好玩的台湾资讯。解答有关台湾自由行的各种疑惑。
         * channelid : 2
         * pic : http://api.jisuapi.com/weixinarticle/upload/201708/30141003_40960.jpeg
         * url : http://mp.weixin.qq.com/s?src=11&timestamp=1504072803&ver=361&signature=HDaDT4AiOZ5jcRxWY*A9ppYsR8v6CZq1d-O1TKmBegYh7mnwwga0RTvHiBvU8YyqWFRNXiIyiRZ6bcoiG3XN8Tlwtv3ZVE77qHchlMAORlLoeKvH4oYCgVl11MzFQURr&new=1
         * readnum : 0
         * likenum : 0
         */

        private List<ListBean> list;

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getChannelid() {
            return channelid;
        }

        public void setChannelid(String channelid) {
            this.channelid = channelid;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private String title;
            private String time;
            private String weixinname;
            private String weixinaccount;
            private String weixinsummary;
            private String channelid;
            private String pic;
            private String content;
            private String url;
            private String readnum;
            private String likenum;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getWeixinname() {
                return weixinname;
            }

            public void setWeixinname(String weixinname) {
                this.weixinname = weixinname;
            }

            public String getWeixinaccount() {
                return weixinaccount;
            }

            public void setWeixinaccount(String weixinaccount) {
                this.weixinaccount = weixinaccount;
            }

            public String getWeixinsummary() {
                return weixinsummary;
            }

            public void setWeixinsummary(String weixinsummary) {
                this.weixinsummary = weixinsummary;
            }

            public String getChannelid() {
                return channelid;
            }

            public void setChannelid(String channelid) {
                this.channelid = channelid;
            }

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getReadnum() {
                return readnum;
            }

            public void setReadnum(String readnum) {
                this.readnum = readnum;
            }

            public String getLikenum() {
                return likenum;
            }

            public void setLikenum(String likenum) {
                this.likenum = likenum;
            }
        }
    }

}
