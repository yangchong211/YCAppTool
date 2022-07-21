#include "quit_signal_hooker.h"
#include "jni_env_manager.h"

#include <jni.h>
#include <sys/utsname.h>
#include <unistd.h>
#include <pthread.h>
#include <android/log.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <linux/prctl.h>
#include <sys/prctl.h>
#include <sys/resource.h>
#include <csignal>
#include <cstdio>
#include <ctime>
#include <csignal>
#include <thread>
#include <memory>
#include <string>
#include <optional>
#include <sstream>
#include <fstream>

#define PROP_VALUE_MAX  92
#define PROP_VERSION_NAME "ro.build.version.sdk"
#define PROP_MODEL_NAME "ro.product.model"
#define TOOL_CLASS_PATH "com/didi/rider/signal_hooker/SigQuitHooker"
#define TOOL_CLASS_RECEIVE_ANR_SIGNAL "onReceiveAnrSignal"
#define TOOL_CLASS_RECEIVE_ANR_SIGNAL_SIGNATURE "()V"
#define TOOL_CLASS_PRINT_LOG "onPrintLog"
#define TOOL_CLASS_PRINT_LOG_SIGNATURE "(Ljava/lang/String;)V"

//using namespace std;

static bool sHandlerInstalled = false;
struct sigaction sOldHandlers;
static std::mutex sHandlerStackMutex;
static sigset_t old_sigSet;

static struct MethodHolderForJNI {
    jclass AnrDetector;
    jmethodID AnrDetector_onReceiveSignal;
    jmethodID AnrDetector_printLog;
} gJ;

void receiveAnrSignal() {
    JNIEnv *env = JniInvocation::getEnv();
    if (!env) {
        return;
    }
    env->CallStaticVoidMethod(gJ.AnrDetector, gJ.AnrDetector_onReceiveSignal);
}

void printLog(jstring message) {
    JNIEnv *env = JniInvocation::getEnv();
    if (!env) {
        return;
    }
    env->CallStaticVoidMethod(gJ.AnrDetector, gJ.AnrDetector_printLog, message);
}

static void *anrCallback(void *arg) {
    receiveAnrSignal();
    return nullptr;
}

void handleSignal(int sig, const siginfo_t *info, void *uc) {
    if (sig == SIGQUIT) {
        pthread_t thd;
        pthread_create(&thd, nullptr, anrCallback, nullptr);
        pthread_detach(thd);
    }
}

void signalHandler(int sig, siginfo_t *info, void *uc) {
    std::unique_lock<std::mutex> lock(sHandlerStackMutex);
    handleSignal(sig, info, uc);
    lock.unlock();
}

static void initSignalHooker(JNIEnv *env, jclass) {
    if (sHandlerInstalled) {
        return;
    }

    sigset_t sigSet;
    sigemptyset(&sigSet);
    sigaddset(&sigSet, SIGQUIT);
    pthread_sigmask(SIG_UNBLOCK, &sigSet, &old_sigSet);

    if (sigaction(SIGQUIT, nullptr, &sOldHandlers) == -1) {
        return;
    }

    struct sigaction sa{};
    sa.sa_sigaction = signalHandler;
    sa.sa_flags = SA_ONSTACK | SA_SIGINFO | SA_RESTART;

    if (sigaction(SIGQUIT, &sa, nullptr) == -1) {
        return;
    }

    sHandlerInstalled = true;
    printLog(env->NewStringUTF("install signal hooker success"));
}

template<typename T, std::size_t sz>
static inline constexpr std::size_t NELEM(const T(&)[sz]) { return sz; }

static const JNINativeMethod ANR_METHODS[] = {
        {"initSignalHooker", "()V", (void *) initSignalHooker},
};

int getApi() {
    char buf[PROP_VALUE_MAX] = {0};
    int len = __system_property_get(PROP_VERSION_NAME, buf);
    if (len <= 0) return 0;
    return atoi(buf);
}

jstring getModel() {
    JNIEnv *env = JniInvocation::getEnv();
    if (!env) {
        return nullptr;
    }
    char value[PROP_VALUE_MAX] = {0};
    int len = __system_property_get(PROP_MODEL_NAME, value);
    if (len <= 0) {
        return env->NewStringUTF("");
    }
    return env->NewStringUTF(value);
}

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *) {
    JniInvocation::init(vm);
    JNIEnv *env;
    if (vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6) != JNI_OK) {
        return -1;
    }
    jclass anrDetectiveCls = env->FindClass(TOOL_CLASS_PATH);
    if (!anrDetectiveCls) {
        return -1;
    }
    gJ.AnrDetector = static_cast<jclass>(env->NewGlobalRef(anrDetectiveCls));
    gJ.AnrDetector_onReceiveSignal = env->GetStaticMethodID(anrDetectiveCls,
                                                            TOOL_CLASS_RECEIVE_ANR_SIGNAL,
                                                            TOOL_CLASS_RECEIVE_ANR_SIGNAL_SIGNATURE);
    gJ.AnrDetector_printLog = env->GetStaticMethodID(anrDetectiveCls,
                                                     TOOL_CLASS_PRINT_LOG,
                                                     TOOL_CLASS_PRINT_LOG_SIGNATURE);
    if (env->RegisterNatives(
            anrDetectiveCls, ANR_METHODS, static_cast<jint>(NELEM(ANR_METHODS))) != 0) {
        return -1;
    }

    env->DeleteLocalRef(anrDetectiveCls);
    return JNI_VERSION_1_6;
}
