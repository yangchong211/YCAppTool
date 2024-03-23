package com.yc.toolutils.stack;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * <pre>
 *     @author: yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2020/07/08
 *     desc  : 堆栈相关工具类
 * </pre>
 */
public final class StackTraceUtils {

    public final static byte[] getThrowableStackTraceBytes(Throwable throwable) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        throwable.printStackTrace(new PrintStream(baos));
        return baos.toByteArray();
    }

    /**
     * 打印throwable堆栈日志
     * @param throwable     异常
     */
    public static synchronized void print1(Throwable throwable) {
        //堆栈跟踪元素，它由 Throwable.getStackTrace() 返回。每个元素表示单独的一个【堆栈帧】。
        //所有的堆栈帧（堆栈顶部的那个堆栈帧除外）都表示一个【方法调用】。堆栈顶部的帧表示【生成堆栈跟踪的执行点】。
        StackTraceElement[] stackTraces = throwable.getStackTrace();
        String print = printToString1(stackTraces);
        Log.i("print1",print);
    }

    /**
     * 打印throwable堆栈日志
     * @param throwable     异常
     */
    public static synchronized void print2(Throwable throwable) {
        //堆栈跟踪元素，它由 Throwable.getStackTrace() 返回。每个元素表示单独的一个【堆栈帧】。
        //所有的堆栈帧（堆栈顶部的那个堆栈帧除外）都表示一个【方法调用】。堆栈顶部的帧表示【生成堆栈跟踪的执行点】。
        StackTraceElement[] stackTraces = throwable.getStackTrace();
        String print = printToString1(stackTraces);
        Log.i("print2",print);
    }

    /**
     * 打印线程堆栈日志
     * @param thread        线程
     */
    public static synchronized void print3(Thread thread) {
        //堆栈跟踪元素，它由 Throwable.getStackTrace() 返回。每个元素表示单独的一个【堆栈帧】。
        //所有的堆栈帧（堆栈顶部的那个堆栈帧除外）都表示一个【方法调用】。堆栈顶部的帧表示【生成堆栈跟踪的执行点】。
        StackTraceElement[] stackTraces = thread.getStackTrace();
        String print = printToString1(stackTraces);
        Log.i("print3",print);
    }

    /**
     * 打印线程堆栈日志
     * @param thread        线程
     */
    public static synchronized void print4(Thread thread) {
        //堆栈跟踪元素，它由 Throwable.getStackTrace() 返回。每个元素表示单独的一个【堆栈帧】。
        //所有的堆栈帧（堆栈顶部的那个堆栈帧除外）都表示一个【方法调用】。堆栈顶部的帧表示【生成堆栈跟踪的执行点】。
        StackTraceElement[] stackTraces = thread.getStackTrace();
        String print = printToString1(stackTraces);
        Log.i("print4",print);
    }

    /**
     * 打印线程堆栈日志
     */
    public static synchronized void print5() {
        Thread thread = Thread.currentThread();
        //堆栈跟踪元素，它由 Throwable.getStackTrace() 返回。每个元素表示单独的一个【堆栈帧】。
        //所有的堆栈帧（堆栈顶部的那个堆栈帧除外）都表示一个【方法调用】。堆栈顶部的帧表示【生成堆栈跟踪的执行点】。
        StackTraceElement[] stackTraces = thread.getStackTrace();
        String print = printToString1(stackTraces);
        Log.i("print5",print);
    }

    /**
     * 打印线程堆栈日志
     */
    public static synchronized void print6() {
        String methodStack = getMethodStack();
        Log.i("print6",methodStack);
    }

    /**
     * 获取当前线程的方法堆栈调用链路信息
     * @return          堆栈信息
     */
    public static synchronized String getMethodStack() {
        Thread thread = Thread.currentThread();
        StringBuilder stringBuilder = new StringBuilder();
        //获取线程信息
        String threadInfo = getThreadInfo(thread);
        stringBuilder.append(threadInfo);
        // 返回表示此线程的堆栈转储的堆栈跟踪元素数组。
        // 如果这个线程还没有启动，已经启动但还没有被系统计划运行，或者已经终止，这个方法将返回一个零长度的数组。
        StackTraceElement[] stackTraceElements = thread.getStackTrace();
        String print = printToString2(stackTraceElements);
        stringBuilder.append("线程堆栈日志：").append(print);
        return stringBuilder.toString();
    }

    private static synchronized String getThreadInfo(Thread thread){
        StringBuilder stringBuilder = new StringBuilder();
        // 记录线程id
        stringBuilder.append("线程thread的id：").append(thread.getId()).append("\n");
        // 记录线程名称
        stringBuilder.append("线程thread的name：").append(thread.getName()).append("\n");
        // 记录线程优先级
        stringBuilder.append("线程thread的priority：").append(thread.getPriority()).append("\n");
        // 记录线程状态
        // new新建，runnable正在执行，blocked等待(锁)，waiting挂起(wait)，timed_waiting超时，terminated终止
        stringBuilder.append("线程thread的state：").append(thread.getState()).append("\n");
        // 记录classLoader
        stringBuilder.append("线程thread的classLoader：").append(thread.getContextClassLoader()).append("\n");
        return stringBuilder.toString();
    }

    private static String printToString1(StackTraceElement[] stackTraces) {
        StringBuilder result = new StringBuilder();
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
            result.append(clazzName).append(".").append(methodName);
            if (nativeMethod) {
                //这种是 native 方法
                result.append("(Native Method)");
            } else if (fileName != null) {
                //有方法名称
                if (lineNumber >= 0) {
                    result.append("(").append(fileName).append(":").append(lineNumber).append(")");
                } else {
                    result.append("(").append(fileName).append(")");
                }
            } else {
                //没有方法名称
                if (lineNumber >= 0) {
                    result.append("(Unknown Source:").append(lineNumber).append(")");
                } else {
                    result.append("(Unknown Source)");
                }
            }
            result.append("\n");
        }
        return result.toString();
    }


    private static String printToString2(StackTraceElement[] stackTraces) {
        StringBuilder result = new StringBuilder();
        for (StackTraceElement temp : stackTraces) {
            result.append(temp.toString())
                    .append("\n");
        }
        return result.toString();
    }


}
