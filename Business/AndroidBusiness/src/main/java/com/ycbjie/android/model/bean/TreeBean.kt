package com.ycbjie.android.model.bean

import java.io.Serializable


class TreeBean : Serializable {

    /**
     * children : []
     * courseId : 13
     * id : 60
     * name : Android Studio相关
     * order : 1000
     * parentChapterId : 150
     * visible : 1
     */

    var courseId: Int = 0
    var id: Int = 0
    var name: String? = null
    var order: Int = 0
    var parentChapterId: Int = 0
    var visible: Int = 0
    var children: List<TreeBean>? = null


}