package com.yc.jetpack.common

import android.app.Activity
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController

/**
 * @author: 杨充
 * email  : yangchong211@163.com
 * time   : 2017/05/23
 * desc   : 拓展类
 * revise :
 */


fun Fragment.findNavController(activity : Activity, id : Int): NavController {
    //在activity中找到NavController
    return Navigation.findNavController(activity , id)
}

/**
 * 返回到上一个页面
 */
fun Fragment.navigateUp() {
    findNavController().navigateUp()
}

/**
 * 跳转到目标页面
 */
fun Fragment.navigate(directions : NavDirections) {
    findNavController().navigate(directions)
}

fun Fragment.navigate(@IdRes resId: Int, args: Bundle?, navOptions: NavOptions?) {
    findNavController().navigate(resId, args, navOptions)
}

fun Fragment.navigate(directions: NavDirections, navOptions: NavOptions) {
    findNavController().navigate(directions, navOptions)
}



/**
 * A页面跳转到B 页面
 * - enterAnim 是指 B页面进入的动画
 * - exitAnim 是指A页面退出时的动画
 * - popEnterAnim 是指 B退出时，A进入的动画
 * - popExitAnim 是指 B退出时,B退出的动画
 */
private fun buildNavOptions(
    singleTop: Boolean,
    popUpToFragmentId: Int,
    popUpToInclusive: Boolean,
    enterAnim: Int,
    exitAnim: Int,
    popEnterAnim: Int,
    popExitAnim: Int
): NavOptions {
    return NavOptions.Builder()
        .setLaunchSingleTop(singleTop)
        .setPopUpTo(popUpToFragmentId, popUpToInclusive)
        .setEnterAnim(enterAnim)
        .setExitAnim(exitAnim)
        .setPopEnterAnim(popEnterAnim)
        .setPopExitAnim(popExitAnim)
        .build()
}



