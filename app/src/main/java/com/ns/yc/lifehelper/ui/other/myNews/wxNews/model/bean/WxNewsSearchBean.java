package com.ns.yc.lifehelper.ui.other.myNews.wxNews.model.bean;

import java.util.List;

/**
 * Created by PC on 2017/8/30.
 * 作者：PC
 */

public class WxNewsSearchBean {

    private String status;
    private String msg;

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
        private String keyword;
        private String num;
        /**
         * channelid : 0
         * url : http://mp.weixin.qq.com/s?src=11&timestamp=1504161238&ver=363&signature=uE6owy9*YlC1ICPITL*WL107CCcBrmmGLkD0afD6ZMgXZmZ0SMYN7r*ZGkckDys6HuZV0psO-k-ciqhOHMAtR66ykp7cT9jozr1MRBrQ88xvj0epsHDwM7DlAbhC*iy1&new=1
         * title : 【关注】姚明，被抓了吗！？
         * time : 2017-07-08
         * weixinname : vivo浙江
         * weixinaccount : vivozj
         * weixinsummary : vivo智能手机（浙江）唯一指定微信服务平台。vivo，不断追求极致，持续创造惊喜。小v将为您带来最新的产品资讯，为您播报最新的活动详情，为您解答各种疑问！您的关注和意见，就是vivo不断追求极致和创造惊喜的最大动力！
         * pic : http://api.jisuapi.com/weixinarticle/upload/201708/31143358_67128.jpeg
         * readnum :
         * likenum :
         * content : <section data-tools=
         */

        private List<ListBean> list;

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private String channelid;
            private String url;
            private String title;
            private String time;
            private String weixinname;
            private String weixinaccount;
            private String weixinsummary;
            private String pic;
            private String readnum;
            private String likenum;
            private String content;

            public String getChannelid() {
                return channelid;
            }

            public void setChannelid(String channelid) {
                this.channelid = channelid;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

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

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
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

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }
        }
    }
}
