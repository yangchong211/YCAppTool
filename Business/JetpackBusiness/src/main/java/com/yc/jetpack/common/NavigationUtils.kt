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


fun Fragment.findNavController(activity : Activity, id : Int): NavController {
    //在activity中找到NavController
    return Navigation.findNavController(activity , id)
}

//返回到上一个页面
fun Fragment.navigateUp() {
    findNavController().navigateUp()
}

fun Fragment.navigate(directions : NavDirections) {
    findNavController().navigate(directions)
}

fun Fragment.navigate(@IdRes resId: Int, args: Bundle?, navOptions: NavOptions?) {
    findNavController().navigate(resId, args, navOptions)
}

fun Fragment.navigate(directions: NavDirections, navOptions: NavOptions) {
    findNavController().navigate(directions, navOptions)
}



