//
// Created by 杨充 on 2023/6/16.
//

#include "calljnilib.h"
#include <jni.h>
#include <string>
#include <assert.h>
#include <android/log.h>
#include <string.h>

using namespace std;
//java路径
#define JNI_CLASS_NAME "com/yc/calljni/CallNativeLib"
static const char* TAG ="CallJniLib";
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, TAG, fmt, ##args)
#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, TAG, fmt, ##args)

void callJavaField(JNIEnv* env,jobject obj,jstring className,jstring fieldName){
    jboolean iscopy;
    const char* name = env->GetStringUTFChars(fieldName,&iscopy);
    LOGD("invoke method:%s",name);
    /*
     * 步骤1：定义类的全限定名：const char* str = "java/lang/String"
     * 步骤2：找到类的jclass：env->FindClass()
     * 步骤3：读取类的构造函数：env->GetMethodID(c,"<init>","()V");
     * 步骤4：根据构造函数创建一个Object对象：env->NewObject(c,constructMethod);
     * 步骤5：调用对象的字段和方法：
     * */
    //步骤1：定义类的全限定名
    const char* classNameStr = env->GetStringUTFChars(className,&iscopy);
    //步骤2：找到类的jclass
    jclass c = env->FindClass(classNameStr);
    //步骤3：读取类的构造函数
    jmethodID constructMethod = env->GetMethodID(c,"<init>","()V");
    //步骤4：根据构造函数创建一个Object对象
    jobject objCallBack = env->NewObject(c,constructMethod);

    //步骤5：调用对象的字段和方法，需要先获取类的字段id和方法id
    /*
     * 获取字段id
     * 参数1：class
     * 参数2：字段名称
     * 参数3：字段签名格式
     * */
    jboolean isCopy;
    const char* _fieldName = env->GetStringUTFChars(fieldName,&isCopy);

    /*
    * 此处如果传入一个找不到的字段会报错，如果不做异常处理，应用直接会崩溃，为了更好的知晓问题所在，需要        * 使用jni异常处理机制
     * 此处是native异常，有两种异常处理机制：
     * 方式1：native层处理
     * 方式2：抛出给Java层处理
     * */
    jfieldID field_Name = env->GetFieldID(c,_fieldName,"Ljava/lang/String;");

    /*方式1：native层处理*/
    /*检测是否有异常*/
    jboolean hasException = env->ExceptionCheck();
    if(hasException == JNI_TRUE){
        //打印异常，同Java中的printExceptionStack;
        env->ExceptionDescribe();
        //清除当前异常
        env->ExceptionClear();

        //方式2：抛出异常给上面，让Java层去捕获
        jclass noFieldClass = env->FindClass("java/lang/Exception");
        std::string msg(_fieldName);
        std::string header = "找不到该字段";
        env->ThrowNew(noFieldClass,header.append(msg).c_str());
        env->ReleaseStringUTFChars(fieldName,_fieldName);
        return;
    }
    //没有异常去获取字段的值
    jstring fieldObj = static_cast<jstring>(env->GetObjectField(objCallBack, field_Name));
    const char* fieldC = env->GetStringUTFChars(fieldObj,&isCopy);
    LOGD("你成功获取了字段%s值:%s",_fieldName,fieldC);
    env->ReleaseStringUTFChars(fieldObj,fieldC);
}
jboolean callJavaMethod(JNIEnv* env,jobject obj1,jstring className,jstring methodName){
    /*
     * 1.找到类：FindClass
     * 2.创建一个对象
     * 3.获取这个类对应的方法id
     * 4.通过对象和方法id调用对应方法
     * 5.释放内存
     * */
    jboolean isCopy;
    const char* classNameStr = env->GetStringUTFChars(className,&isCopy);
    //1.找到类：FindClass
    jclass callbackClass = env->FindClass(classNameStr);
    //获取构造函数
    jmethodID constructMethod = env->GetMethodID(callbackClass,"<init>","()V");
    //2.创建一个对象
    jobject objCallBack = env->NewObject(callbackClass,constructMethod);

    const char* _methodName = env->GetStringUTFChars(methodName,&isCopy);
    //3.获取这个类对应的方法id
    jmethodID _jmethodName = env->GetMethodID(callbackClass,_methodName,"(Ljava/lang/String;)V");
    const char *str = "123";
    /*切记JNI返回类型不能直接使用基础类型，而要用jni语法中定义的类型：如String需要转换为jstring
     * 不然会报错：JNI DETECTED ERROR IN APPLICATION: use of deleted global reference*/
    jstring result = env->NewStringUTF(str);
    //4.通过对象和方法id调用对应方法
    env->CallVoidMethod(objCallBack,_jmethodName,result);

    if(env->ExceptionCheck()){
        env->ExceptionDescribe();
        env->ExceptionClear();
    }
    //释放字符串内存
    env->ReleaseStringUTFChars(methodName,_methodName);
    env->ReleaseStringUTFChars(className,classNameStr);
    return JNI_TRUE;
}
static JNINativeMethod gMethods[] = {
        {"callJavaField","(Ljava/lang/String;Ljava/lang/String;)V",(void *)callJavaField},
        {"callJavaMethod","(Ljava/lang/String;Ljava/lang/String;)Z",(void *)callJavaMethod},

};
int register_dynamic_Methods(JNIEnv *env){
    std::string s = JNI_CLASS_NAME;
    const char* className = s.c_str();
    jclass clazz = env->FindClass(className);
    if(clazz == NULL){
        return JNI_FALSE;
    }
    //注册JNI方法
    if(env->RegisterNatives(clazz,gMethods,sizeof(gMethods)/sizeof(gMethods[0]))<0){
        return JNI_FALSE;
    }
    return JNI_TRUE;
}
jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env = NULL;
    if(vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6) != JNI_OK){
        return JNI_ERR;
    }
    assert(env != NULL);

    if(!register_dynamic_Methods(env)){
        return JNI_ERR;
    }
    return JNI_VERSION_1_6;
}
