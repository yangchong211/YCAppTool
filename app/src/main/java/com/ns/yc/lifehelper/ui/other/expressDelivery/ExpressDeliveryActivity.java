package com.ns.yc.lifehelper.ui.other.expressDelivery;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.Constant;
import com.ns.yc.lifehelper.base.BaseActivity;
import com.ns.yc.lifehelper.ui.other.expressDelivery.activity.ExpressDeliveryInfoActivity;
import com.ns.yc.lifehelper.ui.other.expressDelivery.indexModel.SelectorCompanyActivity;

import butterknife.Bind;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/18
 * 描    述：快递查询页面
 * 修订历史：
 * ================================================
 */
public class ExpressDeliveryActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.et_number)
    EditText etNumber;
    @Bind(R.id.tv_company)
    TextView tvCompany;
    @Bind(R.id.tv_search)
    TextView tvSearch;
    private String type;

    @Override
    public int getContentView() {
        return R.layout.activity_express_delivery;
    }

    @Override
    public void initView() {
        toolbarTitle.setText("选择公司");
    }

    @Override
    public void initListener() {
        tvCompany.setOnClickListener(this);
        llTitleMenu.setOnClickListener(this);
        tvSearch.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_company:
                Constant.expressDelivery = etNumber.getText().toString().trim();
                Intent intent = new Intent(this, SelectorCompanyActivity.class);
                startActivityForResult(intent, 100);
                break;
            case R.id.ll_title_menu:
                finish();
                break;
            case R.id.tv_search:
                startSearch();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100 && data!=null){
            String name = data.getStringExtra("name");
            type = data.getStringExtra("type");
            tvCompany.setText(name);
            etNumber.setText(Constant.expressDelivery);
        }
    }

    private void startSearch() {
        String number = etNumber.getText().toString().trim();
        String company = tvCompany.getText().toString().trim();
        if(TextUtils.isEmpty(number)){
            ToastUtils.showShortSafe("快递号不能为空");
            return;
        }
        if(TextUtils.isEmpty(company)){
            ToastUtils.showShortSafe("快递公司不能为空");
            return;
        }

        Intent intent = new Intent(ExpressDeliveryActivity.this,ExpressDeliveryInfoActivity.class);
        intent.putExtra("number",number);
        intent.putExtra("type",type);
        startActivity(intent);
    }



}
