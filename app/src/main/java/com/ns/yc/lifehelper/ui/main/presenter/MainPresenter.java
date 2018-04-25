package com.ns.yc.lifehelper.ui.main.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.res.TypedArray;

import com.flyco.tablayout.listener.CustomTabEntity;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.model.entry.TabEntity;
import com.ns.yc.lifehelper.ui.main.contract.MainContract;

import java.util.ArrayList;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import rx.subscriptions.CompositeSubscription;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/03/22
 *     desc  : Main主页面
 *     revise:
 * </pre>
 */
public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;
    private CompositeSubscription mSubscriptions;
    private Activity activity;

    public MainPresenter(MainContract.View androidView) {
        this.mView = androidView;
        this.activity = (Activity) androidView;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mSubscriptions.unsubscribe();
        if(activity!=null){
            activity = null;
        }
    }


    @Override
    public ArrayList<CustomTabEntity> getTabEntity() {
        ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
        TypedArray mIconUnSelectIds = activity.getResources().obtainTypedArray(R.array.main_tab_un_select);
        TypedArray mIconSelectIds = activity.getResources().obtainTypedArray(R.array.main_tab_select);
        String[] mainTitles = activity.getResources().getStringArray(R.array.main_title);
        for (int i = 0; i < mainTitles.length; i++) {
            int unSelectId = mIconUnSelectIds.getResourceId(i, R.drawable.tab_home_unselect);
            int selectId = mIconSelectIds.getResourceId(i, R.drawable.tab_home_select);
            mTabEntities.add(new TabEntity(mainTitles[i],selectId , unSelectId));
        }
        mIconUnSelectIds.recycle();
        mIconSelectIds.recycle();
        return mTabEntities;
    }

    /**
     * 版本更新
     * 后期自己制作json文件，暂时先放着
     */
    @Override
    public void getUpdate() {

    }

    @Override
    public void locationPermissionsTask() {
        startPermissionsTask();
    }


    private static final int RC_LOCATION_CONTACTS_PERM = 124;
    private static final String[] LOCATION_AND_CONTACTS = {
            Manifest.permission.CALL_PHONE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    @AfterPermissionGranted(RC_LOCATION_CONTACTS_PERM)
    private void startPermissionsTask() {
        //检查是否获取该权限
        if (hasPermissions()) {
            //具备权限 直接进行操作
            //Toast.makeText(this, "Location and Contacts things", Toast.LENGTH_LONG).show();
        } else {
            //权限拒绝 申请权限
            //第二个参数是被拒绝后再次申请该权限的解释
            //第三个参数是请求码
            //第四个参数是要申请的权限

            EasyPermissions.requestPermissions(activity,
                    activity.getResources().getString(R.string.easy_permissions),
                    RC_LOCATION_CONTACTS_PERM, LOCATION_AND_CONTACTS);
        }
    }

    /**
     * 判断是否添加了权限
     * @return true
     */
    private boolean hasPermissions() {
        return EasyPermissions.hasPermissions(activity, LOCATION_AND_CONTACTS);
    }


}
