package com.yc.http.config;

import androidx.annotation.NonNull;

import com.yc.http.model.BodyType;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2020/01/01
 *    desc   : 请求接口配置
 */
public interface IRequestType {

    /**
     * 获取参数的提交类型
     */
    @NonNull
    BodyType getBodyType();
}