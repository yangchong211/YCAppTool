#include <jni.h>

#include <sys/stat.h>
#include <fcntl.h>
#include <sstream>
#include <asm-generic/mman-common.h>
#include <sys/mman.h>
#include "LogBuffer.h"


static char *openMMap(int buffer_fd, size_t buffer_size);

static void writeDirtyLogToFile(int buffer_fd);

static AsyncFileFlush *fileFlush = nullptr;

static jlong initNative(JNIEnv *env, jclass type, jstring buffer_path_,
                        jint capacity, jstring log_path_, jboolean compress_) {
    //env
    const char *buffer_path = env->GetStringUTFChars(buffer_path_, 0);
    const char *log_path = env->GetStringUTFChars(log_path_, 0);
    size_t buffer_size = static_cast<size_t>(capacity);
    int buffer_fd = open(buffer_path, O_RDWR | O_CREAT, S_IRUSR | S_IWUSR | S_IRGRP | S_IROTH);
    // buffer 的第一个字节会用于存储日志路径名称长度，后面紧跟日志路径，之后才是日志信息
    if (fileFlush == nullptr) {
        fileFlush = new AsyncFileFlush();
    }
    // 加上头占用的大小
    buffer_size = buffer_size + LogBufferHeader::calculateHeaderLen(strlen(log_path));
    char *buffer_ptr = openMMap(buffer_fd, buffer_size);
    bool map_buffer = true;
    //如果打开 mmap 失败，则降级使用内存缓存
    if (buffer_ptr == nullptr) {
        buffer_ptr = new char[buffer_size];
        map_buffer = false;
    }
    LogBuffer *logBuffer = new LogBuffer(buffer_ptr, buffer_size);
    logBuffer->setAsyncFileFlush(fileFlush);
    //将buffer内的数据清0， 并写入日志文件路径
    logBuffer->initData((char *) log_path, strlen(log_path), compress_);
    logBuffer->map_buffer = map_buffer;

    env->ReleaseStringUTFChars(buffer_path_, buffer_path);
    env->ReleaseStringUTFChars(log_path_, log_path);
    return reinterpret_cast<long>(logBuffer);
}

static char *openMMap(int buffer_fd, size_t buffer_size) {
    char *map_ptr = nullptr;
    if (buffer_fd != -1) {
        // 写脏数据
        writeDirtyLogToFile(buffer_fd);
        // 根据 buffer size 调整 buffer 文件大小
        ftruncate(buffer_fd, static_cast<int>(buffer_size));
        lseek(buffer_fd, 0, SEEK_SET);
        map_ptr = (char *) mmap(0, buffer_size, PROT_WRITE | PROT_READ, MAP_SHARED, buffer_fd, 0);
        if (map_ptr == MAP_FAILED) {
            map_ptr = nullptr;
        }
    }
    return map_ptr;
}

static void writeDirtyLogToFile(int buffer_fd) {
    struct stat fileInfo;
    if (fstat(buffer_fd, &fileInfo) >= 0) {
        size_t buffered_size = static_cast<size_t>(fileInfo.st_size);
        // buffer_size 必须是大于文件头长度的，否则会导致下标溢出
        if (buffered_size > LogBufferHeader::calculateHeaderLen(0)) {
            char *buffer_ptr_tmp = (char *) mmap(0, buffered_size, PROT_WRITE | PROT_READ,
                                                 MAP_SHARED, buffer_fd, 0);
            if (buffer_ptr_tmp != MAP_FAILED) {
                LogBuffer *tmp = new LogBuffer(buffer_ptr_tmp, buffered_size);
                size_t data_size = tmp->length();
                if (data_size > 0) {
                    tmp->async_flush(fileFlush, tmp);
                } else {
                    delete tmp;
                }
            }
        }
    }
}

static void writeNative(JNIEnv *env, jobject instance, jlong ptr,
                        jstring log_) {
    const char *log = env->GetStringUTFChars(log_, 0);
    jsize log_len = env->GetStringUTFLength(log_);
    LogBuffer *logBuffer = reinterpret_cast<LogBuffer *>(ptr);
    // 缓存写不下时异步刷新
    if (log_len >= logBuffer->emptySize()) {
        logBuffer->async_flush(fileFlush);
    }
    logBuffer->append(log, (size_t) log_len);
    env->ReleaseStringUTFChars(log_, log);
}

static void releaseNative(JNIEnv *env, jobject instance, jlong ptr) {
    LogBuffer *logBuffer = reinterpret_cast<LogBuffer *>(ptr);
    logBuffer->async_flush(fileFlush, logBuffer);
    if (fileFlush != nullptr) {
        delete fileFlush;
    }
    fileFlush = nullptr;
}

static void changeLogPathNative(JNIEnv *env, jobject instance, jlong ptr,
                                jstring logFilePath) {
    const char *log_path = env->GetStringUTFChars(logFilePath, 0);
    LogBuffer *logBuffer = reinterpret_cast<LogBuffer *>(ptr);
    logBuffer->changeLogPath(const_cast<char *>(log_path));
    env->ReleaseStringUTFChars(logFilePath, log_path);
}

static void flushAsyncNative(JNIEnv *env, jobject instance, jlong ptr) {
    LogBuffer *logBuffer = reinterpret_cast<LogBuffer *>(ptr);
    logBuffer->async_flush(fileFlush);
}

static JNINativeMethod gMethods[] = {

        {
                "initNative",
                "(Ljava/lang/String;ILjava/lang/String;Z)J",
                (void *) initNative
        },

        {
                "writeNative",
                "(JLjava/lang/String;)V",
                (void *) writeNative
        },

        {
                "flushAsyncNative",
                "(J)V",
                (void *) flushAsyncNative
        },

        {
                "changeLogPathNative",
                "(JLjava/lang/String;)V",
                (void *) changeLogPathNative
        },

        {
                "releaseNative",
                "(J)V",
                (void *) releaseNative
        }

};

extern "C"
JNIEXPORT jint JNICALL
JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env = NULL;
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_4) != JNI_OK) {
        return JNI_FALSE;
    }
    jclass classDocScanner = env->FindClass("me/pqpo/librarylog4a/LogBuffer");
    jint nMethods = sizeof(gMethods) / sizeof(gMethods[0]);
    if (env->RegisterNatives(classDocScanner, gMethods, nMethods) < 0) {
        return JNI_FALSE;
    }
    return JNI_VERSION_1_4;
}