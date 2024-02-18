
#include "LogBuffer.h"

LogBuffer::LogBuffer(char *ptr, size_t buffer_size):
        buffer_ptr(ptr),
        buffer_size(buffer_size),
        logHeader(buffer_ptr, buffer_size) {
    if (logHeader.isAvailable()) {
        data_ptr = (char *) logHeader.ptr();
        write_ptr = (char *) logHeader.write_ptr();
        if(logHeader.getIsCompress()) {
            initCompress(true);
        }
        char* log_path = getLogPath();
        if(log_path != nullptr) {
            openSetLogFile(log_path);
            delete[] log_path;
        }
    }
    memset(&zStream, 0, sizeof(zStream));
}

LogBuffer::~LogBuffer() {
    release();
}

size_t LogBuffer::length() {
    return write_ptr - data_ptr;
}

void LogBuffer::setLength(size_t len) {
    logHeader.setLogLen(len);
}

size_t LogBuffer::append(const char *log, size_t len) {
    std::lock_guard<std::recursive_mutex> lck_append(log_mtx);

    if (length() == 0) {
        initCompress(is_compress);
    }

    size_t freeSize = emptySize();
    size_t writeSize = 0;
    if (is_compress) {
        zStream.avail_in = (uInt)len;
        zStream.next_in = (Bytef*)log;

        zStream.avail_out = (uInt)freeSize;
        zStream.next_out = (Bytef*)write_ptr;

        if (Z_OK != deflate(&zStream, Z_SYNC_FLUSH)) {
            return 0;
        }

        writeSize = freeSize - zStream.avail_out;
    } else {
        writeSize = len <= freeSize ? len : freeSize;
        memcpy(write_ptr, log, writeSize);
    }
    write_ptr += writeSize;
    setLength(length());
    return writeSize;
}

void LogBuffer::setAsyncFileFlush(AsyncFileFlush *_fileFlush) {
    fileFlush = _fileFlush;
}

void LogBuffer::async_flush() {
    async_flush(fileFlush);
}

void LogBuffer::async_flush(AsyncFileFlush *fileFlush) {
    async_flush(fileFlush, nullptr);
}

void LogBuffer::async_flush(AsyncFileFlush *fileFlush, LogBuffer *releaseThis) {
    if(fileFlush == nullptr) {
        if (releaseThis != nullptr) {
            delete releaseThis;
        }
        return;
    }
    std::lock_guard<std::recursive_mutex> lck_clear(log_mtx);
    if (length() > 0) {
        if (is_compress && Z_NULL != zStream.state) {
            deflateEnd(&zStream);
        }
        FlushBuffer* flushBuffer = new FlushBuffer(log_file);
        flushBuffer->write(data_ptr, length());
        flushBuffer->releaseThis(releaseThis);
        clear();
        fileFlush->async_flush(flushBuffer);
    } else if (releaseThis != nullptr) {
        delete releaseThis;
    }
}

void LogBuffer::clear() {
    std::lock_guard<std::recursive_mutex> lck_clear(log_mtx);
    write_ptr = data_ptr;
    memset(write_ptr, '\0', emptySize());
    setLength(length());
}

void LogBuffer::release() {
    std::lock_guard<std::recursive_mutex> lck_release(log_mtx);
    if (is_compress && Z_NULL != zStream.state) {
        deflateEnd(&zStream);
    }
    if(map_buffer) {
        munmap(buffer_ptr, buffer_size);
    } else {
        delete[] buffer_ptr;
    }
    if(log_file != nullptr) {
        fclose(log_file);
    }
}

size_t LogBuffer::emptySize() {
    return buffer_size - (write_ptr - buffer_ptr);
}

void LogBuffer::initData(char *log_path, size_t log_path_len, bool is_compress) {
    std::lock_guard<std::recursive_mutex> lck_release(log_mtx);
    memset(buffer_ptr, '\0', buffer_size);

    log_header::Header header;
    header.magic = kMagicHeader;
    header.log_path_len = log_path_len;
    header.log_path = log_path;
    header.log_len = 0;
    header.isCompress = is_compress;

    logHeader.initHeader(header);
    initCompress(is_compress);

    data_ptr = (char *) logHeader.ptr();
    write_ptr = (char *) logHeader.write_ptr();

    openSetLogFile(log_path);
}

char *LogBuffer::getLogPath() {
    return logHeader.getLogPath();
}

bool LogBuffer::initCompress(bool compress) {
    is_compress = compress;
    if (is_compress) {
        zStream.zalloc = Z_NULL;
        zStream.zfree = Z_NULL;
        zStream.opaque = Z_NULL;
        return Z_OK == deflateInit2(&zStream, Z_BEST_COMPRESSION, Z_DEFLATED, -MAX_WBITS, MAX_MEM_LEVEL, Z_DEFAULT_STRATEGY);
    }
    return false;
}

bool LogBuffer::openSetLogFile(const char *log_path) {
    if (log_path != nullptr) {
        FILE* _file_log = fopen(log_path, "ab+");
        if(_file_log != NULL) {
            log_file = _file_log;
            return true;
        }
    }
    return false;
}

void LogBuffer::changeLogPath(char *log_path) {
    if(log_file != nullptr) {
        async_flush();
    }
    initData(log_path, strlen(log_path), is_compress);
}






