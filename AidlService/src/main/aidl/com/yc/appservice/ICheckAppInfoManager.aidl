// ICheckAppInfoManager.aidl
package com.yc.appservice;
import com.yc.appservice.AppInfo;
// Declare any non-default types here with import statements

//使用 AIDL 定义了基本的业务操作，rebuild 之后会生成与 .aidl 相同文件名的 .java 文件
//生成的这个接口文件(在build-generated-aidl_source下的.java文件)中有一个名称为 Stub 的一个子类，这个子类也是其父接口的抽象实现，主要用于生成 .aidl 文件中的所有方法
//public static abstract class Stub extends android.os.Binder implements com.yc.appclient.ICheckAppInfoManager
//Stub 实现了本地接口且继承了 Binder 对象，介于 Binder 对象在系统底层的支持下，Stub 对象就具有了远程传输数据的能力，在生成 Stub 对象的时候会调用 asInterface 方法


//在 aidl 文件中编写 service 提供给 client 的可调用方法
interface ICheckAppInfoManager {

    //AIDL中支持以下的数据类型
    //1. 基本数据类型
    //2. String 和CharSequence
    //3. List 和 Map ,List和Map 对象的元素必须是AIDL支持的数据类型;
    //4. AIDL自动生成的接口（需要导入-import）
    //5. 实现android.os.Parcelable 接口的类（需要导入-import)

    //需要特别注意的是，对于非基本数据类型，也不是String和CharSequence类型的，需要有方向指示，包括in、out和inout
    //in表示由客户端设置，out表示由服务端设置，inout是两者均可设置。

    List<AppInfo> getAppInfo(String sign);

    boolean setToken(String sign,String token);

    boolean setChannel(String sign,String channel);

    boolean setAppAuthorName(String sign,String name);

}
