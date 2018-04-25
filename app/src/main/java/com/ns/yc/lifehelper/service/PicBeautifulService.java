package com.ns.yc.lifehelper.service;

import android.app.IntentService;
import android.content.Intent;

import com.ns.yc.lifehelper.comment.Constant;
import com.ns.yc.lifehelper.ui.other.myPicture.bean.PicBeautifulMainBean;
import com.ns.yc.lifehelper.ui.other.myPicture.util.BeautifulPicUtils;
import com.ns.yc.lifehelper.ui.other.myPicture.util.ContentParser;
import com.ns.yc.lifehelper.ui.other.myPicture.util.RequestFactory;

import java.io.IOException;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/1
 * 描    述：美图欣赏服务
 * 修订历史：
 * ================================================
 */
public class PicBeautifulService extends IntentService {

    private String type;
    private String mPage;
    private Intent resultIntent;
    private Realm realm;
    private static final String TAG = "PicBeautifulService";
    private RealmResults<PicBeautifulMainBean> latest;

    public PicBeautifulService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        initIntentData(intent);
        //返回结果的
        resultIntent = new Intent(type);
        //IllegalStateException: Realm access from incorrect thread. Realm objects can only be accessed on the thread they were created.
        //realm = BaseApplication.getInstance().getRealmHelper();
        //realm = Realm.getDefaultInstance();
        RealmConfiguration realmConfig = new RealmConfiguration
                .Builder()
                .name(Constant.REALM_NAME)
                .schemaVersion(Constant.REALM_VERSION)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(realmConfig);
        realm.createAllFromJson(PicBeautifulMainBean.class,"");


        if(realm.where(PicBeautifulMainBean.class)!=null && realm.where(PicBeautifulMainBean.class).findAll()!=null){
            latest = realm.where(PicBeautifulMainBean.class)
                    .equalTo("type", type)
                    .findAll();
        }

        boolean hasdata = ((latest==null ? 0 : latest.size()) >= Page2int(mPage) * 24);//数据库有数据
        boolean firstload = (Page2int(mPage) == 1);//第一次加载||刷新
        boolean loadmore = (Page2int(mPage) != 1);//加载更多

        if (hasdata) {//数据库有
            if (firstload) {//刷新
                resultIntent.putExtra("isRefreshe", true);
            }
        } else {//数据库没有 就是第一次加载
            if (loadmore) {
                resultIntent.putExtra("isLoadmore", true);
            } else {
                resultIntent.putExtra("isFirstload", true);
            }
        }
        loadData();
        sendBroadcast(resultIntent);
        realm.close();
    }


    private void initIntentData(Intent intent) {
        if(intent!=null){
            type = intent.getStringExtra("type");
            mPage = intent.getStringExtra("page");
        }else {
            type = "mm";
            mPage = "2";
        }
    }

    int Page2int(String s) {
        int i = 1;
        if (s!=null && s.length()>0) {
            i = Integer.parseInt(s);
        }
        return i;
    }


    //加载数据
    private final OkHttpClient client = new OkHttpClient();
    private void loadData() {
        try {
            Call call = client.newCall(RequestFactory.make(BeautifulPicUtils.makeUrl(type, mPage)));
            Response execute = call.execute();
            ResponseBody body = execute.body();
            String html = body.string();
            /*String html = client.newCall(RequestFactory.make(BeautifulPicUtils.makeUrl(type, mPage)))
                    .execute()
                    .body()
                    .string();*/
            if(html!=null){
                List<PicBeautifulMainBean> list = ContentParser.ParserMainBean(html, type);
                saveDb(realm, list);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveDb(final Realm realm, final List<PicBeautifulMainBean> list) {
        //realm.createAllFromJson(PicBeautifulMainBean.class,"");
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(list);
        realm.commitTransaction();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
