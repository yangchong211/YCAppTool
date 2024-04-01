//
// Created by yangchong on 2017/11/23.
//

#ifndef LOG4A_FILEFLUSH_H
#define LOG4A_FILEFLUSH_H

#include "vector"
#include "thread"
#include "mutex"
#include "condition_variable"
#include "unistd.h"
#include "flush_buffer.h"

class async_file {

public:
    async_file();

    ~async_file();

    bool async_flush(flush_buffer *flushBuffer);

    void stopFlush();

private:
    void async_log_thread();
    ssize_t flush(flush_buffer *flushBuffer);
    bool exit = false;
    std::vector<flush_buffer *> async_buffer;
    std::thread async_thread;
    std::condition_variable async_condition;
    std::mutex async_mtx;
};


#endif //LOG4A_FILEFLUSH_H
