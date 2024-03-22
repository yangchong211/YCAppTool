package com.yc.socket.easy;

/**
 * 响应码结构
 */
public class ResponseBean extends BaseBean {

    private String action;//类型
    private int errcode;//错误码
    private String errmsg;//错误信息
    private int msgid;//消息id

    public ResponseBean() {

    }

    public ResponseBean(String action, int errcode, String errmsg) {
        this.action = action;
        this.errcode = errcode;
        this.errmsg = errmsg;
    }

    public ResponseBean(String action, int errcode, String errmsg, int msgid) {
        this.action = action;
        this.errcode = errcode;
        this.errmsg = errmsg;
        this.msgid = msgid;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public int getMsgid() {
        return msgid;
    }

    public void setMsgid(int msgid) {
        this.msgid = msgid;
    }

    @Override
    public String toString() {
        return "ResponseBean{" +
                "action='" + action + '\'' +
                ", errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                '}';
    }

    @Override
    public String toJsonString() {
        return  "{\"action\":\""+action+"\"," +
                "\"errmsg\":\""+errmsg+"\"," +
                "\"errcode\":"+errcode +
                "}";
    }
}
