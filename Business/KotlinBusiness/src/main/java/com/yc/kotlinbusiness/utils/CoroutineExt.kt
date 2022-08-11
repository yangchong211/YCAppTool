package com.yc.kotlinbusiness.utils

import kotlinx.coroutines.delay


suspend inline fun <T> delayReturning(
    delayTimeMillis: Long,
    includeExecutionTime: Boolean = true,
    block: () -> T
): T {
    val startTimeMillis = System.nanoTime() / 1_000000L
    val result = block()
    val finalDelayTimeMillis = if (includeExecutionTime) {
        startTimeMillis + delayTimeMillis - System.nanoTime() / 1_000000L
    } else {
        delayTimeMillis
    }
    delay(finalDelayTimeMillis)
    return result
}

suspend inline fun <T> delay(
    delayExecuteTimeMillis: Long = 0L,
    delayReturnTimeMillis: Long = 0L,
    includeExecutionTime: Boolean = true,
    block: () -> T
): T {
    delay(delayExecuteTimeMillis)
    val startTimeMillis = System.nanoTime() / 1_000000L
    val result = block()
    val finalDelayReturnTimeMillis = if (includeExecutionTime) {
        startTimeMillis + delayReturnTimeMillis - System.nanoTime() / 1_000000L
    } else {
        delayReturnTimeMillis
    }
    delay(finalDelayReturnTimeMillis)
    return result
}