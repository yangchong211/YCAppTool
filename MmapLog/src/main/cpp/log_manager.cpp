#include <jni.h>

#include <sys/stat.h>
#include <fcntl.h>
#include <sstream>
#include <asm-generic/mman-common.h>
#include <sys/mman.h>
#include "log_buffer.h"
#include "log_utils.h"
#include "mmap_helper.h"


//动态申请内存
//new 运算符，为给定类型的变量在运行时分配堆内的内存，这会返回所分配的空间地址。
//delete 运算符，如果不再需要动态分配的内存空间，删除之前由 new 运算符分配的内存。
static async_file *fileFlush = nullptr;

static jlong initNative(JNIEnv *env, jclass type, jstring buffer_path_,
                        jint bufferSize, jstring log_path_, jboolean compress_) {
    //JNIEnv 代表了JNI的环境，只要在本地代码中拿到了JNIEnv和jobject
    //JNI层实现的方法都是通过JNIEnv 指针调用JNI层的方法访问Java虚拟机，进而操作Java对象，这样就能调用Java代码。
    //jobject thiz
    //在AS中自动为我们生成的JNI方法声明都会带一个这样的参数，这个instance就代表Java中native方法声明所在的
    //思考一下，为什么直接使用字符串 buffer_path_ 会出现错误提示？
    const char *buffer_path = env->GetStringUTFChars(buffer_path_, 0);
    const char *log_path = env->GetStringUTFChars(log_path_, 0);
    logPrintD("initNative 方法被调用 \nbuffer_path: %s \nlog_path: %s", buffer_path, log_path);
    //静态转换  static_cast
    //静态转换是将一种数据类型的值强制转换为另一种数据类型的值。
    //静态转换通常用于比较类型相似的对象之间的转换，例如将 int 类型转换为 float 类型。
    //静态转换不进行任何运行时类型检查，因此可能会导致运行时错误。
    //size_t是C++中的一种无符号整数类型，用于表示对象的大小或元素的数量。它通常用于与内存分配、数组索引和循环计数等相关的操作。
    //size_t的大小在不同的平台上可能会有所变化，但它通常被设计为足够大以容纳系统中最大可能的对象大小。在大多数情况下，size_t的大小与unsigned int或unsigned long相同。
    size_t buffer_size = static_cast<size_t>(bufferSize);
    //fcntl.h头文件中的open函数是C语言中用于打开文件的函数。它提供了对文件的访问和操作的功能。
    //pathname：要打开的文件的路径名。
    //flags：打开文件的标志，用于指定文件的打开方式和操作选项。例如，O_RDONLY表示只读打开，O_WRONLY表示只写打开，O_RDWR表示读写打开，O_CREAT表示如果文件不存在则创建文件等。
    //mode：当使用O_CREAT标志创建新文件时，指定文件的权限。它是一个八进制数，例如0644表示文件权限为rw-r--r--。
    int buffer_fd = open(buffer_path, O_RDWR | O_CREAT, S_IRUSR | S_IWUSR | S_IRGRP | S_IROTH);
    if (buffer_fd == -1) {
        //我们使用open函数打开名为"buffer_path"的文件，以只写方式打开，并在需要时创建文件。如果打开文件失败，我们使用perror函数打印错误信息。
        perror("initNative fcntl open file error");
    }
    // buffer 的第一个字节会用于存储日志路径名称长度，后面紧跟日志路径，之后才是日志信息
    logPrintD("initNative 打开文件 buffer_fd: %d", buffer_fd);
    //创建对象
    if (fileFlush == nullptr) {
        fileFlush = new async_file();
    }
    // 加上头占用的大小
    buffer_size = buffer_size + log_buffer_header::calculateHeaderLen(strlen(log_path));
    mmap_helper *mmapHelper = new mmap_helper(fileFlush);
    // 打开MMap
    char *buffer_ptr = mmapHelper->openMMap(buffer_fd, buffer_size);
    logPrintD("initNative openMMap buffer_ptr: %s", buffer_ptr);
    bool map_buffer = true;
    //如果打开 mmap 失败，则降级使用内存缓存
    if (buffer_ptr == nullptr) {
        buffer_ptr = new char[buffer_size];
        map_buffer = false;
        logPrintW("initNative openMMap 如果打开 mmap 失败，则降级使用内存缓存");
    }
    log_buffer *logBuffer = new log_buffer(buffer_ptr, buffer_size);
    logBuffer->setAsyncFileFlush(fileFlush);
    //将buffer内的数据清0， 并写入日志文件路径
    logBuffer->initData((char *) log_path, strlen(log_path), compress_);
    logBuffer->map_buffer = map_buffer;
    /*方式1：native层处理*/
    /*检测是否有异常*/
    jboolean hasException = env->ExceptionCheck();
    if (hasException == JNI_TRUE) {
        logPrintW("initNative openMMap 检测到有异常");
        //打印异常，同Java中的printExceptionStack;
        env->ExceptionDescribe();
        //清除当前异常
        env->ExceptionClear();
        return reinterpret_cast<long>(logBuffer);
    }
    //释放字符串内存
    env->ReleaseStringUTFChars(buffer_path_, buffer_path);
    env->ReleaseStringUTFChars(log_path_, log_path);
    return reinterpret_cast<long>(logBuffer);
}

static void writeNative(JNIEnv *env, jobject instance, jlong ptr, jstring log_) {
    //注意：log_打印出来乱码，需要用GetStringUTFChars进行转化
    const char *log = env->GetStringUTFChars(log_, 0);
    jsize log_len = env->GetStringUTFLength(log_);
    logPrintD("writeNative 方法被调用 log_: %s , log_len: %d", log , log_len);
    //重新解释转换（Reinterpret Cast）
    //重新解释转换将一个数据类型的值重新解释为另一个数据类型的值，通常用于在不同的数据类型之间进行转换。
    //重新解释转换不进行任何类型检查，因此可能会导致未定义的行为。
    //& 运算符访问的地址，它表示了在内存中的一个地址。
    //* 星号是用来指定一个变量是指针
    log_buffer *logBuffer = reinterpret_cast<log_buffer *>(ptr);
    //缓存写不下时异步刷新
    if (log_len >= logBuffer->emptySize()) {
        logPrintD("writeNative 方法被调用 缓存写不下时异步刷新");
        logBuffer->async_flush(fileFlush);
    }
    //append添加
    logBuffer->append(log, (size_t) log_len);
    //释放字符串内存
    env->ReleaseStringUTFChars(log_, log);
}

static void releaseNative(JNIEnv *env, jobject instance, jlong ptr) {
    logPrintI("releaseNative 方法被调用");
    log_buffer *logBuffer = reinterpret_cast<log_buffer *>(ptr);
    //异步刷新
    logBuffer->async_flush(fileFlush, logBuffer);
    //在释放操作中。如何释放对象，针对new动态分配的内存，使用delete释放内存
    if (fileFlush != nullptr) {
        // 释放内存
        delete fileFlush;
    }
    //先调用delete释放new的对象，然后设置空
    fileFlush = nullptr;
}

static void changeLogPathNative(JNIEnv *env, jobject instance, jlong ptr,
                                jstring logFilePath) {
    logPrintI("changeLogPathNative 方法被调用");
    /*
     * 步骤1：定义类的全限定名：const char* str = "java/lang/String"
     * 步骤2：找到类的jclass：env->FindClass()
     * 步骤3：读取类的构造函数：env->GetMethodID(c,"<init>","()V");
     * 步骤4：根据构造函数创建一个Object对象：env->NewObject(c,constructMethod);
     * 步骤5：调用对象的字段和方法：
     * */
    const char *log_path = env->GetStringUTFChars(logFilePath, 0);
    log_buffer *logBuffer = reinterpret_cast<log_buffer *>(ptr);
    logBuffer->changeLogPath(const_cast<char *>(log_path));
    env->ReleaseStringUTFChars(logFilePath, log_path);
}

static void flushAsyncNative(JNIEnv *env, jobject instance, jlong ptr) {
    logPrintI("flushAsyncNative 方法被调用");
    log_buffer *logBuffer = reinterpret_cast<log_buffer *>(ptr);
    //刷新
    logBuffer->async_flush(fileFlush);
}

//JNI动态注册案例学习
//动态注册其实就是使用到了前面分析的so加载原理：在最后一步的JNI_OnLoad中注册对应的jni方法。这样在类加载的过程中就可以自动注册native函数。
/**
 * 需要动态注册的方法
 * 第一个参数：java中要注册的native方法名
 * 第二个参数：方法的签名，括号内为参数类型，后面为返回类型
 * 第三个参数：需要重新注册的方法名
 */
//研究下JNINativeMethod:
//JNI允许我们提供一个函数映射表，注册给Java虚拟机，这样JVM就可以用函数映射表来调用相应的函数。
//这样就可以不必通过函数名来查找需要调用的函数了。
//Java与JNI通过JNINativeMethod的结构来建立联系，它被定义在jni.h中，其结构内容如下：
//typedef struct {
//    const char* name;
//    const char* signature;
//    void* fnPtr;
//} JNINativeMethod;
static JNINativeMethod gMethods[] = {
        {"initNative",          "(Ljava/lang/String;ILjava/lang/String;Z)J", (void *) initNative},
        {"writeNative",         "(JLjava/lang/String;)V",                    (void *) writeNative},
        {"flushAsyncNative",    "(J)V",                                      (void *) flushAsyncNative},
        {"changeLogPathNative", "(JLjava/lang/String;)V",                    (void *) changeLogPathNative},
        {"releaseNative",       "(J)V",                                      (void *) releaseNative}
};


//java路径
#define JNI_CLASS_NAME "com/yc/mmaplog/LogNativeHelper"


int register_dynamic_Methods(JNIEnv *env) {
    std::string s = JNI_CLASS_NAME;
    const char *className = s.c_str();
    // 找到需要动态注册的java类
    jclass clazz = env->FindClass(className);
    //如何打印class
    //logPrintD("JNI_OnLoad clazz: %s" ,clazz);
    if (clazz == NULL) {
        return JNI_FALSE;
    }
    //sizeof运算符返回的是对象在内存中所占用的字节大小，而不是对象的实际值的大小。
    //例如，对于整数类型的变量，sizeof返回的是该类型的字节大小，而不是变量的值的大小。
    //nMethods = 数组的字节大小 / 数组中数据的字节大小
    jint nMethods = sizeof(gMethods) / sizeof(gMethods[0]);
    //注册JNI方法
    //核心方法：RegisterNatives，jni注册native方法。
    //通过调用RegisterNatives函数将注册函数的Java类，以及注册函数的数组，以及个数注册在一起，这样就实现了绑定。
    //参数1：Java对应的类。
    //参数2：JNINativeMethod数组。
    //参数3：JNINativeMethod数组的长度，也就是要注册的方法的个数。
    if (env->RegisterNatives(clazz, gMethods, nMethods) < 0) {
        return JNI_FALSE;
    }
    logPrintD("register_dynamic_Methods 动态注册函数成功");
    return JNI_TRUE;
}



//System.loadLibrary()执行时会调用此方法
//类加载时会调用到这里
extern "C"
JNIEXPORT jint JNICALL
JNI_OnLoad(JavaVM *vm, void *reserved) {
    //JNIEnv是什么？
    //JNIEnv代表Java调用native层的环境，一个封装了几乎所有的JNI方法的指针。其只在创建它的线程有效，不能跨线程传递，不同的线程的JNIEnv彼此独立。
    //native 环境中创建的线程，如果需要访问JNI，必须调用AttachCurrentThread 进行关联，然后使用DetachCurrentThread 解除关联。
    logPrintD("JNI_OnLoad 方法被调用");
    JNIEnv *env = NULL;
    //指定JNI版本：告诉VM该组件使用那一个JNI版本(若未提供JNI_OnLoad()函数，VM会默认该使用最老的JNI 1.1版)，
    //如果要使用新版本的JNI，例如JNI 1.6版，则必须由JNI_OnLoad()函数返回常量JNI_VERSION_1_6(该常量定义在jni.h中) 来告知VM。
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_4) != JNI_OK) {
        return JNI_FALSE;
    }
    assert(env != NULL);
    if (!register_dynamic_Methods(env)) {
        logPrintD("register_dynamic_Methods 动态注册函数异常");
        return JNI_ERR;
    }
    // 返回JNI使用的版本
    return JNI_VERSION_1_4;
}
