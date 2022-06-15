package com.yc.toolutils;

import android.content.Context;

/**
 * <pre>
 *     @author: yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2020/07/08
 *     desc  : 堆栈相关工具类
 * </pre>
 */
public final class StackTraceUtils {

    public static void print(Throwable throwable) {
        //堆栈跟踪元素，它由 Throwable.getStackTrace() 返回。每个元素表示单独的一个【堆栈帧】。
        //所有的堆栈帧（堆栈顶部的那个堆栈帧除外）都表示一个【方法调用】。堆栈顶部的帧表示【生成堆栈跟踪的执行点】。
        StackTraceElement[] stackTraces = throwable.getStackTrace();
        for (StackTraceElement stackTrace : stackTraces) {
            //获取class的名称，该类包含由该堆栈跟踪元素所表示的执行点
            String clazzName = stackTrace.getClassName();
            //返回源文件名，该文件包含由该堆栈跟踪元素所表示的执行点。
            String fileName = stackTrace.getFileName();
            //返回源行的行号，该行包含由该堆栈该跟踪元素所表示的执行点。
            int lineNumber = stackTrace.getLineNumber();
            //返回方法名，此方法包含由该堆栈跟踪元素所表示的执行点。
            String methodName = stackTrace.getMethodName();
            //如果包含由该堆栈跟踪元素所表示的执行点的方法是一个本机方法，则返回 true。
            boolean nativeMethod = stackTrace.isNativeMethod();
            AppLogUtils.i("print clazzName : "+clazzName
                    +" fileName " +fileName+" lineNumber "
                    +lineNumber+" methodName "+methodName + " " + nativeMethod);
        }
    }

    private static StackTraceElement parseThrowable(Throwable ex , Context context) {
        if (ex == null || ex.getStackTrace() == null || ex.getStackTrace().length == 0) {
            return null;
        }
        if (context == null){
            return null;
        }
        StackTraceElement[] stackTrace = ex.getStackTrace();
        StackTraceElement element;
        for (StackTraceElement ele : stackTrace) {
            if (ele.getClassName().contains(context.getPackageName())) {
                element = ele;
                String clazzName = element.getClassName();
                String fileName = element.getFileName();
                int lineNumber = element.getLineNumber();
                String methodName = element.getMethodName();
                boolean nativeMethod = element.isNativeMethod();
                AppLogUtils.i("StackTraceUtils clazzName : "+clazzName
                        +" fileName " +fileName+" lineNumber "
                        +lineNumber+" methodName "+methodName + " " + nativeMethod);
                return element;
            }
        }
        element = stackTrace[0];
        String clazzName = element.getClassName();
        String fileName = element.getFileName();
        int lineNumber = element.getLineNumber();
        String methodName = element.getMethodName();
        boolean nativeMethod = element.isNativeMethod();
        AppLogUtils.i("parseThrowable clazzName : "+clazzName
                +" fileName " +fileName+" lineNumber "
                +lineNumber+" methodName "+methodName + " " + nativeMethod);
        return element;
    }

    public static String getMethodStack() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StringBuilder stringBuilder = new StringBuilder();
        for (StackTraceElement temp : stackTraceElements) {
            stringBuilder.append(temp.toString()).append("\n");
        }
        return stringBuilder.toString();
    }



}
