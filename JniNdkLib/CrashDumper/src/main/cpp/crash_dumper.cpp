#include <jni.h>
#include <string>
#include <android/log.h>
#include <fstream>
#include <map>
#include <unistd.h>
#include <unwind.h>
#include <dlfcn.h>
#include "stack_tracer.h"


JavaVM *g_vm;
#define SIGNAL_COUNT 8
static struct sigaction g_oldSigActions[SIGNAL_COUNT] = {0};
char *tombstone_file_path = NULL;
// do not need deleteGlobalRef, because process will be killed, or always listening
jobject g_crashListener = NULL;

/**
 * handleMode masks，refer to Java class: HandleMode
 */
#define HANDLE_MODE_DO_NOTHING 0b00
#define HANDLE_MODE_RAISE_ERROR 0b01
#define HANDLE_MODE_NOTICE_CALLBACK 0b10

static int handleMode = HANDLE_MODE_DO_NOTHING;

/**
 * 代表信号量，可以是除 SIGKILL 和 SIGSTOP 外的任何一个特定有效的信号量，SIGKILL和SIGSTOP既不能被捕捉，也不能被忽略。
 * 同一个信号在不同的系统中值可能不一样，所以建议最好使用为信号定义的名字。
 */
static int signals[SIGNAL_COUNT] = {
        SIGTRAP,
        SIGABRT,
        SIGILL,
        SIGSEGV,
        SIGFPE,
        SIGBUS,
        SIGPIPE,
        SIGSYS
};

static std::map<int, const char *> signal_names = {
        {SIGTRAP, "SIGTRAP"},
        {SIGABRT, "SIGABRT"},
        {SIGILL,  "SIGILL"},
        {SIGSEGV, "SIGSEGV"},
        {SIGFPE,  "SIGFPE"},
        {SIGBUS,  "SIGBUS"},
        {SIGPIPE, "SIGPIPE"},
        {SIGSYS,  "SIGSYS"}
};
static std::map<int, const char *> si_names = {
        {SI_USER,     "SI_USER"},
        {SI_KERNEL,   "SI_KERNEL"},
        {SI_QUEUE,    "SI_QUEUE"},
        {SI_TIMER,    "SI_TIMER"},
        {SI_MESGQ,    "SI_MESGQ"},
        {SI_ASYNCIO,  "SI_ASYNCIO"},
        {SI_SIGIO,    "SI_SIGIO"},
        {SI_TKILL,    "SI_TKILL"},
        {SI_DETHREAD, "SI_DETHREAD"},
        {SI_ASYNCNL,  "SI_ASYNCNL"}
};

void noticeCallback(int signal, std::string logPath);

JNIEnv *attachEnv(bool *handAttached);

void detachEnv(JNIEnv *env, bool byAttach);


std::string getRegisterInfo(const ucontext_t *ucontext);

std::string getDumpFileName() {
    time_t timep;
    time(&timep);
    char tmp[64];
    strftime(tmp, sizeof(tmp), "/%Y-%m-%d-%H-%M-%S.txt", localtime(&timep));
    return tmp;
}

std::string getCrashTimeStamp() {
    time_t timep;
    time(&timep);
    char tmp[64];
    strftime(tmp, sizeof(tmp), "%Y-%m-%d %H:%M:%S%z", localtime(&timep));
    return tmp;
}


const char *getABI() {
    //  It's usually most convenient to determine the ABI at build time using #ifdef in conjunction with:
    //
    //  __arm__ for 32-bit ARM
    //  __aarch64__ for 64-bit ARM
    //  __i386__ for 32-bit X86
    //  __x86_64__ for 64-bit X86
    //  Note that 32-bit X86 is called __i386__, not __x86__ as you might expect!
    //
    //  https://developer.android.google.cn/ndk/guides/cpu-features?hl=en
#if defined(__aarch64__)
    return "arm64";
#elif defined(__arm__)
    return "arm";
#else
    return "unknown";
#endif
}


// 获取header信息
std::string getHeaderInfo() {
    //Build fingerprint
    //读取系统的ro.build.fingerprint字段信息即可
    char finger[92] = {0};
    __system_property_get("ro.build.fingerprint", finger);
    if (strlen(finger) == 0) {
        strcpy(finger, "unknown");
    }

    //revision
    //读取系统的ro.revision字段信息即可
    char revision[92] = {0};
    __system_property_get("ro.revision", revision);
    if (strlen(revision) == 0) {
        strcpy(revision, "unknown");
    }

    //abi
    //我们可以读取so中的内容判断，也可以在编译期就进行判断，这里选择在编译期判断
    const char *abi = getABI();
    char result[256] = {0};


    sprintf(result,
            "Build fingerprint: '%s'\r\nRevision: '%s'\r\nABI: '%s'",
            finger, revision, abi);
    return result;
}


void unregisterSigHandler() {
    for (int i = 0; i < SIGNAL_COUNT; ++i) {
        sigaction(signals[i], &g_oldSigActions[i], NULL);
    }
    memset(g_oldSigActions, 0, sizeof(g_oldSigActions));
}

/**
 * 获取进程名称
 * @param pid           进程号
 * @return
 */
std::string get_process_name_by_pid(const int pid) {
    char processName[256] = {0};
    char cmd[64] = {0};
    sprintf(cmd, "/proc/%d/cmdline", pid);
    FILE *f = fopen(cmd, "r");
    if (f) {
        size_t size;
        size = fread(processName, sizeof(char), 256, f);
        if (size > 0 && '\n' == processName[size - 1]) {
            processName[size - 1] = '\0';
        }
        fclose(f);
    }
    return processName;
}

void sigHandler(int sig, siginfo_t *info, void *context) {
    std::string desc;
    desc.append("*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***\r\n")
            .append(getHeaderInfo()).append("\r\n")
            .append("Timestamp: ").append(getCrashTimeStamp()).append("\r\n")
            .append("pid: ").append(std::to_string(info->_sifields._kill._pid))
            .append(", uid: ").append(std::to_string(info->_sifields._kill._uid))
            .append(", process: >>> ").append(
                    get_process_name_by_pid(info->_sifields._kill._pid)).append(" <<<\r\n")
            .append("signal ").append(std::to_string(sig)).append(" (").append(signal_names[sig])
            .append("), code ").append(std::to_string(info->si_code))
            .append(" (").append(si_names[info->si_code])
            .append("), fault addr --------\r\n")
            .append(getRegisterInfo((const ucontext_t *) context))
            .append("\r\nbacktrace:");

    std::string result;
    storeCallStack(&result);
    std::string path = tombstone_file_path;
    path += getDumpFileName();
    std::ofstream outStream(path.c_str(), std::ios::out);

    if (outStream) {
        const char *logContent = result.c_str();

        outStream.write(desc.c_str(), desc.length());
        outStream.write("\r\n", strlen("\r\n"));
        outStream.write(logContent, result.length());
        outStream.close();
    }

    switch (handleMode) {
        case HANDLE_MODE_RAISE_ERROR:
            unregisterSigHandler();
            raise(sig);
            break;
        case HANDLE_MODE_NOTICE_CALLBACK:
            noticeCallback(sig, path.c_str());
            break;
        case HANDLE_MODE_DO_NOTHING:
        default:
            break;
    }
}

std::string getRegisterInfo(const ucontext_t *ucontext) {
    std::string ctxTxt;
#if defined(__aarch64__)
    for (int i = 0; i < 30; ++i) {
        if (i % 4 == 0) {
            ctxTxt.append("     ");
        }
        char info[64] = {0};
        sprintf(info, "x%d %016lx ", i, ucontext->uc_mcontext.regs[i]);
        ctxTxt.append(info);
        if ((i + 1) % 4 == 0) {
            ctxTxt.append("\r\n");
        }
    }
    ctxTxt.append("\r\n");

    char spInfo[64] = {0};
    sprintf(spInfo, "sp %016lx ", ucontext->uc_mcontext.sp);

    char lrInfo[64] = {0};
    sprintf(lrInfo, "lr %016lx ", ucontext->uc_mcontext.regs[30]);

    char pcInfo[64] = {0};
    sprintf(pcInfo, "pc %016lx ", ucontext->uc_mcontext.pc);

    ctxTxt.append("     ").append(spInfo).append(lrInfo).append(pcInfo);
#elif defined(__arm__)
    char x0Info[64] = {0};
    sprintf(x0Info, "x0 %08lx ", ucontext->uc_mcontext.arm_r0);
    char x1Info[64] = {0};
    sprintf(x1Info, "x1 %08lx ", ucontext->uc_mcontext.arm_r1);
    char x2Info[64] = {0};
    sprintf(x2Info, "x2 %08lx ", ucontext->uc_mcontext.arm_r2);
    char x3Info[64] = {0};
    sprintf(x3Info, "x3 %08lx ", ucontext->uc_mcontext.arm_r3);
    char x4Info[64] = {0};
    sprintf(x4Info, "x4 %08lx ", ucontext->uc_mcontext.arm_r4);
    char x5Info[64] = {0};
    sprintf(x5Info, "x5 %08lx ", ucontext->uc_mcontext.arm_r5);
    char x6Info[64] = {0};
    sprintf(x6Info, "x6 %08lx ", ucontext->uc_mcontext.arm_r6);
    char x7Info[64] = {0};
    sprintf(x7Info, "x7 %08lx ", ucontext->uc_mcontext.arm_r7);
    char x8Info[64] = {0};
    sprintf(x8Info, "x8 %08lx ", ucontext->uc_mcontext.arm_r8);
    char x9Info[64] = {0};
    sprintf(x9Info, "x9 %08lx ", ucontext->uc_mcontext.arm_r9);
    char x10Info[64] = {0};
    sprintf(x10Info, "x10 %08lx ", ucontext->uc_mcontext.arm_r10);

    char ipInfo[64] = {0};
    sprintf(ipInfo, "ip %08lx ", ucontext->uc_mcontext.arm_ip);
    char spInfo[64] = {0};
    sprintf(spInfo, "sp %08lx ", ucontext->uc_mcontext.arm_sp);
    char lrInfo[64] = {0};
    sprintf(lrInfo, "lr %08lx ", ucontext->uc_mcontext.arm_lr);
    char pcInfo[64] = {0};
    sprintf(pcInfo, "pc %08lx ", ucontext->uc_mcontext.arm_pc);

    ctxTxt.append("     ").append(x0Info).append(x1Info).append(x2Info).append(x3Info).append(
                    "\r\n")
            .append("     ").append(x4Info).append(x5Info).append(x6Info).append(x7Info).append(
                    "\r\n")
            .append("     ").append(x8Info).append(x9Info).append(x10Info).append("\r\n")
            .append("     ").append(ipInfo).append(spInfo).append(lrInfo).append(pcInfo);
#endif
    return ctxTxt;
}

/**
 * 注册信号处理函数
 */
void registerSigHandler() {
    // create new actions
    struct sigaction newSigAction;
    sigemptyset(&newSigAction.sa_mask);
    newSigAction.sa_flags = SA_SIGINFO;
    // set custom handle function
    newSigAction.sa_sigaction = sigHandler;

    // set new action for each signal, and save old actions
    memset(g_oldSigActions, 0, sizeof(g_oldSigActions));
    for (int i = 0; i < SIGNAL_COUNT; ++i) {
        //进程通过 sigaction() 函数来指定对某个信号的处理行为。
        //sig：代表信号量，可以是除 SIGKILL 和 SIGSTOP 外的任何一个特定有效的信号量，SIGKILL和SIGSTOP既不能被捕捉，也不能被忽略。同一个信号在不同的系统中值可能不一样，所以建议最好使用为信号定义的名字。
        //new_action：指向结构体 sigaction 的一个实例的指针，该实例指定了对特定信号的处理，如果设置为空，进程会执行默认处理。
        //old_action：和参数 new_action 类似，只不过保存的是原来对相应信号的处理，也可设置为 NULL。
        sigaction(signals[i], &newSigAction, &g_oldSigActions[i]);
    }
}

void nativeInit(JNIEnv *env, jobject clazz,
                jstring crash_dump_dir,
                jobject crash_listener,
                jint handle_mode) {
    if (g_crashListener) {
        env->DeleteGlobalRef(g_crashListener);
        g_crashListener = NULL;
    }
    if (crash_listener) {
        g_crashListener = env->NewGlobalRef(crash_listener);
    }
    handleMode = handle_mode;
    const char *crashDumpDir = env->GetStringUTFChars(crash_dump_dir, JNI_FALSE);


    long strLen = strlen(crashDumpDir);
    if (tombstone_file_path != NULL) {
        free(tombstone_file_path);
    }
    tombstone_file_path = static_cast<char *>(malloc(strLen + 1));
    memset(tombstone_file_path, 0, strLen);
    strcpy(tombstone_file_path, crashDumpDir);
    env->ReleaseStringUTFChars(crash_dump_dir, crashDumpDir);

    //注册信号处理函数
    registerSigHandler();
}


void noticeCallback(int signal, std::string logPath) {
    if (g_crashListener == NULL) {
        return;
    }
    bool byAttach = false;
    JNIEnv *env = attachEnv(&byAttach);
    if (!env) {
        return;
    }
    jclass crashListenerClazz = env->GetObjectClass(g_crashListener);
    jmethodID onSignalReceivedId = env->GetMethodID(crashListenerClazz, "onSignalReceived",
                                                    "(ILjava/lang/String;)V");
    env->CallVoidMethod(g_crashListener, onSignalReceivedId, signal,
                        env->NewStringUTF(logPath.c_str()));
    detachEnv(env, byAttach);
}

JNIEnv *attachEnv(bool *handAttach) {
    JNIEnv *env;
    int status = g_vm->GetEnv((void **) &env, JNI_VERSION_1_4);
    if (status != JNI_OK) {
        int getEnvCode = g_vm->AttachCurrentThread(&env, NULL);
        if (JNI_OK == getEnvCode) {
            *handAttach = true;
            return env;
        } else {
            return NULL;
        }
    } else {
        return env;
    }
}

void detachEnv(JNIEnv *env, bool byAttach) {
    if (env && byAttach) {
        g_vm->DetachCurrentThread();
    }
}

JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    g_vm = vm;
    bool byAttach = false;
    JNIEnv *env = attachEnv(&byAttach);
    if (env == NULL) {
        return JNI_ERR;
    }
    jclass crashDumperClazz = env->FindClass("com/yc/crash/NativeCrashDumper");
    JNINativeMethod jniNativeMethod[] = {
            {"nativeInit", "(Ljava/lang/String;Lcom/yc/crash/NativeCrashListener;I)V",
             (void *) nativeInit}
    };
    if (env->RegisterNatives(crashDumperClazz, jniNativeMethod,
                             sizeof(jniNativeMethod) / sizeof((jniNativeMethod)[0])) < 0) {
        return JNI_ERR;
    }
    detachEnv(env, byAttach);

    return JNI_VERSION_1_4;
}
