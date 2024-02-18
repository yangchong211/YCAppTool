
#ifndef LOG4A_LOGBUFFER_H
#define LOG4A_LOGBUFFER_H

#include <string>
#include <math.h>
#include <unistd.h>
#include <sys/mman.h>
#include <thread>
#include<vector>
#include <mutex>
#include <condition_variable>
#include "AsyncFileFlush.h"
#include "FlushBuffer.h"
#include "LogBufferHeader.h"
#include <zlib.h>

using namespace log_header;

class LogBuffer {
public:
    LogBuffer(char* ptr, size_t capacity);
    ~LogBuffer();

    void initData(char *log_path, size_t log_path_len, bool is_compress);
    size_t length();
    size_t append(const char* log, size_t len);
    void release();
    size_t emptySize();
    char *getLogPath();
    void setAsyncFileFlush(AsyncFileFlush *fileFlush);
    void async_flush();
    void async_flush(AsyncFileFlush *fileFlush);
    void async_flush(AsyncFileFlush *fileFlush, LogBuffer *releaseThis);
    void changeLogPath(char *log_path);

public:
    bool map_buffer = true;

private:
    void clear();
    void setLength(size_t len);
    bool initCompress(bool compress);
    bool openSetLogFile(const char *log_path);

    FILE* log_file = nullptr;
    AsyncFileFlush *fileFlush = nullptr;
    char* const buffer_ptr = nullptr;
    char* data_ptr = nullptr;
    char* write_ptr = nullptr;

    size_t buffer_size = 0;
    std::recursive_mutex log_mtx;

    LogBufferHeader logHeader;
    z_stream zStream;
    bool is_compress = false;

};


#endif //LOG4A_LOGBUFFER_H
