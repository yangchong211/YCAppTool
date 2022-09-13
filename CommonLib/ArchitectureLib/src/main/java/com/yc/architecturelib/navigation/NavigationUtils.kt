package com.yc.architecturelib.navigation

import android.app.Activity
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.yc.architecturelib.R

val ANIM_ARRAY_SLIDE = intArrayOf(
    R.anim.slide_enter, R.anim.slide_exit,
    R.anim.slide_pop_enter, R.anim.slide_pop_exit
)

val ANIM_ARRAY_TOP = intArrayOf(
    R.anim.bottom_enter, R.anim.bottom_exit,
    R.anim.bottom_pop_enter, R.anim.bottom_pop_exit
)

/*------------------------------------------------------------------------------*/
/*--------------------------Activity拓展函数--------------------------------------*/
/*------------------------------------------------------------------------------*/
/*------------------------------------------------------------------------------*/
/**
 * Activity拓展函数
 */
fun Activity.findNavController2(@IdRes viewId: Int): NavController {
    return Navigation.findNavController(this, viewId)
}

fun Activity.navigateUp(@IdRes viewId: Int): Boolean {
    return findNavController2(viewId).navigateUp()
}

fun Activity.navigate(@IdRes viewId: Int, directions: NavDirections, navOptions: NavOptions) {
    findNavController2(viewId).navigate(directions, navOptions)
}

fun Activity.navigate(
    @IdRes viewId: Int,
    directions: NavDirections,
    singleTop: Boolean = false,
    @IdRes popUpToDestinationId: Int = -1,
    popUpToInclusive: Boolean = false,
    animArray: IntArray = ANIM_ARRAY_SLIDE
) {
    val navOptions = buildNavOptions(
        singleTop,
        popUpToDestinationId,
        popUpToInclusive,
        animArray[0],
        animArray[1],
        animArray[2],
        animArray[3]
    )

    findNavController2(viewId).navigate(directions, navOptions)
}

@Suppress("unused")
fun Activity.popCurrentBackStackAndNavigate(
    @IdRes viewId: Int, directions: NavDirections,
    animArray: IntArray = ANIM_ARRAY_SLIDE
) {
    navigate(
        viewId,
        directions,
        singleTop = false,
        popUpToDestinationId = findNavController2(viewId).currentDestination?.id ?: -1,
        popUpToInclusive = true,
        animArray = animArray
    )
}

fun Activity.popAllBackStackAndNavigate(
    @IdRes viewId: Int, directions: NavDirections,
    animArray: IntArray = ANIM_ARRAY_SLIDE
) {
    navigate(
        viewId,
        directions,
        singleTop = false,
        popUpToDestinationId = findNavController2(viewId).graph.id,
        popUpToInclusive = true,
        animArray = animArray
    )
}

/*------------------------------------------------------------------------------*/
/*--------------------------------Fragment拓展函数--------------------------------*/
/*------------------------------------------------------------------------------*/
/*------------------------------------------------------------------------------*/
fun Fragment.findNavController2(): NavController {
    return NavHostFragment.findNavController(this)
}

/**
 * Fragment拓展函数
 */
fun Fragment.navigate(@IdRes resId: Int) {
    //通过id跳转
    findNavController2().navigate(resId)
}

fun Fragment.navigate(@IdRes resId: Int, args: Bundle?) {
    //通过id跳转
    findNavController2().navigate(resId, args)
}

fun Fragment.navigate(@IdRes resId: Int, args: Bundle?, navOptions: NavOptions?) {
    //通过id跳转
    findNavController2().navigate(resId, args, navOptions)
}

fun Fragment.navigate(directions: NavDirections, navOptions: NavOptions) {
    //通过action跳转
    findNavController2().navigate(directions, navOptions)
}

fun Fragment.navigateUp(): Boolean {
    //返回到上一页面
    return findNavController2().navigateUp()
}

fun Fragment.navigate(
    directions: NavDirections,
    singleTop: Boolean = false,
    @IdRes popUpToDestinationId: Int = -1,
    popUpToInclusive: Boolean = false,
    animArray: IntArray = ANIM_ARRAY_SLIDE
) {
    val navOptions = buildNavOptions(
        singleTop,
        popUpToDestinationId,
        popUpToInclusive,
        animArray[0],
        animArray[1],
        animArray[2],
        animArray[3]
    )
    findNavController2().navigate(directions, navOptions)
}


fun Fragment.navigateTop(
    directions: NavDirections,
    singleTop: Boolean = false,
    @IdRes popUpToDestinationId: Int = -1,
    popUpToInclusive: Boolean = false,
    animArray: IntArray = ANIM_ARRAY_TOP
) {
    val navOptions = buildNavOptions(
        singleTop,
        popUpToDestinationId,
        popUpToInclusive,
        animArray[0],
        animArray[1],
        animArray[2],
        animArray[3]
    )
    findNavController2().navigate(directions, navOptions)
}

fun Fragment.popCurrentBackStackAndNavigate(
    directions: NavDirections,
    animArray: IntArray = ANIM_ARRAY_SLIDE
) {
    navigate(
        directions,
        singleTop = false,
        popUpToDestinationId = findNavController2().currentDestination?.id ?: -1,
        popUpToInclusive = true,
        animArray = animArray
    )
}

fun Fragment.popAllBackStackAndNavigate(
    directions: NavDirections,
    animArray: IntArray = ANIM_ARRAY_SLIDE
) {
    navigate(
        directions,
        singleTop = false,
        popUpToDestinationId = findNavController2().graph.id,
        popUpToInclusive = true,
        animArray = animArray
    )
}

fun Fragment.popBackStackAndNavigate(
    directions: NavDirections,
    popUpToDestinationId: Int,
    animArray: IntArray = ANIM_ARRAY_SLIDE
) {
    navigate(
        directions,
        singleTop = false,
        popUpToDestinationId = popUpToDestinationId,
        popUpToInclusive = true,
        animArray = animArray
    )
}

/**
 * A页面跳转到B 页面
 * enterAnim 是指 B页面进入的动画
 * exitAnim 是指A页面退出时的动画
 * popEnterAnim 是指 B退出时，A进入的动画
 * popExitAnim 是指 B退出时,B退出的动画
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
