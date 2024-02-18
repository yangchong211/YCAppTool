
#ifndef LOG4A_LOGBUFFERHEADER_H
#define LOG4A_LOGBUFFERHEADER_H

#include "stddef.h"


namespace log_header{
    static const char kMagicHeader = '\x11';

    struct Header {
        char magic;
        size_t log_len;
        size_t log_path_len;
        char* log_path;
        bool isCompress;
    };

    class LogBufferHeader {

    public:
        LogBufferHeader(void* data, size_t size);
        ~LogBufferHeader();
        void initHeader(Header& header);
        void* originPtr();
        void* ptr();
        void* write_ptr();
        Header* getHeader();
        size_t getHeaderLen();
        size_t getLogLen();
        size_t getLogPathLen();
        char* getLogPath();
        void setLogLen(size_t log_len);
        bool getIsCompress();
        bool isAvailable();

        static size_t calculateHeaderLen(size_t log_path_len);

    private:
        char* data_ptr;
        size_t  data_size;
    };
}


#endif //LOG4A_LOGBUFFERHEADER_H
