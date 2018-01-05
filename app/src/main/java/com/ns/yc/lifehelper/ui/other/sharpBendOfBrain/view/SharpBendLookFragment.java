package com.ns.yc.lifehelper.ui.other.sharpBendOfBrain.view;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp1.BaseFragment;
import com.ns.yc.lifehelper.ui.other.sharpBendOfBrain.SharpBendOfBrainActivity;
import com.ns.yc.lifehelper.ui.other.sharpBendOfBrain.adapter.SharpBendLookAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by PC on 2017/9/4.
 * 作者：PC
 */

public class SharpBendLookFragment extends BaseFragment {


    @Bind(R.id.tv_type)
    TextView tvType;
    @Bind(R.id.gridView)
    GridView gridView;
    private SharpBendOfBrainActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (SharpBendOfBrainActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public int getContentView() {
        return R.layout.activity_other_sharp_bend;
    }

    @Override
    public void initView() {
        tvType.setText("脑经急转弯分类");
        initGridView();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    private String[] titles = {"女人","男人","志趣","昆虫","儿童","植物","趣味","数字","搞笑","经典","成语"
            ,"花","聪明","成功","失败","战争","情侣","钱","冷笑话","英语","字谜"};
    private void initGridView() {
        final SharpBendLookAdapter adapter = new SharpBendLookAdapter(activity,titles);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

}
