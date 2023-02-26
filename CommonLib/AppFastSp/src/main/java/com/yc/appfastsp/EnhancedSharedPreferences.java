package com.yc.appfastsp;

import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import java.io.Serializable;


public interface EnhancedSharedPreferences extends SharedPreferences {

    Serializable getSerializable(String key, @Nullable Serializable defValue);
}
