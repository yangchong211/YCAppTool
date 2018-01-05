package com.ns.yc.lifehelper.ui.guide.presenter;

import android.app.Activity;
import android.text.TextUtils;

import com.ns.yc.lifehelper.api.Constant;
import com.ns.yc.lifehelper.api.ConstantImageApi;
import com.ns.yc.lifehelper.base.app.BaseApplication;
import com.ns.yc.lifehelper.base.BaseConfig;
import com.ns.yc.lifehelper.bean.HomeBlogEntity;
import com.ns.yc.lifehelper.cache.CacheFindBottomNews;
import com.ns.yc.lifehelper.cache.CacheFindNews;
import com.ns.yc.lifehelper.cache.CacheHomeNews;
import com.ns.yc.lifehelper.cache.CacheHomePile;
import com.ns.yc.lifehelper.ui.guide.contract.GuideContract;
import com.ns.yc.lifehelper.ui.weight.pileCard.ItemEntity;

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
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/14
 * 描    述：启动页
 * 修订历史：
 *          启动页目前设置为5秒倒计时
 * ================================================
 */
public class GuidePresenter implements GuideContract.Presenter {

    private GuideContract.View mView;
    private CompositeSubscription mSubscriptions;
    private Realm realm;
    private Activity activity;

    public GuidePresenter(GuideContract.View androidView) {
        this.mView = androidView;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {
        if(BaseConfig.INSTANCE.isShowGirlImg()){
            String bannerUrl = BaseConfig.INSTANCE.getBannerUrl();
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

    private void initRealm() {
        if(realm ==null){
            realm = BaseApplication.getInstance().getRealmHelper();
        }
    }


    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
        activity = null;
    }


    @Override
    public void goMainActivity() {
        mView.toMainActivity();
    }

    @Override
    public void cacheHomeNewsData() {
        activity = mView.getActivity();
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
