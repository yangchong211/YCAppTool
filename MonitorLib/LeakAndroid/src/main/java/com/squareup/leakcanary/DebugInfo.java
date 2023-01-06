package com.squareup.leakcanary;

/**
 * description:
 * <p></p>
 *
 * @author yangchong
 * @since 2019-08-13
 */
public class DebugInfo {

    private static DebugInfo instance;

    private boolean isDebug;

    private DebugInfo(){

    }

    public static DebugInfo getInstance(){
        if (null == instance) {
            synchronized (DebugInfo.class) {
                if (null == instance){
                    instance = new DebugInfo();
                }
            }
        }
        return instance;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean debug) {
        isDebug = debug;
    }
}
