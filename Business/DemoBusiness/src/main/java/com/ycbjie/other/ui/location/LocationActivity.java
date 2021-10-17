package com.ycbjie.other.ui.location;

import android.annotation.SuppressLint;
import android.location.Address;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.yc.mocklocationlib.easymock.LocationManagerHelper;
import com.yc.mocklocationlib.easymock.LocationToolUtils;
import com.yc.mocklocationlib.gpsmock.GpsMockFragment;
import com.yc.mocklocationlib.gpsmock.GpsMockManager;
import com.ycbjie.other.R;


public class LocationActivity extends AppCompatActivity {

    private Button btn1;
    private TextView tv11;
    private TextView tv12;
    private Button btn2;
    private EditText longitude;
    private EditText latitude;
    private Button btn3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_content_view);


        btn1 = findViewById(R.id.btn_1);
        tv11 = findViewById(R.id.tv_1_1);
        tv12 = findViewById(R.id.tv_1_2);
        btn2 = findViewById(R.id.btn_2);
        longitude = findViewById(R.id.longitude);
        latitude = findViewById(R.id.latitude);
        btn3 = findViewById(R.id.btn_3);


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //注意6.0及以上版本需要在申请完权限后调用方法
                LocationManagerHelper.getInstance(LocationActivity.this).setAddressCallback(new LocationManagerHelper.AddressCallback() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onGetAddress(Address address) {
                        String countryName = address.getCountryName();//国家
                        String adminArea = address.getAdminArea();//省
                        String locality = address.getLocality();//市
                        String subLocality = address.getSubLocality();//区
                        String featureName = address.getFeatureName();//街道
                        LogUtils.d("LocationUtils 定位地址 countryName："+countryName
                                +" adminArea："+ adminArea+" locality："+ locality+" subLocality："
                                + subLocality+" featureName："+ featureName);
                        tv11.setText(""+countryName + "," + adminArea + ","
                                +locality+","+subLocality+"," +featureName);
                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onGetLocation(double lat, double lng) {
                        LogUtils.d("LocationUtils 定位经纬度"+" lat："+ lat+" lng："+ lng);
                        tv12.setText("lat："+ lat+" lng："+ lng);
                    }
                });
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double lat = LocationToolUtils.parseDouble(latitude.getText().toString());
                double log = LocationToolUtils.parseDouble(longitude.getText().toString());
                //开始开启线程去定位
                LocationManagerHelper.getInstance(LocationActivity.this).start();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GpsMockManager.onAppInit(LocationActivity.this);
                GpsMockFragment fragment = new GpsMockFragment();
                FragmentManager fragmentManager = LocationActivity.this.getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                //transaction.setCustomAnimations(R.anim.push_bottom_in, 0);
                //transaction.show(fragment);       //暂时不用这个
                transaction.add(android.R.id.content, fragment, "GpsMockFragment");
                transaction.commitAllowingStateLoss();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocationManagerHelper.getInstance(LocationActivity.this).stop();
        LocationManagerHelper.getInstance(LocationActivity.this).removeLocationUpdatesListener();
    }
}