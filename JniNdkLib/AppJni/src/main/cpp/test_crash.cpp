#include <jni.h>
#include <string>


void raiseError(int signal) {
    raise(signal);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_yc_jnidemo_CrashActivity_nativeCrash(JNIEnv *env, jobject thiz) {
    raiseError(SIGABRT);
}