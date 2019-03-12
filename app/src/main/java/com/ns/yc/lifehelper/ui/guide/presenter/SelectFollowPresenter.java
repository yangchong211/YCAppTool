package com.ns.yc.lifehelper.ui.guide.presenter;

import android.app.Activity;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.model.bean.SelectPoint;
import com.ns.yc.lifehelper.ui.guide.contract.SelectFollowContract;
import com.ns.yc.lifehelper.model.cache.SelectFollow;
import com.ns.yc.lifehelper.model.cache.SelectUnFollow;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.subscriptions.CompositeSubscription;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/03/22
 *     desc  : 关注点页面
 *     revise:
 * </pre>
 */
public class SelectFollowPresenter implements SelectFollowContract.Presenter {

    private SelectFollowContract.View mView;
    private CompositeSubscription mSubscriptions;
    private RealmResults<SelectUnFollow> selectUnFollows;
    private List<SelectPoint> list = new ArrayList<>();
    private int del;


    public SelectFollowPresenter(SelectFollowContract.View androidView) {
        this.mView = androidView;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {
        addUnSelectData();
    }


    @Override
    public void unSubscribe() {
        mSubscriptions.unsubscribe();
    }

    private void addUnSelectData() {
        Realm realm = Realm.getDefaultInstance();
        if(realm.where(SelectUnFollow.class).findAll()!=null){
            selectUnFollows = realm.where(SelectUnFollow.class).findAll();
        }else {
            return;
        }

        for(int a=0 ; a<list.size() ; a++){
            realm.beginTransaction();
            SelectUnFollow unFollow = realm.createObject(SelectUnFollow.class);
            unFollow.setName(list.get(a).getName());
            realm.commitTransaction();

            //或者下面这种操作也行
            /*SelectUnFollow selectUnFollow = new SelectUnFollow();
            selectUnFollow.setName(list.get(a).getName());
            realm.beginTransaction();
            realm.copyToRealm(selectUnFollow);
            realm.commitTransaction();*/
        }
    }

    @Override
    public void addData(Activity activity) {
        String[] titles = activity.getResources().getStringArray(R.array.select_follow);
        for(int a=0 ; a<titles.length ; a++){
            SelectPoint selectPoint = new SelectPoint();
            selectPoint.setName(titles[a]);
            list.add(selectPoint);
        }
        mView.refreshData(list);
    }


    @Override
    public void addSelectToRealm(final Integer[] selectedIndices) {
        Realm realm = Realm.getDefaultInstance();
        if(realm.where(SelectUnFollow.class).findAll()!=null){
            selectUnFollows = realm.where(SelectUnFollow.class).findAll();
        }else {
            return;
        }
        for(int a=0 ; a<selectedIndices.length ; a++){
            //点击后添加选择的标签到数据库
            realm.beginTransaction();
            SelectFollow selectFollow = realm.createObject(SelectFollow.class);
            selectFollow.setName(list.get(a).getName());
            realm.commitTransaction();

            //删除，同时删除被选择的标签
            for (int j = 0; j < selectUnFollows.size(); j++) {
                if (selectUnFollows.get(j).getName().equals(list.get(selectedIndices[a]).getName())) {
                    del = j;
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            selectUnFollows.deleteFromRealm(del);
                            //selectUnFollows.remove(del);
                        }
                    });
                    // 此时要注意,因为list会动态变化不像数组会占位,所以当前索引应该后退一位
                    j--;
                }
            }
        }
    }
}
