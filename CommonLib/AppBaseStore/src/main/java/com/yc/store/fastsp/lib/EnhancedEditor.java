package com.yc.store.fastsp.lib;

import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import java.io.Serializable;

public interface EnhancedEditor extends SharedPreferences.Editor {

    EnhancedEditor putSerializable(String key, @Nullable Serializable value);
}
