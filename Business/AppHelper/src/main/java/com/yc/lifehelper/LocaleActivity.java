package com.yc.lifehelper;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yc.businessinterface.AppServiceProvider;
import com.yc.localelib.locale.service.LocaleService;
import com.yc.localelib.locale.utils.LocaleToolUtils;
import com.yc.spi.loader.ServiceLoader;
import java.util.List;
import java.util.Locale;

public class LocaleActivity extends AppCompatActivity {

    private TextView mTv1;
    private TextView mTv2;
    private TextView mTv3;

    @Override
    protected void attachBaseContext(Context newBase) {
        // 绑定语种
        super.attachBaseContext(LocaleService.getInstance().attachBaseContext(newBase));
    }

    /**
     * 开启页面
     *
     * @param context 上下文
     */
    public static void startActivity(Context context) {
        try {
            Intent target = new Intent();
            target.setClass(context, LocaleActivity.class);
            context.startActivity(target);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locale);


        mTv1 = findViewById(R.id.tv_1);
        mTv2 = findViewById(R.id.tv_2);
        mTv3 = findViewById(R.id.tv_3);

        initListener();
        initData();

        //((TextView) findViewById(R.id.tv_language_activity)).setText(this.getResources().getString(R.string.current_language));
        ((TextView) findViewById(R.id.tv_main_language_application)).setText(getApplication().getResources().getString(R.string.current_language));
        ((TextView) findViewById(R.id.tv_main_language_system)).setText(LocaleToolUtils.getLanguageString(this, LocaleService.getInstance().getSystemLocale(), R.string.current_language));
        RadioGroup radioGroup = findViewById(R.id.rg_main_languages);
        if (LocaleService.getInstance().isSystemLocale()) {
            radioGroup.check(R.id.rb_main_language_auto);
        } else {
            Locale locale = LocaleService.getInstance().getCurrentLocale();
            if (Locale.CHINA.equals(locale)) {
                radioGroup.check(R.id.rb_main_language_cn);
            } else if (Locale.TAIWAN.equals(locale)) {
                radioGroup.check(R.id.rb_main_language_tw);
            } else if (Locale.ENGLISH.equals(locale)) {
                radioGroup.check(R.id.rb_main_language_en);
            } else {
                radioGroup.check(R.id.rb_main_language_auto);
            }
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // 是否需要重启
                boolean restart = false;

                if (checkedId == R.id.rb_main_language_auto) {
                    // 跟随系统
                    restart = LocaleService.getInstance().clearAppLanguage(LocaleActivity.this);
                } else if (checkedId == R.id.rb_main_language_cn) {
                    // 简体中文
                    restart = LocaleService.getInstance().setAppLanguage(LocaleActivity.this, Locale.CHINA);
                } else if (checkedId == R.id.rb_main_language_tw) {
                    // 繁体中文
                    restart = LocaleService.getInstance().setAppLanguage(LocaleActivity.this, Locale.TAIWAN);
                } else if (checkedId == R.id.rb_main_language_en) {
                    // 英语
                    restart = LocaleService.getInstance().setAppLanguage(LocaleActivity.this, Locale.ENGLISH);
                }

                if (restart) {
                    // 1.使用recreate来重启Activity的效果差，有闪屏的缺陷
                    // recreate();

                    // 2.使用常规startActivity来重启Activity，有从左向右的切换动画，稍微比recreate的效果好一点，但是这种并不是最佳的效果
                    // startActivity(new Intent(this, LanguageActivity.class));
                    // finish();

                    // 3.我们可以充分运用 Activity 跳转动画，在跳转的时候设置一个渐变的效果，相比前面的两种带来的体验是最佳的
                    startActivity(new Intent(LocaleActivity.this, MainActivity.class));
                    finish();
                }
            }
        });
    }

    private void initListener() {
        mTv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocaleToolUtils.setLocale(LocaleActivity.this,new Locale("ja"));
            }
        });
    }

    private void initData() {
        StringBuilder stringBuilder = new StringBuilder();
        Locale sysLocale = LocaleToolUtils.getSysLocale();
        stringBuilder.append("获取系统locale的属性 : ").append(sysLocale.toString()).append("\n");
        stringBuilder.append("获取系统locale的tag1 : ").append(LocaleToolUtils.getSysLocale()).append("\n");
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

        stringBuilder.append("获取手机locale的属性 : ").append(LocaleToolUtils.getAppLocale(mTv1.getContext()).toString()).append("\n");
        stringBuilder.append("获取手机locale的tag : ").append(LocaleToolUtils.getAppLocale(mTv1.getContext())).append("\n");
        stringBuilder.append("根据tag获取locale : ").append(LocaleToolUtils.tagToLocale("US").toString()).append("\n");
        mTv1.setText(stringBuilder.toString());


        setData2();
    }

    private void setData2() {
        Locale appLocale = LocaleToolUtils.getAppLocale(this);
        StringBuilder stringBuilder2 = new StringBuilder();
        List<Locale> supportLocaleList = LocaleService.getInstance().getSupportLocaleList();
        for (int i=0 ; i<supportLocaleList.size() ; i++){
            Locale locale = supportLocaleList.get(i);
            String displayLanguage = locale.getDisplayLanguage();
            //boolean isSame = LocaleUtils.localeToTag(appLocale).equals(LocaleUtils.localeToTag(locale));
            boolean isSame = LocaleToolUtils.equalLocales(appLocale, locale);
            stringBuilder2.append("语言 ")
                    .append(isSame ? "当前语言 : " : " : ")
                    .append(displayLanguage)
                    .append("\n");
        }
        mTv2.setText(stringBuilder2);
    }
}
