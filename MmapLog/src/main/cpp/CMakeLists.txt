# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.10.2)


project("log_manager")

# add_library：添加项目so库，就是你需要创建的目标库声明处
add_library(
        # crash_dumper 就是指定库的名称
        log_manager
        # SHARED就是指定生成库的类型：动态|静态；
        SHARED
        # 需要参与编译的src文件，如果有多个，则使用空格或者换行包括进来
        # 这个是程序入口
        log_manager.cpp
        log_buffer.cpp
        log_buffer_header.cpp
        flush_buffer.cpp
        async_file.cpp
        log_utils.cpp
        mmap_helper.cpp
)

find_library( # Sets the name of the path variable.
        log-lib
        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
        log_manager
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})