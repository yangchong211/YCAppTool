package com.yc.android.model.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class ProjectListBean(
    @SerializedName("curPage")
    val curPage: Int,
    @SerializedName("datas")
    val datas: MutableList<HomeData>? = null,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("isOver")
    val isOver: Boolean = false,
    @SerializedName("pageCount")
    val pageCount: Int = 0,
    @SerializedName("size")
    val size: Int = 0,
    @SerializedName("total")
    val total: Int,
) : Serializable {

    companion object{
        const val NO_MORE = false
    }

    fun getHomeDataSize(): Int {
        return datas?.size ?: 0
    }

}
