package com.yc.logclient.inter

interface ILogInterface {
    /**
     * 打印正常业务日志，日志文件追加方式写入
     *
     * @param type
     * @param level
     * @param msg
     */
    fun logu(type: Int, level: Int, msg: String?)

    /**
     * 打印crash日志，日志文件追加方式写入
     *
     * @param type
     * @param leve
     * @param msg
     * @param ex
     */
    fun crash(type: Int, leve: Int, msg: String?, ex: Throwable?)

    /**
     * 打印anr日志，日志文件追加方式写入
     *
     * @param type
     * @param level
     * @param msg
     */
    fun anr(type: Int, level: Int, msg: String?)

    /**
     * 将日志flush进入 文件。
     */
    fun flushLog()

    /**
     * 截取logcat日志 保存到文件，日志文件不追加。
     *
     * @param savepath    文件保存位置
     * @param delaytime   延时时间
     * @param clearlogcat 截取logcat日志之后 是否清除logcat
     */
    fun collectLogcat(savepath: String?, delaytime: Long, clearlogcat: Boolean)

    /**
     * 将字符串 保存到 文件,用于一次性打印少量日志到文件，日志文件不追加。
     *
     * @param log  字符串
     * @param path 文件保存路径
     */
    fun saveLogWithPath(log: String?, path: String?)
}