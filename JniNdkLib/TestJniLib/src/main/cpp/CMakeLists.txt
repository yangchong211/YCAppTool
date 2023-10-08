# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

# 最低版本要求
# cmake_minimum_required：要求的Cmake最低版本
cmake_minimum_required(VERSION 3.10.2)

# Declares and names the project.

project("testjnilib")

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

# add_library：添加项目so库，就是你需要创建的目标库声明处
add_library( # Sets the name of the library.
        testjnilib

        # #设置为so共享库，如果需要创建静态库.a文件，需要声明为STAIC
        # Sets the library as a shared library.
        SHARED

        #需要参与编译的src文件，如果有多个，则使用空格或者换行包括进来
        # Provides a relative path to your source file(s).
        # 这个是程序入口
        testjnilib.cpp
        testjnilib.h)

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

# find_library：查找第三方库，这个在某个库需要关联其他库进行连接的时候就需要查找到库
find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

# target_link_libraries：将第三方库链接到目标库中，使用find_library找到第三方库之后，使用target_link_libraries某个库就可以在链接时找到第三方库。
target_link_libraries( # Specifies the target library.
        testjnilib

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})