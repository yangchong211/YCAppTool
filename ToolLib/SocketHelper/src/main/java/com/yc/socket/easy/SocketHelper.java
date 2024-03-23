package com.yc.socket.easy;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.yc.toolutils.system.AppLogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * Usb通信的管理类，负责解析数据并且回调数据
 */
public class SocketHelper implements Serializable {

    //TAG
    private final String TAG = SocketHelper.class.getSimpleName();
    //volatile防止指令重排序，内存可见(缓存中的变化及时刷到主存，并且其他的线程副本内存失效，必须从主存获取)，但不保证原子性
    private static volatile SocketHelper singleton = null;
    //主线程的Handler
    private final Handler mainHandler;
    //上次没处理完的缓存的包信息
    private String tempPackage = "";
    //消息id自增,
    private int msgid;
    //发来的单号
    private int payNumber;

    /**
     * 改为对应byte的枚举，是为了给传递对应的错误码
     */
    public enum ERROR_CODE {

        DATA_MD5_ERROR(1, "json数据校验md5不通过"),
        DATA_VALID_LENGTH_ERROR(2, "数据有效位与包定义不符合"),
        SEP_NUMBER_ERROR(3, "分隔符个数不对"),
        DATA_LENGTH_NO_DIGITAL(4, "包长度不是纯数字");

        private int errcode;
        private String errmsg;

        ERROR_CODE(int errcode, String errmsg) {
            this.errcode = errcode;
            this.errmsg = errmsg;
        }

        /**
         * 错误码
         *
         * @return int 错误码
         */
        public int getErroCode() {
            return this.errcode;
        }

        /**
         * 返回错误信息
         *
         * @return String
         */
        public String getErrmsg() {
            return this.errmsg;
        }
    }

    /**
     * 获取单例
     *
     * @return 单例
     */
    public static SocketHelper getInstance() {
        if (singleton == null) {
            synchronized (SocketHelper.class) {
                if (singleton == null) {
                    singleton = new SocketHelper();
                }
            }
        }
        return singleton;
    }

    /**
     * 构造函数
     */
    private SocketHelper() {
        //如果已存在，直接抛出异常，保证只会被new 一次，解决反射破坏单例的方案
        if (singleton != null) {
            throw new RuntimeException("对象已存在不可重复创建");
        }
        //拿到主线程
        mainHandler = new Handler(Looper.getMainLooper());
        //拿到原始数据，这里可以做解包操作
        EasySocket.getInstance().setOnRawDataListener(buff -> {
            onRawDataNext(buff);
            //处理粘包和分包现象
            handleBuffer(buff);
        });
        //连接状态
        EasySocket.getInstance().setOnConnectListener(success -> onConnectNext(success));
    }

    //解决被序列化破坏单例的问题
    private Object readResolve() {
        return singleton;
    }

    /**
     * 启动服务
     */
    public void start() {
        EasySocket.getInstance().start();
    }

    /**
     * 关闭服务
     */
    public void stop() {
        EasySocket.getInstance().stop();
    }

    /**
     * 发送用户信息数据
     *
     * @param sendUserInfoBean 支付成功
     * @param onResultListener 结果回调
     */
    public void sendUserInfo(SendUserInfoBean sendUserInfoBean, OnResultListener onResultListener) {
        send(Constant.HEADER_D, msgid++, sendUserInfoBean.toJsonString(), onResultListener);
    }

    /**
     * 最终发送数据的入口
     *
     * @param header 包头
     * @param msgid 消息id
     * @param jsonData json数据
     * @param onResultListener 回调
     */
    private void send(final String header, final int msgid, final String jsonData,
            final OnResultListener onResultListener) {
        new Thread(() -> {
            //机具发给的结构为：WXD56|19|{"action":"cancel"}|767f0c839210383786b371bd10094ea3\r\n
            //消息id
            String msgIdString = String.valueOf(msgid);
            //如果不传就用默认的发来的消息ID
            if (msgid == 0) {
                msgIdString = String.valueOf(payNumber);
            }
            //json字符串的MD5值
            String md5 = getMD5(jsonData);
            //有效数据位
            String dataString =
                    Constant.SEPERATE + msgIdString + Constant.SEPERATE + jsonData + Constant.SEPERATE + md5;
            //有效数据位的长度
            String dataLengthString = String.valueOf(dataString.length());
            //最后的拼接的数据
            String finalString = header + dataLengthString + dataString + Constant.ENDING;
            AppLogUtils.v(TAG, "send:" + finalString);
            //todo 发送数据
            final boolean result = EasySocket.getInstance().send(finalString.getBytes());
            if (onResultListener != null) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        onResultListener.onResult(result, msgid);
                    }
                });
            }
            AppLogUtils.v(TAG, "send result:" + result);
        }).start();
    }

    /**
     * 处理原始数据
     *
     * @param buffer 字节数组
     */
    private void handleBuffer(byte[] buffer) {
        @SuppressLint({"NewApi", "LocalSuppress"})
        String bufferString = new String(buffer, StandardCharsets.UTF_8);
        //每次处理数据都要和上次没处理完的数据拼接，保证粘包或者分包的情况下都能处理
        String joinString = tempPackage + bufferString;
        AppLogUtils.v(TAG, "handleBuffer -- 开始处理全部数据:" + joinString);
        if (!TextUtils.isEmpty(joinString)) {//非空
            //总共有以下三种情况，分别为整包、粘包和分包，分别处理
            //String[] dataArray1 = "hh\r\n".split("\r\n");//整包
            //String[] dataArray2 = "hh\r\nhh".split("\r\n");//粘包
            //String[] dataArray3 = "hh".split("\r\n");//分包
            //通过计算包中包含的分隔符个数来区分这几种情况
            //int number = strCount("hh\r\n","\r\n");
            //没有结尾符号分割字符串，存在分包现象，直接不处理，缓存下来下次一起处理
            if (!joinString.contains(Constant.ENDING)) {
                tempPackage = joinString;
                AppLogUtils.v(TAG, "handleBuffer -- 没有结尾符号分割字符串，存在分包现象，直接不处理，缓存下来下次一起处理:" + tempPackage);
            } else {
                //用分隔符切出包
                String[] packageArray = joinString.split(Constant.ENDING);
                //算出总共有几个分隔符
                int endingNumber = strCount(joinString, Constant.ENDING);
                //个数相等就是整包，或者多个整包也就是粘包
                if (packageArray.length == endingNumber) {
                    //得到整包需要清空缓存，这很重要
                    tempPackage = "";
                    //可能存在多个整包情况
                    for (String packageString : packageArray) {
                        AppLogUtils.v(TAG, "handleBuffer -- 可能存在多个整包情况:" + packageString);
                        handleOnePackage(packageString + Constant.ENDING);
                    }
                } else {//个数不等就是存在不完整粘包或者分包现象，处理前面的，最后访日不完整包缓存到下次处理
                    //前面处理，最后的不完整包缓存到下次处理
                    for (int i = 0; i < packageArray.length; i++) {
                        if (i != (packageArray.length - 1)) {
                            AppLogUtils.v(TAG, "handleBuffer -- 不完整粘包或者分包情况，处理前面的整包:" + packageArray[i]);
                            handleOnePackage(packageArray[i] + Constant.ENDING);
                        } else {
                            AppLogUtils.v(TAG, "handleBuffer -- 最后的不完整包缓存到下次处理:" + packageArray[i]);
                            tempPackage = packageArray[i];
                        }
                    }
                }
            }
        }
    }

    /**
     * 处理一个整包的情况，分包和粘包不在这处理
     *
     * @param onePackage 一个整包
     */
    private void handleOnePackage(String onePackage) {
        //分隔符拆分
        String[] dataArray = onePackage.split("\\" + Constant.SEPERATE);
        if (onePackage.startsWith(Constant.HEADER_U)) {//发来的命令数据
            //格式为：WXULength|MessageId|Version|Json|Md5 \r\n
            if (dataArray.length == 5) {
                String length = dataArray[0].replace(Constant.HEADER_U, "");
                String messageId = dataArray[1];
                String version = dataArray[2];
                String jsonData = dataArray[3];
                String oldMd5 = dataArray[4].replace(Constant.ENDING, "");
                String newMd5 = getMD5(jsonData);
                //判断MD5
                if (!oldMd5.equals(newMd5)) {
                    AppLogUtils.v(TAG, "handleOnePackage -- 数据md5校验不通过-oldMd5：" + oldMd5 + " newMd5：" + newMd5);
                    onErrorListenerNext(ERROR_CODE.DATA_MD5_ERROR.getErroCode(), ERROR_CODE.DATA_MD5_ERROR.getErrmsg(),
                            onePackage);
                    return;
                }
                //判断长度
                if (!TextUtils.isDigitsOnly(length)) {
                    AppLogUtils.v(TAG, "handleOnePackage -- 长度不是纯数字:" + length);
                    onErrorListenerNext(ERROR_CODE.DATA_LENGTH_NO_DIGITAL.getErroCode(),
                            ERROR_CODE.DATA_LENGTH_NO_DIGITAL.getErrmsg(), onePackage);
                    return;
                }
                //有效数据位
                String dataString =
                        Constant.SEPERATE + messageId + Constant.SEPERATE + version + Constant.SEPERATE + jsonData
                                + Constant.SEPERATE + oldMd5;
                if (dataString.length() != Integer.parseInt(length)) {
                    AppLogUtils.v(TAG, "handleOnePackage -- 长度个数不对:" + length + " 计算长度:" + dataString.length());
                    onErrorListenerNext(ERROR_CODE.DATA_VALID_LENGTH_ERROR.getErroCode(),
                            ERROR_CODE.DATA_VALID_LENGTH_ERROR.getErrmsg(), onePackage);
                    return;
                }
                //都没问题的话就在转换json数据
                handleJsonString(Integer.parseInt(messageId), jsonData);
                AppLogUtils.v(TAG, "handleOnePackage -- 解析出json:" + jsonData);
            } else {
                //格式不对
                AppLogUtils.v(TAG, "handleOnePackage -- 分隔符个数不对:" + dataArray.length);
                onErrorListenerNext(ERROR_CODE.SEP_NUMBER_ERROR.getErroCode(), ERROR_CODE.SEP_NUMBER_ERROR.getErrmsg(),
                        onePackage);
            }
        } else if (onePackage.startsWith(Constant.HEADER_C)) {//发来的回复数据
            //格式为：WXCLength|MessageId |Json|Md5 \r\n
            if (dataArray.length == 4) {
                String length = dataArray[0].replace(Constant.HEADER_C, "");
                String messageId = dataArray[1];
                String jsonData = dataArray[2];
                String oldMd5 = dataArray[3].replace(Constant.ENDING, "");
                String newMd5 = getMD5(jsonData);
                //判断MD5
                if (!oldMd5.equals(newMd5)) {
                    AppLogUtils.v(TAG, "handleOnePackage -- 数据md5校验不通过-oldMd5：" + oldMd5 + " newMd5：" + newMd5);
                    onErrorListenerNext(ERROR_CODE.DATA_MD5_ERROR.getErroCode(), ERROR_CODE.DATA_MD5_ERROR.getErrmsg(),
                            onePackage);
                    return;
                }
                //判断长度
                if (!TextUtils.isDigitsOnly(length)) {
                    AppLogUtils.v(TAG, "handleOnePackage -- 长度不是纯数字:" + length);
                    onErrorListenerNext(ERROR_CODE.DATA_LENGTH_NO_DIGITAL.getErroCode(),
                            ERROR_CODE.DATA_LENGTH_NO_DIGITAL.getErrmsg(), onePackage);
                    return;
                }
                //有效数据位
                String dataString =
                        Constant.SEPERATE + messageId + Constant.SEPERATE + jsonData + Constant.SEPERATE + oldMd5;
                if (dataString.length() != Integer.parseInt(length)) {
                    AppLogUtils.v(TAG, "handleOnePackage -- 长度个数不对:" + length + " 计算长度:" + dataString.length());
                    onErrorListenerNext(ERROR_CODE.DATA_VALID_LENGTH_ERROR.getErroCode(),
                            ERROR_CODE.DATA_VALID_LENGTH_ERROR.getErrmsg(), onePackage);
                    return;
                }
                //都没问题的话就在转换json数据
                handleResponseString(Integer.parseInt(messageId), jsonData);
                AppLogUtils.v(TAG, "handleOnePackage -- 解析出json:" + jsonData);
            } else {
                //格式不对
                AppLogUtils.v(TAG, "handleOnePackage -- 分隔符个数不对:" + dataArray.length);
                onErrorListenerNext(ERROR_CODE.SEP_NUMBER_ERROR.getErroCode(), ERROR_CODE.SEP_NUMBER_ERROR.getErrmsg(),
                        onePackage);
            }
        }
    }

    /**
     * 处理从整包中分离出来分json数据
     *
     * @param jsonString json数据
     */
    private void handleJsonString(int msgId, String jsonString) {
        try {
            AppLogUtils.v(TAG, "handleJsonString -- :" + msgId + " , " + jsonString);
            JSONObject jsonObject = new JSONObject(jsonString);
            onHandleData(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
            AppLogUtils.v(TAG, "handleJsonString -- JSONException:" + e.toString());
        }
    }

    /**
     * 处理从整包中分离出来分回复json数据
     *
     * @param jsonString json数据
     */
    private void handleResponseString(int msgid, String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String action = jsonObject.getString("action");
            int errcode = jsonObject.getInt("errcode");
            String errmsg = jsonObject.getString("errmsg");
            onResponseNext(new ResponseBean(action, errcode, errmsg, msgid));
        } catch (JSONException e) {
            e.printStackTrace();
            AppLogUtils.v(TAG, "handleJsonString -- JSONException:" + e.toString());
        }
    }

    /********************** 得到原始数据 *****************/

    private OnRawDataListener onRawDataListener;

    public void setOnRawDataListener(OnRawDataListener onRawDataListener) {
        this.onRawDataListener = onRawDataListener;
    }

    private void onRawDataNext(final byte[] buffer) {
        if (this.onRawDataListener != null) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    onRawDataListener.onData(buffer);
                }
            });
        }
    }

    public interface OnRawDataListener {

        void onData(byte[] buff);
    }

    /********************** 连接结果回调回调 *****************/

    private OnConnectListener onConnectListener;

    public void setOnConnectListener(OnConnectListener onConnectListener) {
        this.onConnectListener = onConnectListener;
    }

    private void onConnectNext(final boolean result) {
        if (this.onConnectListener != null) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    onConnectListener.onConnect(result);
                }
            });
        }
    }

    public interface OnConnectListener {

        void onConnect(boolean success);
    }



    /********************** 回复数据 *****************/

    private OnResponseListener onResponseListener;

    public void setOnResponseListener(OnResponseListener onResponseListener) {
        this.onResponseListener = onResponseListener;
    }

    private void onResponseNext(final ResponseBean responseBean) {
        if (this.onResponseListener != null) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    onResponseListener.onResponse(responseBean);
                }
            });
        }
    }

    public interface OnResponseListener {

        void onResponse(ResponseBean responseBean);
    }

    /********************** 数据回调 *****************/

    private OnHandleDataListener onPaySuccessListener;

    private void setOnHandleDataListener(OnHandleDataListener onPaySuccessListener) {
        this.onPaySuccessListener = onPaySuccessListener;
    }

    private void onHandleData(final JSONObject paySuccessBean) {
        if (this.onPaySuccessListener != null) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    onPaySuccessListener.onHandleData(paySuccessBean);
                }
            });
        }
    }

    public interface OnHandleDataListener {

        void onHandleData(JSONObject paySuccessBean);
    }

    /************************ 错误回调 start *******************/

    //listener全局变量
    private OnErrorListenerListener onErrorListenerListener;

    //供外部调用的set方法
    public void setOnErrorListenerListener(OnErrorListenerListener onErrorListenerListener) {
        this.onErrorListenerListener = onErrorListenerListener;
    }

    //内部调用传递信息到外部
    private void onErrorListenerNext(final int errcode, final String errmsg, final String origin) {
        if (this.onErrorListenerListener != null) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    onErrorListenerListener.onErrorListener(errcode, errmsg, origin);
                }
            });
        }
    }

    //定义listener
    public interface OnErrorListenerListener {

        void onErrorListener(int errcode, String errmsg, String origin);
    }

    /************************ 错误回调 end  *******************/

    /**
     * 找到findByStr在str中出现的次数
     * @param str 源字符串
     * @param findByStr 被查询的字符串
     * @return 返回findByStr在str中出现的次数
     */
    public static int strCount(String str,String findByStr){
        if(TextUtils.isEmpty(str) | TextUtils.isEmpty(findByStr)){
            return 0;
        }
        //原来的字符串的长度
        int origialLength = str.length();
        //将字符串中的指定字符去掉
        str = str.replace(findByStr, "");
        //去掉指定字符的字符串长度
        int newLength = str.length();
        //需要查找的字符串的长度
        int findStrLength = findByStr.length();
        //返回指定字符出现的次数
        return (origialLength - newLength)/findStrLength;
    }


    /**
     * 获取字符串的MD5值，32位
     * @param code 源字符串
     * @return 32位md5
     */
    public static String getMD5(String code) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(code.getBytes("UTF8"));
            byte s[] = m.digest();
            String result = "";
            for (int i = 0; i < s.length; i++) {
                result += Integer.toHexString((0x000000FF & s[i]) | 0xFFFFFF00).substring(6);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
