package com.yc.socket.easy;

import android.text.TextUtils;


/**
 * 发送用户信息的结构
 */
public class SendUserInfoBean extends BaseBean {

    private String action;//类型
    private String cardid;//卡号
    private String userid;//用户id

    public SendUserInfoBean() {
        this.action = "send_userinfo";
    }

    public SendUserInfoBean(String cardid, String userid) {
        if (TextUtils.isEmpty(cardid)){
            cardid = "";
        }
        if (TextUtils.isEmpty(userid)){
            userid = "";
        }
        this.action = "send_userinfo";
        this.cardid = cardid;
        this.userid = userid;
    }

    public String getAction() {
        return action;
    }

    public String getCardid() {
        return cardid;
    }

    public void setCardid(String cardid) {
        this.cardid = cardid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    @Override
    public String toString() {
        return "PaySuccessBean{" +
                "action='" + action + '\'' +
                ", cardid='" + cardid + '\'' +
                ", userid='" + userid + '\'' +
                '}';
    }

    @Override
    public String toJsonString(){
        return  "{\"action\":\""+action+"\"," +
                "\"cardid\":\""+cardid+"\"," +
                "\"userid\":\""+userid+"\"" +
                "}";
    }
}
