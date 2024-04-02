
#include "async_file.h"


async_file::async_file() {
    //创建一个异步线程
    async_thread = std::thread(&async_file::async_log_thread, this);
}

async_file::~async_file() {
    //停止刷新
    stopFlush();
}

void async_file::async_log_thread() {
    while (true) {
        std::unique_lock<std::mutex> lck_async_log_thread(async_mtx);
        while (!async_buffer.empty()) {
            flush_buffer* data = async_buffer.back();
            async_buffer.pop_back();
            flush(data);
        }
        if (exit) {
            return;
        }
        async_condition.wait(lck_async_log_thread);
    }
}

ssize_t async_file::flush(flush_buffer* flushBuffer) {
    ssize_t written = 0;
    FILE* log_file = flushBuffer->logFile();
    if(log_file != nullptr && flushBuffer->length() > 0) {
        written = fwrite(flushBuffer->ptr(), flushBuffer->length(), 1, log_file);
        fflush(log_file);
    }
    delete flushBuffer;
    return written;
}

bool async_file::async_flush(flush_buffer* flushBuffer) {
    std::unique_lock<std::mutex> lck_async_flush(async_mtx);
    if (exit) {
        delete flushBuffer;
        return false;
    }
    async_buffer.push_back(flushBuffer);
    async_condition.notify_all();
    return true;
}

void async_file::stopFlush() {
    exit = true;
    async_condition.notify_all();
    async_thread.join();
}
