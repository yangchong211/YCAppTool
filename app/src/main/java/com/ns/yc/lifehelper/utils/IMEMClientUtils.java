package com.ns.yc.lifehelper.utils;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.exceptions.HyphenateException;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.6
 * 创建日期：2017/11/14
 * 描    述：im即时通讯管理类，方便快速查找定位
 * 修订历史：
 * ================================================
 */
public class IMEMClientUtils {


    /**
     * 获取对象
     * @return                  EMClient对象
     */
    public static EMClient  getIMemClientInstance(){
        return EMClient.getInstance();
    }


    /**
     * 注册
     * @param username          用户名
     * @param pwd               密码
     */
    public static void  imRegister(String username, String pwd){
        try {
            EMClient.getInstance().createAccount(username, pwd);//同步方法
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 登录
     * @param userName          用户名
     * @param password          密码
     * @param callBack          callback
     */
    public static void imLogin(String userName,String password , EMCallBack callBack){
        /*EMClient.getInstance().login(userName,password,new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.d("main", "登录聊天服务器成功！");
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Log.d("main", "登录聊天服务器失败！");
            }
        });*/
        EMClient.getInstance().login(userName,password,callBack);
    }


    /**
     * 自动自动登录，初始化时调用
     * @param options           属性
     */
    public static void imAutoLogin(EMOptions options){
        //SDK 中自动登录属性默认是 true 打开的，如果不需要自动登录
        //在初始化 SDK 初始化的时候，调用options.setAutoLogin(false);设置为 false 关闭。
        options.setAutoLogin(false);
    }


    /**
     * 退出登录
     * 同步方法
     */
    public static void imOutLogin(){
        EMClient.getInstance().logout(true);
    }


    /**
     * 退出登录
     * 异步方法
     */
    public static void imOutLogin(boolean login , EMCallBack callBack){
        /*EMClient.getInstance().logout(true, new EMCallBack() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(int code, String message) {
            }
        });*/
        EMClient.getInstance().logout(login, callBack);
    }


    /**
     * 注册连接状态监听
     * 当掉线时，Android SDK 会自动重连，无需进行任何操作，通过注册连接监听来知道连接状态。
     * @param listener              自定义类并继承EMConnectionListener
     */
    public static void imAddConnectionListener(EMConnectionListener listener){
        //注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(listener);
    }


    /**
     * 发送文本消息
     * @param content               为消息文字内容
     * @param toChatUsername        为对方用户或者群聊的id
     * @param chatType              如果是群聊，设置chattype
     * @param CHATTYPE_GROUP        默认是单聊
     */
    public static void imSendMessage(String content , String toChatUsername
                                    ,String chatType , String CHATTYPE_GROUP){
        //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
        //如果是群聊，设置chattype，默认是单聊
        if (chatType == CHATTYPE_GROUP){
            message.setChatType(EMMessage.ChatType.GroupChat);
        }
        //发送消息
        EMClient.getInstance().chatManager().sendMessage(message);
    }


    /**
     * 发送语音消息
     * @param filePath              语音文件路径
     * @param length                录音时间(秒)
     * @param toChatUsername        为对方用户或者群聊的id
     * @param chatType              如果是群聊，设置chattype
     * @param CHATTYPE_GROUP        默认是单聊
     */
    public static void imVoiceSendMessage(String filePath, int length,String toChatUsername
                                            ,String chatType , String CHATTYPE_GROUP){
        //filePath为语音文件路径，length为录音时间(秒)
        EMMessage message = EMMessage.createVoiceSendMessage(filePath, length, toChatUsername);
        //如果是群聊，设置chattype，默认是单聊
        if (chatType == CHATTYPE_GROUP)
            message.setChatType(EMMessage.ChatType.GroupChat);
        EMClient.getInstance().chatManager().sendMessage(message);
    }


}
