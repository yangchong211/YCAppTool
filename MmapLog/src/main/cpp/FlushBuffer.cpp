
//#include <FlushBuffer.h>
#include "FlushBuffer.h"

FlushBuffer::FlushBuffer(FILE* log_file, size_t size) : capacity(size), log_file(log_file) {}

FlushBuffer::~FlushBuffer() {
    if (data_ptr != nullptr) {
        delete[] data_ptr;
    }
    if (release != nullptr) {
        delete release;
    }
}

size_t FlushBuffer::length() {
    if (data_ptr != nullptr && write_ptr != nullptr) {
        return write_ptr - data_ptr;
    }
    return 0;
}

void *FlushBuffer::ptr() {
    return data_ptr;
}

size_t FlushBuffer::emptySize() {
    return capacity - length();
}

void FlushBuffer::write(void *data, size_t len) {

    if (data_ptr == nullptr) {
        capacity = (size_t)fmax(capacity, len);
        data_ptr = new char[capacity]{0};
        write_ptr = data_ptr;
    }

    size_t empty_size = emptySize();
    if (len < empty_size) {
        memcpy(write_ptr, data, len);
        write_ptr += len;
    } else {
        size_t now_len = length();
        size_t new_capacity = now_len + len;
        char* data_tmp = new char[new_capacity]{0};
        memcpy(data_tmp, data_ptr, now_len);
        memcpy(data_tmp + now_len, data, len);
        char* old_data = data_ptr;
        data_ptr = data_tmp;
        write_ptr = data_ptr + new_capacity;
        delete[] old_data;
    }
}

void FlushBuffer::reset() {
    if (data_ptr != nullptr) {
        memset(data_ptr, 0, capacity);
        write_ptr = data_ptr;
    }
}

FILE *FlushBuffer::logFile() {
    return log_file;
}

void FlushBuffer::releaseThis(void *release) {
    this->release = release;
}


