#ifndef LAG_DETECTOR_LAG_DETECTOR_MAIN_CPP_NATIVE_HELPER_MANAGED_JNIENV_H_
#define LAG_DETECTOR_LAG_DETECTOR_MAIN_CPP_NATIVE_HELPER_MANAGED_JNIENV_H_

#include <jni.h>

namespace JniInvocation {

    void init(JavaVM *vm);

    JavaVM *getJavaVM();

    JNIEnv *getEnv();
}

#endif

