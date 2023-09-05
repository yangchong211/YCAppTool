#include "stack_tracer.h"
#include "readelf.h"
#include <unwind.h>
#include <dlfcn.h>
#include <android/log.h>
#include <cstdio>
#include <malloc.h>
#include <fstream>
#include <ctime>
#include <map>

typedef struct TraceInfo {
    int depth;
    std::string result;
} BackTraceInfo;

std::map<const char *, std::string> buildIdMap;

std::string getSharedObjectBuildId(const char *path) {
    std::string buildId = buildIdMap[path];
    if (buildId.length() == 0) {
        buildId = getBuildIdFromFile(path);
        buildIdMap[path] = buildId;
    }
    return buildId;
}

_Unwind_Reason_Code traceBackCallStack(_Unwind_Context *context, void *hnd) {
    auto *traceHandle = (BackTraceInfo *) hnd;

    _Unwind_Word ip = _Unwind_GetIP(context);
    Dl_info info;
    int res = dladdr((void *) ip, &info);

    if (res == 0) {
        return _URC_END_OF_STACK;
    }

    char *desc = (char *) malloc(1024);
    memset(desc, 0, 1024);
    std::string buildId;
    if (info.dli_fname != NULL) {
        char *symbol = (char *) malloc(256);

        if (info.dli_sname == NULL) {
            strcpy(symbol, "unknown");
        } else {
            sprintf(symbol, "%s+%ld", info.dli_sname,
                    ip - (_Unwind_Word) info.dli_saddr);
        }

        buildId = getSharedObjectBuildId(info.dli_fname);
        if (buildId.length() > 0) {
            buildId = "(BuildId: " + buildId + ")";
        }
#if defined(__arm__)
        sprintf(desc, "     #%02d pc %08lx  %s (%s) ", traceHandle->depth,
                ip - (_Unwind_Word) info.dli_fbase,
                info.dli_fname, symbol);
#elif defined(__aarch64__)
        sprintf(desc, "     #%02d pc %016lx  %s (%s) ", traceHandle->depth,
                ip - (_Unwind_Word) info.dli_fbase,
                info.dli_fname, symbol);
#endif
        free(symbol);
    }


    if (traceHandle->result.length() != 0) {
        traceHandle->result.append("\r\n");
    }
    traceHandle->result.append(desc).append(buildId);
    free(desc);
    ++traceHandle->depth;
    // FIXME: crash if call stack is over 5 on ARM32, unknown reason
#if !defined(__aarch64__) && defined(__arm__)
    if (traceHandle->depth == 5) {
        return _URC_END_OF_STACK;
    }
#endif
    return _URC_NO_REASON;
}

void storeCallStack(std::string *result) {
    BackTraceInfo traceInfo;
    traceInfo.depth = 0;
    _Unwind_Backtrace(traceBackCallStack, &traceInfo);
    *result += traceInfo.result;
}