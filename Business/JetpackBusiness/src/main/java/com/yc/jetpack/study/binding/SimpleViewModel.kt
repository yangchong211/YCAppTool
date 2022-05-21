package com.yc.jetpack.study.binding

import androidx.lifecycle.ViewModel

class SimpleViewModel : ViewModel(){

    var name = "杨充"
    var lastName = "YangChong"
    var likes = 0
        private set

    fun onLike() {
        likes++
        name = "杨充 $likes"
        lastName = "YangChong $likes"
    }

    val popularity: Popularity
        get() {
            return when {
                likes > 9 -> Popularity.STAR
                likes > 4 -> Popularity.POPULAR
                else -> Popularity.NORMAL
            }
        }

}