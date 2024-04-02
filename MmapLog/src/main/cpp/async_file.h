//
// Created by yangchong on 2017/11/23.
//

//#ifndef和#endif是C/C++预处理器指令，用于条件编译（Conditional Compilation）。
//#ifndef是#if not defined的缩写，用于检查一个标识符是否已经被定义。如果该标识符未定义，则执行#ifndef和#endif之间的代码块。
//#endif用于结束条件编译的代码块。
//这两个指令通常与#ifdef（#if defined的缩写）一起使用，用于根据条件选择性地编译代码。
//#ifdef用于检查一个标识符是否已经被定义，如果已定义，则执行#ifdef和#endif之间的代码块。
#ifndef YC_FILEFLUSH_H
#define YC_FILEFLUSH_H

#include "vector"
#include "thread"
#include "mutex"
#include "condition_variable"
#include "unistd.h"
#include "flush_buffer.h"

// 这里是一些代码

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


#endif
