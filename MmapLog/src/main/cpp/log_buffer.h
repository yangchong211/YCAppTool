
#ifndef YC_LOGBUFFER_H
#define YC_LOGBUFFER_H

#include <string>
#include <math.h>
#include <unistd.h>
#include <sys/mman.h>
#include <thread>
#include<vector>
#include <mutex>
#include <condition_variable>
#include "async_file.h"
#include "flush_buffer.h"
#include "log_buffer_header.h"
#include <zlib.h>

using namespace log_header;

class log_buffer {
public:
    log_buffer(char* ptr, size_t capacity);
    ~log_buffer();

    void initData(char *log_path, size_t log_path_len, bool is_compress);
    size_t length();
    size_t append(const char* log, size_t len);
    void release();
    size_t emptySize();
    char *getLogPath();
    void setAsyncFileFlush(async_file *fileFlush);
    void async_flush();
    void async_flush(async_file *fileFlush);
    void async_flush(async_file *fileFlush, log_buffer *releaseThis);
    void changeLogPath(char *log_path);

public:
    bool map_buffer = true;

private:
    void clear();
    void setLength(size_t len);
    bool initCompress(bool compress);
    bool openSetLogFile(const char *log_path);

    FILE* log_file = nullptr;
    async_file *fileFlush = nullptr;
    char* const buffer_ptr = nullptr;
    char* data_ptr = nullptr;
    char* write_ptr = nullptr;

    size_t buffer_size = 0;
    std::recursive_mutex log_mtx;

    log_buffer_header logHeader;
    z_stream zStream;
    bool is_compress = false;

};


#endif //YC_LOGBUFFER_H
