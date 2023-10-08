#include <jni.h>
#include <string>

//
// Created by 杨充 on 2023/6/7.
//

jstring ByteToHexStr(JNIEnv *env, const char *string, char *result, jsize length);

jstring ToMd5(JNIEnv *env, jbyteArray source);

jstring loadSignature(JNIEnv *env, jobject context);

extern "C"
JNIEXPORT jstring JNICALL
Java_com_yc_safetyjni_SafetyJniLib_stringFromJNI(JNIEnv *env, jobject thiz) {
    std::string hello = "Hello from C++ , yc";
    return env->NewStringUTF(hello.c_str());
}


extern "C"
JNIEXPORT jstring JNICALL
Java_com_yc_safetyjni_SafetyJniLib_getAppSign(JNIEnv *env, jobject thiz) {
    jclass appClass = env->FindClass("com/yc/App");
    jmethodID getAppContextMethod = env->GetStaticMethodID(appClass, "getContext",
                                                           "()Landroid/content/Context;");
    //获取APplication定义的context实例
    jobject appContext = env->CallStaticObjectMethod(appClass, getAppContextMethod);
    // 获取应用当前的签名信息
    jstring signature = loadSignature(env, appContext);
    // 期待的签名信息
    jstring keystoreSigature = env->NewStringUTF("31BC77F998CB0D305D74464DAECC2");
    const char *keystroreMD5 = env->GetStringUTFChars(keystoreSigature, NULL);
    const char *releaseMD5 = env->GetStringUTFChars(signature, NULL);
    // 比较两个签名信息是否相等
    int result = strcmp(keystroreMD5, releaseMD5);
    // 这里记得释放内存
    env->ReleaseStringUTFChars(signature, releaseMD5);
    env->ReleaseStringUTFChars(keystoreSigature, keystroreMD5);
}

jstring loadSignature(JNIEnv *env, jobject context) {
    // 获取Context类
    jclass contextClass = env->GetObjectClass(context);
    // 得到getPackageManager方法的ID
    jmethodID getPkgManagerMethodId = env->GetMethodID(
            contextClass, "getPackageManager", "()Landroid/content/pm/PackageManager;");
    // PackageManager
    jobject pm = env->CallObjectMethod(context, getPkgManagerMethodId);
    // 得到应用的包名
    jmethodID pkgNameMethodId = env->GetMethodID(
            contextClass, "getPackageName", "()Ljava/lang/String;");
    jstring pkgName = (jstring) env->CallObjectMethod(context, pkgNameMethodId);
    // 获得PackageManager类
    jclass cls = env->GetObjectClass(pm);
    // 得到getPackageInfo方法的ID
    jmethodID mid = env->GetMethodID(
            cls, "getPackageInfo", "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");
    // 获得应用包的信息
    jobject packageInfo = env->CallObjectMethod(pm, mid, pkgName, 0x40); //GET_SIGNATURES = 64;
    // 获得PackageInfo 类
    cls = env->GetObjectClass(packageInfo);
    // 获得签名数组属性的ID
    jfieldID fid = env->GetFieldID(cls, "signatures", "[Landroid/content/pm/Signature;");
    // 得到签名数组
    jobjectArray signatures = (jobjectArray) env->GetObjectField(packageInfo, fid);
    // 得到签名
    jobject signature = env->GetObjectArrayElement(signatures, 0);
    // 获得Signature类
    cls = env->GetObjectClass(signature);
    // 得到toCharsString方法的ID
    mid = env->GetMethodID(cls, "toByteArray", "()[B");
    // 返回当前应用签名信息
    jbyteArray signatureByteArray = (jbyteArray) env->CallObjectMethod(signature, mid);
    return ToMd5(env, signatureByteArray);
}


jstring ToMd5(JNIEnv *env, jbyteArray source) {
    // MessageDigest类
    jclass classMessageDigest = env->FindClass(
            "java/security/MessageDigest");
    // MessageDigest.getInstance()静态方法
    jmethodID midGetInstance = env->GetStaticMethodID(
            classMessageDigest, "getInstance", "(Ljava/lang/String;)Ljava/security/MessageDigest;");
    // MessageDigest object
    jobject objMessageDigest = env->CallStaticObjectMethod(
            classMessageDigest, midGetInstance, env->NewStringUTF("md5"));
    // update方法，这个函数的返回值是void，写V
    jmethodID midUpdate = env->GetMethodID(
            classMessageDigest, "update", "([B)V");
    env->CallVoidMethod(objMessageDigest,
                        midUpdate, source);
    // digest方法
    jmethodID midDigest = env->GetMethodID(classMessageDigest, "digest", "()[B");
    jbyteArray objArraySign = (jbyteArray) env->CallObjectMethod(objMessageDigest, midDigest);
    jsize intArrayLength = env->GetArrayLength(objArraySign);
    jbyte *byte_array_elements = env->GetByteArrayElements(objArraySign, NULL);
    size_t length = (size_t) intArrayLength * 2 + 1;
    char *char_result = (char *) malloc(length);
    memset(char_result, 0, length);
    // 将byte数组转换成16进制字符串，发现这里不用强转，jbyte和unsigned char应该字节数是一样的
    ByteToHexStr(env, (const char *) byte_array_elements, char_result, intArrayLength);
    // 在末尾补\0
    *(char_result + intArrayLength * 2) = '\0';
    jstring stringResult = env->NewStringUTF(char_result);
    // release
    env->ReleaseByteArrayElements(objArraySign, byte_array_elements, JNI_ABORT);
    // 释放指针使用free
    free(char_result);
    return stringResult;
}

jstring ByteToHexStr(JNIEnv *env, const char *bytes, char *result, jsize length) {
    if (bytes == NULL) {
        return env->NewStringUTF("");
    }
    std::string buff;
    const int len = length;
    for (int j = 0; j < len; j++) {
        int high = bytes[j] / 16, low = bytes[j] % 16;
        buff += (high < 10) ? ('0' + high) : ('a' + high - 10);
        buff += (low < 10) ? ('0' + low) : ('a' + low - 10);
    }
    return env->NewStringUTF(buff.c_str());
}
