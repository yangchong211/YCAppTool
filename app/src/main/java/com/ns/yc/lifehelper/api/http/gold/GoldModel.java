package com.ns.yc.lifehelper.api.http.gold;

import com.ns.yc.lifehelper.api.constantApi.ConstantALiYunApi;
import com.ns.yc.lifehelper.api.RetrofitWrapper;
import com.ns.yc.lifehelper.ui.other.gold.model.GoldListBean;

import java.util.List;

import rx.Observable;


/**
 * Created by PC on 2017/8/21.
 * 作者：PC
 */

public class GoldModel {

    private static GoldModel model;
    private GoldApi mApiService;

    private GoldModel() {
        mApiService = RetrofitWrapper
                .getInstance(ConstantALiYunApi.GOLD_URL)
                .create(GoldApi.class);
    }

    public static GoldModel getInstance(){
        if(model == null) {
            model = new GoldModel();
        }
        return model;
    }

    public Observable<List<GoldListBean>> fetchGoldList(String type, int num, int page) {
        return mApiService.getGoldList(ConstantALiYunApi.LEANCLOUD_ID, ConstantALiYunApi.LEANCLOUD_SIGN,
                "{\"category\":\"" + type + "\"}", "-createdAt", "user,user.installation", num, page * num);
    }

    public Observable<List<GoldListBean>> fetchGoldHotList(String type, String dataTime, int limit) {
        return mApiService.getGoldHot(ConstantALiYunApi.LEANCLOUD_ID, ConstantALiYunApi.LEANCLOUD_SIGN,
                "{\"category\":\"" + type + "\",\"createdAt\":{\"$gt\":{\"__type\":\"Date\",\"iso\":\""
                        + dataTime + "T00:00:00.000Z\"}},\"objectId\":{\"$nin\":[\"58362f160ce463005890753e\"," +
                        "\"583659fcc59e0d005775cc8c\",\"5836b7358ac2470065d3df62\"]}}",
                "-hotIndex", "user,user.installation", limit);
    }

}
