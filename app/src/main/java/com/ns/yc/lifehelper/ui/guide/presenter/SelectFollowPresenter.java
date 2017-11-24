package com.ns.yc.lifehelper.ui.guide.presenter;

import android.app.Activity;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.BaseApplication;
import com.ns.yc.lifehelper.bean.SelectPoint;
import com.ns.yc.lifehelper.cache.SelectFollow;
import com.ns.yc.lifehelper.cache.SelectUnFollow;
import com.ns.yc.lifehelper.ui.guide.contract.SelectFollowContract;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.subscriptions.CompositeSubscription;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2016/5/18
 * 描    述：关注点页面
 * 修订历史：
 * ================================================
 */
public class SelectFollowPresenter implements SelectFollowContract.Presenter {

    private SelectFollowContract.View mView;
    private CompositeSubscription mSubscriptions;
    private Realm realm;
    private RealmResults<SelectFollow> selectFollows;
    private RealmResults<SelectUnFollow> selectUnFollows;
    private List<SelectPoint> list = new ArrayList<>();
    private int del;


    public SelectFollowPresenter(SelectFollowContract.View androidView) {
        this.mView = androidView;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {
        initRealm();
        addUnSelectData();
    }

    private void initRealm() {
        if(realm ==null){
            realm = BaseApplication.getInstance().getRealmHelper();
        }
    }

    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }

    private void addUnSelectData() {
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
    public void goMainActivity() {
        mView.toMainActivity();
    }

    @Override
    public void addSelectToRealm(final Integer[] selectedIndices) {
        initRealm();
        if(realm.where(SelectFollow.class).findAll()!=null){
            selectFollows = realm.where(SelectFollow.class).findAll();
        }else {
            return;
        }
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
            //realm.copyToRealm(selectFollow);
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
                    //此时要注意,因为list会动态变化不像数组会占位,所以当前索引应该后退一位
                    j--;
                }
            }
        }
    }
}
