package com.ns.yc.lifehelper.api.httpUtils.callback;


import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.ns.yc.lifehelper.api.httpUtils.httpBean.ResEntity;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.HttpException;
import retrofit2.Response;

public abstract class SimpleCallBack<T> extends BaseCallBack<ResEntity<T>> {

    @Override
    void dispatchHttpSuccess(Call<ResEntity<T>> call, Response<ResEntity<T>> response) {
        if (response.body() != null && response.body().code == ResEntity.CODE_SUCCESS) {
            //业务层的成功
            onSuccess(call, response);
        } else {
            //业务层的失败
            onHttpFailure(call, response);
        }
    }

    public static class JSONResponseException extends RuntimeException {

        public final int code;
        public final String message;

        public JSONResponseException(int code, String message) {
            this.code = code;
            this.message = message;
        }

    }

    //抽象方法，子类去实现
    abstract void onSuccess(Call<ResEntity<T>> call, Response<ResEntity<T>> response);

    private void onHttpFailure(Call<ResEntity<T>> call, Response<ResEntity<T>> response){
        if(response.body().code==ResEntity.CODE_SHOW_TOAST){
            LogUtils.e("业务层失败"+response.body().message);
        }
    }


    @Override
    public void onFailure(Call<ResEntity<T>> call, Throwable t) {
        super.onFailure(call, t);
        if (t instanceof JSONResponseException) {
            JSONResponseException jsonResponseException = (JSONResponseException) t;
            defNotify(jsonResponseException.message);
            //defNotify(String.format("%s:%s", jsonResponseException.code, jsonResponseException.message));
            switch (jsonResponseException.code) {
                //重新登陆
                case ResEntity.CODE_TOKEN_INVALID:

                    break;
            }
        } else if (t instanceof HttpException) {
            if(((HttpException) t).code()== ResEntity.CODE_TOKEN_INVALID){//重新登录
                try {
                    String msg = ((HttpException) t).response().toString();
                    Gson gson=new Gson();
                    ResEntity resEntity =  gson.fromJson(msg,ResEntity.class);
                    if(resEntity!=null && !TextUtils.isEmpty(resEntity.message)){
                        defNotify(resEntity.message);
                    }else{
                        defNotify("请重新登录");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    defNotify("请重新登录");
                }
            }else if(((HttpException) t).code()== ResEntity.CODE_NO_MISSING_PARAMETER){//缺少参数

                try {
                    String msg = ((HttpException) t).response().toString();
                    if(TextUtils.isEmpty(msg)){
                        defNotify("未知异常");
                        return;
                    }
                    Gson gson=new Gson();
                    ResEntity resEntity =  gson.fromJson(msg,ResEntity.class);
                    if(resEntity!=null && !TextUtils.isEmpty(resEntity.message)){
                        defNotify(resEntity.message);
                    }else{
                        defNotify("未知异常");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    defNotify("未知异常");
                }

            }else if(((HttpException) t).code()== ResEntity.CODE_NO_OTHER){//其他情况
                String json = ((HttpException) t).response().toString();
                if(TextUtils.isEmpty(json)){
                    defNotify("未知异常");
                    return;
                }
                try {
                    Gson gson=new Gson();
                    ResEntity resEntity =  gson.fromJson(json,ResEntity.class);
                    if(resEntity!=null && !TextUtils.isEmpty(resEntity.message)){
                        defNotify(resEntity.message);
                    }else{
                        defNotify("未知异常");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    defNotify("未知异常");
                }

                //相应逻辑处理
            }else{
                defNotify(String.format("%s:%s", ((HttpException) t).code(), ((HttpException) t).message()));
            }

        } else if (t instanceof JsonParseException) {
            defNotify("解析异常");
        } else if (t instanceof java.net.UnknownHostException) {
            defNotify("网络未连接");
        } else if (t instanceof SocketTimeoutException) {
            defNotify("服务器响应超时");
        } else {
            defNotify("未知异常");
        }
        LogUtils.d("http", "------->throwable:" + t+"-->"+call.request().toString());
    }

    /**
     * 出现异常
     * @param string            字符串
     */
    private void defNotify(String string) {
        LogUtils.e("业务层失败"+string);
    }

}
