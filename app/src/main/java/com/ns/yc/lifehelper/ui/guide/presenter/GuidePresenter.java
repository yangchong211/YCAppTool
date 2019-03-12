package com.ns.yc.lifehelper.ui.guide.presenter;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.ns.yc.lifehelper.model.bean.ItemEntity;
import com.ns.yc.lifehelper.ui.guide.contract.GuideContract;
import com.ycbjie.library.api.ConstantImageApi;
import com.ycbjie.library.base.config.AppConfig;
import com.ycbjie.library.constant.Constant;
import com.ycbjie.library.model.HomeBlogEntity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

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

    public GuidePresenter(GuideContract.View androidView) {
        this.mView = androidView;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {
        LogUtils.e("GuideActivity"+"------"+"subscribe");
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
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void cacheFindNewsData() {
        Constant.findNews.clear();
        try {
            InputStream in = Utils.getApp().getAssets().open("findNews.config");
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
                        Constant.findNews.add(itemEntity);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void cacheFindBottomNewsData() {
        Constant.findBottomNews.clear();
        try {
            InputStream in = Utils.getApp().getAssets().open("findBottomNews.config");
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
                        Constant.findBottomNews.add(itemEntity);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void cacheHomePileData() {
        ArrayList<ItemEntity> dataList = new ArrayList<>();
        try {
            InputStream in = Utils.getApp().getAssets().open("preset.config");
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
        }
    }



}
