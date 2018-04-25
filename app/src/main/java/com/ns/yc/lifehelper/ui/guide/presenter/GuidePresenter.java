package com.ns.yc.lifehelper.ui.guide.presenter;

import android.app.Activity;
import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.ns.yc.lifehelper.comment.Constant;
import com.ns.yc.lifehelper.api.constantApi.ConstantImageApi;
import com.ns.yc.lifehelper.base.app.BaseApplication;
import com.ns.yc.lifehelper.comment.config.AppConfig;
import com.ns.yc.lifehelper.db.cache.CacheFindBottomNews;
import com.ns.yc.lifehelper.db.cache.CacheFindNews;
import com.ns.yc.lifehelper.db.cache.CacheHomeNews;
import com.ns.yc.lifehelper.db.cache.CacheHomePile;
import com.ns.yc.lifehelper.model.bean.HomeBlogEntity;
import com.ns.yc.lifehelper.model.bean.ItemEntity;
import com.ns.yc.lifehelper.ui.guide.contract.GuideContract;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.subscriptions.CompositeSubscription;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/03/22
 *     desc  : 启动页面
 *     revise:
 * </pre>
 */
public class GuidePresenter implements GuideContract.Presenter {

    private GuideContract.View mView;
    private CompositeSubscription mSubscriptions;
    private Realm realm;
    private Activity activity;

    public GuidePresenter(GuideContract.View androidView) {
        this.mView = androidView;
        this.activity = (Activity) androidView;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {
        LogUtils.e("GuideActivity"+"------"+"subscribe");
    }

    private void initRealm() {
        if(realm ==null){
            realm = BaseApplication.getInstance().getRealmHelper();
        }
    }

    @Override
    public void unSubscribe() {
        mSubscriptions.unsubscribe();
    }


    @Override
    public void startGuideImage() {
        if(AppConfig.INSTANCE.isShowGirlImg()){
            String bannerUrl = AppConfig.INSTANCE.getBannerUrl();
            if(TextUtils.isEmpty(bannerUrl)){
                int i = new Random().nextInt(ConstantImageApi.SPALSH_URLS.length);
                String splashUrl = ConstantImageApi.SPALSH_URLS[i];
                mView.showGuideLogo(splashUrl);
            }else {
                mView.showGuideLogo(bannerUrl);
            }
        } else {
            // 先显示默认图
            int i = new Random().nextInt(ConstantImageApi.SPALSH_URLS.length);
            String splashUrl = ConstantImageApi.SPALSH_URLS[i];
            mView.showGuideLogo(splashUrl);
        }
        initRealm();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void cacheHomeNewsData() {
        List<HomeBlogEntity> blog = new ArrayList<>();
        try {
            InputStream in = activity.getAssets().open("ycBlog.config");
            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);
            String jsonStr = new String(buffer, "UTF-8");
            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONArray jsonArray = jsonObject.optJSONArray("result");
            if (null != jsonArray) {
                int len = jsonArray.length();
                for (int j = 0; j < 3; j++) {
                    for (int i = 0; i < len; i++) {
                        JSONObject itemJsonObject = jsonArray.getJSONObject(i);
                        HomeBlogEntity blogEntity = new HomeBlogEntity(itemJsonObject);
                        blog.add(blogEntity);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            cacheHomeNews(blog);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void cacheFindNewsData() {
        List<HomeBlogEntity> findNews = new ArrayList<>();
        try {
            InputStream in = activity.getAssets().open("findNews.config");
            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);
            String jsonStr = new String(buffer, "UTF-8");
            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONArray jsonArray = jsonObject.optJSONArray("result");
            if (null != jsonArray) {
                int len = jsonArray.length();
                for (int j = 0; j < 3; j++) {
                    for (int i = 0; i < len; i++) {
                        JSONObject itemJsonObject = jsonArray.getJSONObject(i);
                        HomeBlogEntity itemEntity = new HomeBlogEntity(itemJsonObject);
                        findNews.add(itemEntity);
                        Constant.findNews.add(itemEntity);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            cacheFindNews(findNews);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void cacheFindBottomNewsData() {
        List<HomeBlogEntity> findBottomNews = new ArrayList<>();
        try {
            InputStream in = activity.getAssets().open("findBottomNews.config");
            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);
            String jsonStr = new String(buffer, "UTF-8");
            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONArray jsonArray = jsonObject.optJSONArray("result");
            if (null != jsonArray) {
                int len = jsonArray.length();
                for (int j = 0; j < 3; j++) {
                    for (int i = 0; i < len; i++) {
                        JSONObject itemJsonObject = jsonArray.getJSONObject(i);
                        HomeBlogEntity itemEntity = new HomeBlogEntity(itemJsonObject);
                        findBottomNews.add(itemEntity);
                        Constant.findBottomNews.add(itemEntity);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            cacheFindBottomNews(findBottomNews);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void cacheHomePileData() {
        ArrayList<ItemEntity> dataList = new ArrayList<>();
        try {
            InputStream in = activity.getAssets().open("preset.config");
            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);
            String jsonStr = new String(buffer, "UTF-8");
            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONArray jsonArray = jsonObject.optJSONArray("result");
            if (null != jsonArray) {
                int len = jsonArray.length();
                for (int j = 0; j < 3; j++) {
                    for (int i = 0; i < len; i++) {
                        JSONObject itemJsonObject = jsonArray.getJSONObject(i);
                        ItemEntity itemEntity = new ItemEntity(itemJsonObject);
                        dataList.add(itemEntity);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cacheHomePileData(dataList);
        }
    }


    /**
     * 直接换成新闻页的数据
     */
    private void cacheHomeNews(List<HomeBlogEntity> blog) {
        initRealm();
        RealmResults<CacheHomeNews> cacheHomeNewses;
        if(realm.where(CacheHomeNews.class).findAll()!=null){
            cacheHomeNewses = realm.where(CacheHomeNews.class).findAll();
        }else {
            return;
        }
        realm.beginTransaction();
        cacheHomeNewses.deleteAllFromRealm();
        realm.commitTransaction();
        realm.beginTransaction();
        for(int a=0 ; a<blog.size() ; a++){
            CacheHomeNews homeNews = realm.createObject(CacheHomeNews.class);
            homeNews.setUrl(blog.get(a).getUrl());
            homeNews.setAuthor(blog.get(a).getAuthor());
            homeNews.setImageUrl(blog.get(a).getImageUrl());
            homeNews.setLogo(blog.get(a).getLogo());
            homeNews.setSummary(blog.get(a).getSummary());
            homeNews.setTime(blog.get(a).getTime());
            homeNews.setTitle(blog.get(a).getTitle());
        }
        realm.commitTransaction();
    }


    private void cacheFindNews(List<HomeBlogEntity> findNews) {
        initRealm();
        RealmResults<CacheFindNews> cacheFindNewses;
        if(realm.where(CacheFindNews.class).findAll()!=null){
            cacheFindNewses = realm.where(CacheFindNews.class).findAll();
        }else {
            return;
        }
        realm.beginTransaction();
        cacheFindNewses.deleteAllFromRealm();
        realm.commitTransaction();
        realm.beginTransaction();
        for(int a=0 ; a<findNews.size() ; a++){
            CacheFindNews news = realm.createObject(CacheFindNews.class);
            news.setUrl(findNews.get(a).getUrl());
            news.setAuthor(findNews.get(a).getAuthor());
            news.setImageUrl(findNews.get(a).getImageUrl());
            news.setLogo(findNews.get(a).getLogo());
            news.setSummary(findNews.get(a).getSummary());
            news.setTime(findNews.get(a).getTime());
            news.setTitle(findNews.get(a).getTitle());
        }
        realm.commitTransaction();
    }


    private void cacheFindBottomNews(List<HomeBlogEntity> findBottomNews) {
        initRealm();
        RealmResults<CacheFindBottomNews> cacheFindBottomNewses;
        if(realm.where(CacheFindBottomNews.class).findAll()!=null){
            cacheFindBottomNewses = realm.where(CacheFindBottomNews.class).findAll();
        }else {
            return;
        }
        realm.beginTransaction();
        cacheFindBottomNewses.deleteAllFromRealm();
        realm.commitTransaction();
        realm.beginTransaction();
        for(int a=0 ; a<findBottomNews.size() ; a++){
            CacheFindBottomNews news = realm.createObject(CacheFindBottomNews.class);
            news.setUrl(findBottomNews.get(a).getUrl());
            news.setAuthor(findBottomNews.get(a).getAuthor());
            news.setImageUrl(findBottomNews.get(a).getImageUrl());
            news.setLogo(findBottomNews.get(a).getLogo());
            news.setSummary(findBottomNews.get(a).getSummary());
            news.setTime(findBottomNews.get(a).getTime());
            news.setTitle(findBottomNews.get(a).getTitle());
        }
        realm.commitTransaction();
    }


    private void cacheHomePileData(ArrayList<ItemEntity> dataList) {
        initRealm();
        RealmResults<CacheHomePile> cacheHomePiles;
        if(realm.where(CacheHomePile.class).findAll()!=null){
            cacheHomePiles = realm.where(CacheHomePile.class).findAll();
        }else {
            return;
        }
        realm.beginTransaction();
        cacheHomePiles.deleteAllFromRealm();
        realm.commitTransaction();
        realm.beginTransaction();
        for(int a=0 ; a<dataList.size() ; a++){
            CacheHomePile news = realm.createObject(CacheHomePile.class);
            news.setTime(dataList.get(a).getTime());
            news.setAddress(dataList.get(a).getAddress());
            news.setCountry(dataList.get(a).getCountry());
            news.setCoverImageUrl(dataList.get(a).getCoverImageUrl());
            news.setMapImageUrl(dataList.get(a).getMapImageUrl());
            news.setDescription(dataList.get(a).getDescription());
            news.setTemperature(dataList.get(a).getTemperature());
        }
        realm.commitTransaction();
    }

}
