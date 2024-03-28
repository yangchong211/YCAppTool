//
// Created by 杨充 on 2024/3/28.
//

#include "log_utils.h"
#include <android/log.h>

// 为了能够使用LOGI等宏，需要定义LOGD, LOGI, LOGW, LOGE宏
#define LOGI(fmt, ...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, fmt, ##__VA_ARGS__)
#define logPrintD(fmt, ...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, fmt, ##__VA_ARGS__)
#define LOGW(fmt, ...) __android_log_print(ANDROID_LOG_WARN, LOG_TAG, fmt, ##__VA_ARGS__)
#define LOGE(fmt, ...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, fmt, ##__VA_ARGS__)



void log_utils::logPrint(const std::string &message) {
    if (isWrite) {
        std::cout << "[yc_log] " << message << std::endl;
    }
}
