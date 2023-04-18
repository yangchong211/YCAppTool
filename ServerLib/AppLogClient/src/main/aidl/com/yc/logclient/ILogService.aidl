// ILogService.aidl
package com.yc.logclient;

// Declare any non-default types here with import statements
// 可以看到里面有一个AppLogBean，注意这个类需要自己创建，并且手动导包进来。否则编译时找不到……
import com.yc.logclient.bean.AppLogBean;

interface ILogService {

    /**
     * 服务端提供的方法
     * 输出单个日志实体
     */
    void log(in AppLogBean bean);

    /**
     * 输出单个日志实体数组
     * 1.定义的方法不能有修饰符
     * 2.不能有方法体
     * 3.除了基本数据类型，要加上方向参数  in 输入性（作为参数传入）  out 输出型（返回值
     */
    void logs(in List<AppLogBean>beans);

    /**
     * 截取logcat日志 输出到指定的文件
     *
     * @Params savePath 保存路径,
     * @Params delayTime 延时一段时间 执行截取操作.
     * @Params delayTime clearLogcat 截取logcat之后 是否清楚logcat
     */
    void collectlogcat(in String savePath, in long delayTime, in boolean clearLogcat);

    /**
     * 将字符串保存到指定文件
     *
     * @Params log 待保存的日志文件,
     * @Params path 保存文件的位置
     */
    void saveLogWithPath(in String log, in String path);

    /**
     * 设置默认的日志保存路径
     */
    void setDefaultLogSaveFolder(String path);

}
