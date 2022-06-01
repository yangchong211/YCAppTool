package com.yc.logupload.config


/**
 * @author yangchong
 * email  : yangchong211@163.com
 * time   : 2022/5/11
 * desc   : 搜索file文件的条件bean，目前简单化，按照时间纬度
 *          实践文档：https://wiki.zuoyebang.cc/pages/viewpage.action?pageId=356760839
 * revise :
 */
class UploadLogBean {

    /**
     * 给某个字符串时间
     */
    //var time: String? = null

    /**
     * 给某个时间戳，单位是浩渺
     */
    var systemTime: Long = 0
}