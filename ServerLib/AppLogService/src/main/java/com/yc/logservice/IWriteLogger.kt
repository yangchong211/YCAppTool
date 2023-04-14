package com.yc.logservice

interface IWriteLogger {

    /**
     * 日志输出
     *
     * @param leve
     * @param msg
     */
    fun log(leve: Int, msg: String?)

    /**
     * 单个追加
     *
     * @param object
     */
    fun append(`object`: String?)

    /**
     * 批量追加
     * @param list
     */
    fun append(list: List<String?>?)

    /**
     * 清除缓存到文件
     */
    fun flush()

    /**
     * 关闭
     */
    fun close()
}