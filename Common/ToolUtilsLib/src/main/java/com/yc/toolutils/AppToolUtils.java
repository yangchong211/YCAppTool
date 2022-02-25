package com.yc.toolutils;

import android.app.Application;

import androidx.annotation.NonNull;

public final class AppToolUtils {

    private static Application sApplication;

    public static void init(@NonNull Application app) {
        if (app == null) {
            throw new NullPointerException("Argument 'app' of type Application (#0 out of 1, zero-based) is marked by @android.support.annotation.NonNull but got null for it");
        } else {
            sApplication = app;
        }
    }

    public static Application getApp() {
        if (sApplication != null) {
            return sApplication;
        } else {
            throw new NullPointerException("u should init first");
        }
    }

}
