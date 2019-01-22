package com.ycbjie.android.view.weight

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet



class CricleImageView : AppCompatImageView{

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr){
        initView()
    }

    private fun initView() {

    }

}