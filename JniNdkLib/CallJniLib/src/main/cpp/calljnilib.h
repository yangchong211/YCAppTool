#include <jni.h>


//
// Created by 杨充 on 2023/6/7.
//
//约定俗成的操作是 .h 文件主要负责类成员变量和方法的声明； .cpp 文件主要负责成员变量和方法的定义。

//ida打开显示为：assume cs:yc
#define JNI_SECTION ".yc"

//#ifndef YCJNIHELPER_TESTJNILIB_H
//#define YCJNIHELPER_TESTJNILIB_H
//
//#endif //YCJNIHELPER_TESTJNILIB_H

//__cplusplus这个宏是微软自定义宏，表示是C++编译
//两个一样的函数，在c在函数是通过函数名来识别的，而在C++中，由于存在函数的重载问题，函数的识别方式通函数名、函数的返回类型、函数参数列表三者组合来完成的。
//因此上面两个相同的函数，经过C，C++编绎后会产生完全不同的名字。所以，如果把一个用c编绎器编绎的目标代码和一个用C++编绎器编绎的目标代码进行连接，就会出现连接失败的错误。
//extern "C" 是为了避免C++编绎器按照C++的方式去编绎C函数，
#ifdef __cplusplus
//注意：此处对引入的是函数使用了extern "C"对方法进行了包裹，目的就是为了当引用的是cpp文件，extern "C"修饰的函数可以让外部访问到。
extern "C" {
#endif

void callJavaField(JNIEnv* env,jobject obj,jstring className,jstring fieldName);
jboolean callJavaMethod(JNIEnv* env,jobject obj1,jstring className,jstring methodName);

#ifdef __cplusplus
}
#endif