package com.ns.yc.lifehelper.ui.other.myNews.wxNews.bean;

import java.util.List;

/**
 * Created by PC on 2017/8/30.
 * 作者：PC
 */

public class WxNewsTypeBean {


    /**
     * status : 0
     * msg : ok
     * result : [{"channelid":"1","channel":"热门"},{"channelid":"2","channel":"推荐"},{"channelid":"3","channel":"段子"},{"channelid":"4","channel":"养生"},{"channelid":"5","channel":"私房话"},{"channelid":"6","channel":"八卦"},{"channelid":"7","channel":"爱生活"},{"channelid":"8","channel":"财经"},{"channelid":"9","channel":"汽车"},{"channelid":"10","channel":"科技"},{"channelid":"11","channel":"潮人"},{"channelid":"12","channel":"辣妈"},{"channelid":"13","channel":"点赞党"},{"channelid":"14","channel":"旅行"},{"channelid":"15","channel":"职场"},{"channelid":"16","channel":"美食"},{"channelid":"17","channel":"古今通"},{"channelid":"18","channel":"学霸族"},{"channelid":"19","channel":"星座"},{"channelid":"20","channel":"体育"}]
     */

    private String status;
    private String msg;
    /**
     * channelid : 1
     * channel : 热门
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
        private String channelid;
        private String channel;

        public String getChannelid() {
            return channelid;
        }

        public void setChannelid(String channelid) {
            this.channelid = channelid;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }
    }
}
