package com.yc.other.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.yc.localelib.service.LocaleService;
import com.yc.localelib.utils.LocaleUtils;
import com.ycbjie.other.R;

import java.util.List;
import java.util.Locale;

public class LocaleActivity extends AppCompatActivity {

    private TextView mTv1;
    private TextView mTv2;
    private TextView mTv3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locale);


        mTv1 = findViewById(R.id.tv_1);
        mTv2 = findViewById(R.id.tv_2);
        mTv3 = findViewById(R.id.tv_3);

        initListener();
        initData();
    }

    private void initListener() {
        mTv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocaleUtils.updateLocale(LocaleActivity.this,new Locale("ja"));
            }
        });
    }

    private void initData() {
        StringBuilder stringBuilder = new StringBuilder();
        Locale sysLocale = LocaleUtils.getSysLocale();
        stringBuilder.append("获取系统locale的属性 : ").append(sysLocale.toString()).append("\n");
        stringBuilder.append("获取系统locale的tag1 : ").append(LocaleUtils.getSysLocaleTag()).append("\n");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            stringBuilder.append("获取系统locale的tag2 : ").append(sysLocale.toLanguageTag()).append("\n");
            stringBuilder.append("获取系统locale的language : ").append(sysLocale.getLanguage()).append("\n");
            stringBuilder.append("获取系统locale的country : ").append(sysLocale.getCountry()).append("\n");
            stringBuilder.append("获取系统locale的script : ").append(sysLocale.getScript()).append("\n");
            stringBuilder.append("获取系统locale的variant : ").append(sysLocale.getVariant()).append("\n");

            stringBuilder.append("获取系统locale的displayLanguage : ").append(sysLocale.getDisplayLanguage()).append("\n");
            stringBuilder.append("获取系统locale的ISO3Language : ").append(sysLocale.getISO3Language()).append("\n");
            stringBuilder.append("获取系统locale的displayCountry : ").append(sysLocale.getDisplayCountry()).append("\n");
            stringBuilder.append("获取系统locale的ISO3Country : ").append(sysLocale.getISO3Country()).append("\n");
        }
        Log.d("locale data : ",stringBuilder.toString());

        stringBuilder.append("获取手机locale的属性 : ").append(LocaleUtils.getAppLocale(mTv1.getContext()).toString()).append("\n");
        stringBuilder.append("获取手机locale的tag : ").append(LocaleUtils.getAppLocaleTag(mTv1.getContext())).append("\n");
        stringBuilder.append("根据tag获取locale : ").append(LocaleUtils.tagToLocale("US").toString()).append("\n");
        mTv1.setText(stringBuilder.toString());


        setData2();
    }

    private void setData2() {
        Locale appLocale = LocaleUtils.getAppLocale(this);
        StringBuilder stringBuilder2 = new StringBuilder();
        List<Locale> supportLocaleList = LocaleService.getInstance().getSupportLocaleList();
        for (int i=0 ; i<supportLocaleList.size() ; i++){
            Locale locale = supportLocaleList.get(i);
            String displayLanguage = locale.getDisplayLanguage();
            //boolean isSame = LocaleUtils.localeToTag(appLocale).equals(LocaleUtils.localeToTag(locale));
            boolean isSame = LocaleUtils.equalLocales(appLocale, locale);
            stringBuilder2.append("语言 ")
                    .append(isSame ? "当前语言 : " : " : ")
                    .append(displayLanguage)
                    .append("\n");
        }
        mTv2.setText(stringBuilder2);
    }
}
