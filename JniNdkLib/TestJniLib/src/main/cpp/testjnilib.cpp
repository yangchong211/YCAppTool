#include <jni.h>
#include <string>
#include "testjnilib.h"

//java中stringFromJNI
//extern “C”    指定以"C"的方式来实现native函数
extern "C"
//JNIEXPORT     宏定义，用于指定该函数是JNI函数。表示此函数可以被外部调用，在Android开发中不可省略
JNIEXPORT jstring
//JNICALL       宏定义，用于指定该函数是JNI函数。，无实际意义，但是不可省略
JNICALL
//以注意到jni的取名规则，一般都是包名 + 类名，jni方法只是在前面加上了Java_，并把包名和类名之间的.换成了_
Java_com_yc_testjnilib_NativeLib_stringFromJNI(JNIEnv *env, jobject /* this */) {
    //JNIEnv 代表了JNI的环境，只要在本地代码中拿到了JNIEnv和jobject
    //JNI层实现的方法都是通过JNIEnv 指针调用JNI层的方法访问Java虚拟机，进而操作Java对象，这样就能调用Java代码。
    //jobject thiz
    //在AS中自动为我们生成的JNI方法声明都会带一个这样的参数，这个instance就代表Java中native方法声明所在的
    std::string hello = "Hello from C++";

    //思考一下，为什么直接返回字符串会出现错误提示？
    //return "hello";
    return env->NewStringUTF(hello.c_str());
}


extern "C"
JNIEXPORT jstring JNICALL
Java_com_yc_testjnilib_NativeLib_getMd5(JNIEnv *env, jobject thiz, jstring str) {
    std::string stringHello = "哈哈哈哈哈，逗比";
    return env->NewStringUTF(stringHello.c_str());
}


extern "C"
JNIEXPORT void JNICALL
Java_com_yc_testjnilib_NativeLib_initLib(JNIEnv *env, jobject thiz, jstring version) {
    printf("初始化: 初始化操作2", version);
}


jstring getNameFromJNI(JNIEnv *env, jobject /* this */) {
    std::string hello = "Hello from C++ , yc lov txy";
    return env->NewStringUTF(hello.c_str());
}


//JNIEnv是什么？
//JNIEnv代表Java调用native层的环境，一个封装了几乎所有的JNI方法的指针。其只在创建它的线程有效，不能跨线程传递，不同的线程的JNIEnv彼此独立。
//native 环境中创建的线程，如果需要访问JNI，必须调用AttachCurrentThread 进行关联，然后使用DetachCurrentThread 解除关联。


//JNI动态注册案例学习
//动态注册其实就是使用到了前面分析的so加载原理：在最后一步的JNI_OnLoad中注册对应的jni方法。这样在类加载的过程中就可以自动注册native函数。
//java路径
#define JNI_CLASS_NAME "com/yc/testjnilib/NativeLib"
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
        {"stringFromJNI",  "()Ljava/lang/String;", (void *) Java_com_yc_testjnilib_NativeLib_stringFromJNI},
        {"getNameFromJNI", "()Ljava/lang/String;", (void *) getNameFromJNI},
};

int register_dynamic_Methods(JNIEnv *env) {
    std::string s = JNI_CLASS_NAME;
    const char *className = s.c_str();
    // 找到需要动态注册的java类
    jclass clazz = env->FindClass(className);
    if (clazz == NULL) {
        return JNI_FALSE;
    }
    //注册JNI方法
    //核心方法：RegisterNatives，jni注册native方法。
    //参数1：Java对应的类。
    //参数2：JNINativeMethod数组。
    //参数3：JNINativeMethod数组的长度，也就是要注册的方法的个数。
    //通过调用RegisterNatives函数将注册函数的Java类，以及注册函数的数组，以及个数注册在一起，这样就实现了绑定。
    if (env->RegisterNatives(clazz, gMethods, sizeof(gMethods) / sizeof(gMethods[0])) < 0) {
        return JNI_FALSE;
    }
    return JNI_TRUE;
}

//System.loadLibrary()执行时会调用此方法
extern "C"
//类加载时会调用到这里
JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env = NULL;
    //指定JNI版本：告诉VM该组件使用那一个JNI版本(若未提供JNI_OnLoad()函数，VM会默认该使用最老的JNI 1.1版)，
    //如果要使用新版本的JNI，例如JNI 1.6版，则必须由JNI_OnLoad()函数返回常量JNI_VERSION_1_6(该常量定义在jni.h中) 来告知VM。
    if (vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }
    assert(env != NULL);
    if (!register_dynamic_Methods(env)) {
        return JNI_ERR;
    }
    // 返回JNI使用的版本
    return JNI_VERSION_1_6;
}


//JNI_OnUnload()的作用与JNI_OnLoad()对应，当VM释放JNI组件时会呼叫它，因此在该方法中进行善后清理，资源释放的动作最为合适。
extern "C"
JNIEXPORT void JNI_OnUnload(JavaVM *jvm, void *reserved) {
    JNIEnv *env = NULL;
    if (jvm->GetEnv((void **) (&env), JNI_VERSION_1_6) != JNI_OK) {
        return;
    }
    std::string s = JNI_CLASS_NAME;
    const char *className = s.c_str();
    // 找到需要动态注册的java类
    jclass clazz = env->FindClass(className);
    if (clazz != NULL) {
        env->UnregisterNatives(clazz);
    }
}
