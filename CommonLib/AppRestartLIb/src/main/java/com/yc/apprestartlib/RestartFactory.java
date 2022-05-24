package com.yc.apprestartlib;

public final class RestartFactory {

    public static final String ALARM = "alarm";
    public static final String SERVICE = "service";
    public static final String LAUNCHER = "launcher";
    public static final String MAINIFEST = "mainifest";

    public static IRestartApp create(String type){
        switch (type) {
            case ALARM:
                return new AlarmRestartImpl();
            case SERVICE:
                return new ServiceRestartImpl();
            case LAUNCHER:
                return new LauncherRestartImpl();
            case MAINIFEST:
                return new MainifestRestartImpl();
            default:
                return new EmptyRestartImpl();
        }
    }

}
