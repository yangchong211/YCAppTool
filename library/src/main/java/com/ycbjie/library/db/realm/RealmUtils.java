package com.ycbjie.library.db.realm;


import android.app.Application;
import android.util.Log;
import com.ycbjie.library.constant.Constant;
import java.io.File;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * <pre>
 *     @author      杨充
 *     blog         https://www.jianshu.com/p/53017c3fc75d
 *     time         2016/5/14
 *     desc         全局所有的realm数据库工具类
 *     revise
 *     GitHub       https://github.com/yangchong211
 * </pre>
 */
public class RealmUtils {

    private static Realm realm;

    public static void initRealm(Application application){
        File file ;
        try {
            file = new File(Constant.ExternalStorageDirectory, Constant.DATABASE_FILE_PATH_FOLDER);
            //noinspection ResultOfMethodCallIgnored
            file.mkdirs();
        } catch (Exception e) {
            Log.e("异常",e.getMessage());
        }
        Realm.init(application);
        RealmConfiguration realmConfig = new RealmConfiguration
                .Builder()
                .name(Constant.REALM_NAME)
                .schemaVersion(Constant.REALM_VERSION)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(realmConfig);
    }

    public static void closeRealm(){
        if(realm!=null){
            realm.close();
            realm = null;
        }
    }

    /**
     * 获取Realm数据库对象
     * @return              realm对象
     */
    public static Realm getRealmHelper() {
        return realm;
    }

}
