package com.yc.android.tools.utils

import android.app.Activity
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.yc.toastutils.ToastUtils


fun Activity.showToast(text : CharSequence?){
    ToastUtils.showRoundRectToast(text)
}

