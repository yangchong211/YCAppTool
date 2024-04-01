//
// Created by 杨充 on 2024/3/28.
//


#include "mmap_helper.h"


static async_file *fileFlush = nullptr;

mmap_helper::mmap_helper(async_file *pFlush) {
    //传递一个引用
    fileFlush = pFlush;
}

mmap_helper::~mmap_helper() {

}

char *mmap_helper::openMMap(int buffer_fd, unsigned int buffer_size) {
    logPrintD("openMMap 方法被调用 buffer_fd: %d ，buffer_size: %d", buffer_fd, buffer_size);
    char *map_ptr = nullptr;
    if (buffer_fd != -1) {
        //写数据
        writeDirtyLogToFile(buffer_fd);
        //根据 buffer size 调整 buffer 文件大小。ftruncate函数用于调整文件的大小。
        //fd：文件描述符，表示要调整大小的文件。
        //length：新的文件大小，以字节为单位。
        int code = ftruncate(buffer_fd, static_cast<int>(buffer_size));
        //ftruncate函数返回一个整数值，表示操作的成功与否。如果调整文件大小成功，返回值为0；如果失败，返回值为-1，并且可以通过errno变量获取错误码。
        logPrintD("openMMap 方法被调用 ftruncate返回值: %d", code);
        //unistd.h头文件中的lseek函数是C语言中用于在文件中定位读写位置的函数。它可以用于在文件中移动读写指针的位置。
        //fd：文件描述符，表示要进行定位的文件。
        //offset：偏移量，表示要移动的字节数。正值表示向文件末尾方向移动，负值表示向文件开头方向移动。
        //whence：定位的起始位置。可以是以下值之一：
        //SEEK_SET：从文件开头开始计算偏移量。
        //SEEK_CUR：从当前读写位置开始计算偏移量。
        //SEEK_END：从文件末尾开始计算偏移量。
        off_t seekCode = lseek(buffer_fd, 0, SEEK_SET);
        //lseek函数返回一个off_t类型的值，表示新的读写位置。如果定位失败，返回值为-1，并且可以通过errno变量获取错误码。
        logPrintD("openMMap 方法被调用 lseek返回值: %ld", seekCode);
        map_ptr = (char *) mmap(0, buffer_size, PROT_WRITE | PROT_READ, MAP_SHARED, buffer_fd, 0);
        if (map_ptr == MAP_FAILED) {
            map_ptr = nullptr;
        }
    }
    return map_ptr;
}

void mmap_helper::writeDirtyLogToFile(int buffer_fd) {
    logPrintI("writeDirtyLogToFile buffer_fd: %d", buffer_fd);
    struct stat fileInfo;
    if (fstat(buffer_fd, &fileInfo) >= 0) {
        size_t buffered_size = static_cast<size_t>(fileInfo.st_size);
        // buffer_size 必须是大于文件头长度的，否则会导致下标溢出
        size_t headerLen = log_buffer_header::calculateHeaderLen(0);
        logPrintI("writeDirtyLogToFile calculateHeaderLen headerLen: %d , buffered_size: %d", headerLen , buffered_size);
        if (buffered_size > headerLen) {
            char *buffer_ptr_tmp = (char *) mmap(0, buffered_size, PROT_WRITE | PROT_READ,
                                                 MAP_SHARED, buffer_fd, 0);
            if (buffer_ptr_tmp != MAP_FAILED) {
                log_buffer *tmp = new log_buffer(buffer_ptr_tmp, buffered_size);
                size_t data_size = tmp->length();
                logPrintI("writeDirtyLogToFile data_size : %zu", data_size);
                if (data_size > 0) {
                    tmp->async_flush(fileFlush, tmp);
                } else {
                    delete tmp;
                }
            }
        }
    }
}
