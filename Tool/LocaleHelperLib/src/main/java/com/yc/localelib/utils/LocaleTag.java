package com.yc.localelib.utils;


import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public final class LocaleTag {

    public static final String TAG_CHINA = "zh-CN";
    public static final String TAG_ENGLISH = "en-US";
    public static final String TAG_MEXICO = "es-MX";
    public static final String TAG_BRAZIL = "pt-BR";
    public static final String TAG_JAPAN = "ja-JP";
    public static final String TAG_ES_419 = "es-419";

    private LocaleTag() {
    }

    @StringDef({TAG_CHINA, TAG_ENGLISH, TAG_MEXICO, TAG_BRAZIL, TAG_ES_419 , TAG_JAPAN})
    @Retention(RetentionPolicy.SOURCE)
    @interface LocaleTagDef {

    }

}
