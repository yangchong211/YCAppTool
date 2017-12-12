package com.ns.yc.lifehelper.ui.data.presenter;

import android.content.res.TypedArray;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.bean.ImageIconBean;
import com.ns.yc.lifehelper.ui.data.contract.DataFragmentContract;
import com.ns.yc.lifehelper.ui.main.view.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/3/18
 * 描    述：工具页面
 * 修订历史：
 *         v1.5 17年10月3日修改
 * ================================================
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
        mSubscriptions.clear();
        if(activity!=null){
            activity = null;
        }
    }

    @Override
    public void bindView(MainActivity activity) {
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

    @Override
    public void initRecycleViewData() {
        TypedArray typedArray = activity.getResources().obtainTypedArray(R.array.data_narrow_Image);
        final ArrayList<Integer> list = new ArrayList<>();
        for(int a=0 ; a<8 ; a++){
            list.add(typedArray.getResourceId(a, R.drawable.bg_small_autumn_tree_min));
        }
        typedArray.recycle();
        mView.setRecycleView(list);
    }


}
