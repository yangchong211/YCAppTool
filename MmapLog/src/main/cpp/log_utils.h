//
// Created by 杨充 on 2024/3/28.
//

#ifndef YC_LOG_UTILS_H
#define YC_LOG_UTILS_H

#include "string.h"
#include <iostream>
#include <fstream>
#include <ctime>

#include <android/log.h>
#define LOG_TAG "YcMmapLog" // 这个是自定义的LOG的标识
// 为了能够使用LOGI等宏，需要定义LOGD, LOGI, LOGW, LOGE宏
#define logPrintI(fmt, ...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, fmt, ##__VA_ARGS__)
#define logPrintD(fmt, ...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, fmt, ##__VA_ARGS__)
#define logPrintW(fmt, ...) __android_log_print(ANDROID_LOG_WARN, LOG_TAG, fmt, ##__VA_ARGS__)
#define logPrintE(fmt, ...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, fmt, ##__VA_ARGS__)



//属性与函数声明
class log_utils {
private:
    //私有属性声明
    bool isWrite = true;

protected:
    //子类可见声明

public:
    //公开属性声明
    void logPrint(const std::string &message);
};


#endif //YC_LOG_UTILS_H
