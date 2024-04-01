
//#ifndef和#endif是C/C++预处理器指令，用于条件编译（Conditional Compilation）。
//#ifndef是#if not defined的缩写，用于检查一个标识符是否已经被定义。如果该标识符未定义，则执行#ifndef和#endif之间的代码块。
//#endif用于结束条件编译的代码块。
//这两个指令通常与#ifdef（#if defined的缩写）一起使用，用于根据条件选择性地编译代码。
//#ifdef用于检查一个标识符是否已经被定义，如果已定义，则执行#ifdef和#endif之间的代码块。
#ifndef YC_FLUSHBUFFER_H
#define YC_FLUSHBUFFER_H


#include "unistd.h"
#include "string.h"
#include "math.h"
#include "stdio.h"


class flush_buffer {

    public:
        flush_buffer(FILE* log_file, size_t size = 128);
        ~flush_buffer();
        void write(void* data, size_t len);
        void reset();
        size_t length();
        void* ptr();
        FILE* logFile();

    void releaseThis(void *release);

private:
        FILE* log_file = nullptr;
        void* release = nullptr;
        char* data_ptr = nullptr;
        char* write_ptr = nullptr;
        size_t capacity;

        size_t emptySize();

};


#endif //YC_FLUSHBUFFER_H
