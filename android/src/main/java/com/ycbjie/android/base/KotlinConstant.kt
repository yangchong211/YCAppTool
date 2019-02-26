
package com.ycbjie.android.base

/**
 * <pre>
 *     @author yangchong
 *     blog  :
 *     time  : 2017/03/22
 *     desc  : 存放常量
 *     revise:
 * </pre>
 */

object KotlinConstant {

    /**
     * 在Kotlin中，可以使用两种方法来停止自动生成get和set方法。
     * 1.使用const关键字
     * 2.使用@JvmField注解
     */


    //val 表示常量
    const val INTENT_IS_COLLECT = "isCollect"
    const val COLLECT_STATUS = "collectStatus"
    const val COLLECT_ID = "collectId"
    const val USER_ID = "userId"
    const val HISTORY_SEARCH = "historySearch"
    const val USER_NAME = "userName"
    const val USER_EMAIL = "userEmail"

    const val HOME = 0
    const val FIND = 1
    const val DATA = 2
    const val USER = 3


    //第二种方式
    @JvmStatic
    val NEW_ID = "newId"
    @JvmField
    val IS_CACHE = "isCache"

}