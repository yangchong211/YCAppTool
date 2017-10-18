package com.ns.yc.lifehelper.service;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.ns.yc.lifehelper.ui.other.myPicture.bean.PicBeautifulContentBean;
import com.ns.yc.lifehelper.ui.other.myPicture.util.ContentParser;
import com.ns.yc.lifehelper.ui.other.myPicture.util.RequestFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by PC on 2017/9/4.
 * 作者：PC
 */

public class PicGroupService extends IntentService {

    private static final String TAG = "GroupService";
    private final OkHttpClient client = new OkHttpClient();
    private String groupId;
    private String html;
    private List<PicBeautifulContentBean> lists = new ArrayList<>();
    private RealmResults<PicBeautifulContentBean> groupIdResults;

    public PicGroupService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        groupId = intent.getStringExtra("groupId");
        Intent resultIntent = new Intent(groupId);

        Realm realm = Realm.getDefaultInstance();
        realm.createAllFromJson(PicBeautifulContentBean.class,"");
        if(realm.where(PicBeautifulContentBean.class).findAll()!=null){
            groupIdResults = realm.where(PicBeautifulContentBean.class)
                    .equalTo("groupid", groupId)
                    .findAll();
        }


        if (!groupIdResults.isEmpty()) {//数据库有  直接发送广播通知

        } else {//否则加载网络 并存入数据库 通知
            try {
                ResponseBody body = client.newCall(RequestFactory.make(groupId))
                        .execute()
                        .body();
                html = body.string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(html!=null){
                int count = ContentParser.getCount(html);
                resultIntent.putExtra("count", count);
                sendBroadcast(resultIntent);
                for (int i = 1; i < count + 1; i++) {
                    PicBeautifulContentBean content = null;
                    try {
                        content = fetchContent(groupId + "/" + i);
                        content.setOrder(Integer.parseInt(groupId + i));
                        content.setGroupid(groupId);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    saveDb(realm, content);
                    lists.add(content);
                }
            }
        }
        sendBroadcast(resultIntent);
        realm.close();
    }

    private void saveDb(Realm realm, PicBeautifulContentBean content) {
        realm.beginTransaction();
        realm.copyToRealm(content);
        realm.commitTransaction();
    }

    /**
     * 抓取content
     *
     * @param path      路径
     * @return
     */
    private PicBeautifulContentBean fetchContent(String path) throws IOException {
        String html;
        try {
            html = client.newCall(RequestFactory.make(path)).execute().body().string();
        } catch (IOException e) {
            Log.e(TAG, "Failed to fetch " + path, e);
            return null;
        }

        PicBeautifulContentBean content = ContentParser.ParserContent(html);    //这里解析获取的HTML文本

        //其实这里不用再去解析bitmap，从HTML可以解析到的。。。至于为什么不去解析，我也不知道我当时怎么想的。。
        Response response = client.newCall(new Request.Builder()
                .url(content.getUrl())
                .build())
                .execute();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        ResponseBody body = response.body();
        if(body!=null){
            BitmapFactory.decodeStream(body.byteStream(), null, options);
            content.setImagewidth(options.outWidth);
            content.setImageheight(options.outHeight);
            return content;
        }else {
            return null;
        }
    }

}
