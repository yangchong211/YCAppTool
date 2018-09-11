package com.ns.yc.lifehelper.ui.data.presenter;

import android.content.res.TypedArray;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.constantApi.ConstantImageApi;
import com.ns.yc.lifehelper.model.bean.ImageIconBean;
import com.ns.yc.lifehelper.ui.data.contract.DataFragmentContract;
import com.ns.yc.lifehelper.ui.main.view.MainActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/03/22
 *     desc  :
 *     revise: v1.4 17年6月8日
 *             v1.5 17年10月3日修改
 * </pre>
 */

public class DataFragmentPresenter implements DataFragmentContract.Presenter {

    private DataFragmentContract.View mView;
    private CompositeSubscription mSubscriptions;
    private MainActivity activity;


    public DataFragmentPresenter(DataFragmentContract.View androidView) {
        this.mView = androidView;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {

    }


    @Override
    public void unSubscribe() {
        if(mSubscriptions.isUnsubscribed()){
            mSubscriptions.unsubscribe();
        }
        if(activity!=null){
            activity = null;
        }
    }

    @Override
    public void bindActivity(MainActivity activity) {
        this.activity = activity;
    }


    @Override
    public List<ImageIconBean> getVpData() {
        List<ImageIconBean> listData = new ArrayList<>();
        TypedArray proPic = activity.getResources().obtainTypedArray(R.array.data_pro_pic);
        String[] proName = activity.getResources().getStringArray(R.array.data_pro_title);
        for (int i = 0; i < proName.length; i++) {
            int proPicId = proPic.getResourceId(i, R.drawable.ic_investment);
            listData.add(new ImageIconBean(proName[i], proPicId,i));
        }
        proPic.recycle();
        return listData;
    }


    @Override
    public void initGridViewData() {
        TypedArray toolLogo = activity.getResources().obtainTypedArray(R.array.data_tool_pro_pic);
        String[] toolName = activity.getResources().getStringArray(R.array.data_tool_pro_title);
        ArrayList<Integer> logoList = new ArrayList<>();
        for(int a=0 ; a<toolName.length ; a++){
            logoList.add(toolLogo.getResourceId(a,R.drawable.ic_investment));
        }
        toolLogo.recycle();
        mView.setGridView(toolName, logoList);
    }


    @SuppressWarnings("AlibabaUndefineMagicConstant")
    @Override
    public void initRecycleViewData() {
        TypedArray typedArray = activity.getResources().obtainTypedArray(R.array.data_narrow_Image);
        final ArrayList<Integer> list = new ArrayList<>();
        int b = 8;
        for(int a=0 ; a<b ; a++){
            list.add(typedArray.getResourceId(a, R.drawable.bg_small_autumn_tree_min));
        }
        typedArray.recycle();
        mView.setRecycleView(list);
    }


}
