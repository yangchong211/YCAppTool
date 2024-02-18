package me.pqpo.librarylog4a.logger;

import android.util.Log;

/**
 * Created by pqpo on 2018/2/27.
 */

public class AndroidLogger implements Logger {

    @Override
    public void println(int priority, String tag, String msg) {
        Log.println(priority, tag, msg);
    }

    @Override
    public void flush() {

    }

    @Override
    public void release() {

    }

}
