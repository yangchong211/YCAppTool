//
// Created by 杨充 on 2024/3/28.
//

#ifndef YCAPPTOOL_MMAP_HELPER_H
#define YCAPPTOOL_MMAP_HELPER_H

#include "log_utils.h"
#include "unistd.h"
#include <fcntl.h>
#include <sstream>
#include <sys/mman.h>
#include "log_utils.h"
#include "async_file.h"
#include <sys/stat.h>
#include "log_buffer.h"


class mmap_helper {
public:
    mmap_helper(async_file *pFlush);
    ~mmap_helper();
    char *openMMap(int buffer_fd, unsigned int buffer_size);
    static void writeDirtyLogToFile(int fd);
};


#endif //YCAPPTOOL_MMAP_HELPER_H
