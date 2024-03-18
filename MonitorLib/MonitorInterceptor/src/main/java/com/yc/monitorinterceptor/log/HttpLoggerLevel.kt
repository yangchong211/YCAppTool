package com.yc.netinterceptor.log

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     GitHub : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : http输出信息level
 *     revise:
 * </pre>
 */
enum class HttpLoggerLevel {

    /** No logs. */
    NONE,

    /**
     * Logs request and response lines.
     *
     * Example:
     * ```
     * --> POST /greeting http/1.1 (3-byte body)
     *
     * <-- 200 OK (22ms, 6-byte body)
     * ```
     */
    BASIC,

    /**
     * 记录请求行和响应行及其各自的标题
     *
     * Example:
     * ```
     * --> POST /greeting http/1.1
     * Host: example.com
     * Content-Type: plain/text
     * Content-Length: 3
     * --> END POST
     *
     * <-- 200 OK (22ms)
     * Content-Type: plain/text
     * Content-Length: 6
     * <-- END HTTP
     * ```
     */
    HEADERS,

    /**
     * 记录请求行和响应行及其各自的标题和正文(如果存在)。
     *
     * Example:
     * ```
     * --> POST /greeting http/1.1
     * Host: example.com
     * Content-Type: plain/text
     * Content-Length: 3
     *
     * Hi?
     * --> END POST
     *
     * <-- 200 OK (22ms)
     * Content-Type: plain/text
     * Content-Length: 6
     *
     * Hello!
     * <-- END HTTP
     * ```
     */
    BODY
}