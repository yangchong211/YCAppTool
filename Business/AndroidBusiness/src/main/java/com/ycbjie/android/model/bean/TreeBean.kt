package com.ycbjie.android.model.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * children : []
 * courseId : 13
 * id : 60
 * name : Android Studio相关
 * order : 1000
 * parentChapterId : 150
 * visible : 1
 */

data class TreeBean(
    @SerializedName("courseId")
    val courseId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("order")
    val order: Int? = null,
    @SerializedName("parentChapterId")
    val parentChapterId: Int,
    @SerializedName("visible")
    val visible: Int,
    @SerializedName("children")
    val children: List<TreeBean>?,
) : Serializable

//class TreeBean : Serializable {
//    var courseId: Int = 0
//    var id: Int = 0
//    var name: String? = null
//    var order: Int = 0
//    var parentChapterId: Int = 0
//    var visible: Int = 0
//    var children: List<TreeBean>? = null
//}