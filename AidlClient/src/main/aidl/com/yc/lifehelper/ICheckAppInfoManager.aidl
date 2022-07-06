// ICheckAppInfoManager.aidl
package cn.ycbjie.ycaudioplayer;
import cn.ycbjie.ycaudioplayer.AppInfo;
// Declare any non-default types here with import statements

interface ICheckAppInfoManager {

    //AIDL中支持以下的数据类型
    //1. 基本数据类型
    //2. String 和CharSequence
    //3. List 和 Map ,List和Map 对象的元素必须是AIDL支持的数据类型;
    //4. AIDL自动生成的接口（需要导入-import）
    //5. 实现android.os.Parcelable 接口的类（需要导入-import)

    List<AppInfo> getAppInfo(String sign);

    boolean setToken(String sign,String token);

    boolean setChannel(String sign,String channel);

    boolean setAppAuthorName(String sign,String name);

}
