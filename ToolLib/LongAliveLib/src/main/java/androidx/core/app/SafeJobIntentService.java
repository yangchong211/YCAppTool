package androidx.core.app;

import android.os.Build;

/**
 * https://github.com/evernote/android-job/
 */
public abstract class SafeJobIntentService extends JobIntentService {

    @Override
    GenericWorkItem dequeueWork() {
        try {
            return super.dequeueWork();
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // override mJobImpl with safe class to ignore SecurityException
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mJobImpl = new SafeJobServiceEngineImpl(this);
        } else {
            mJobImpl = null;
        }
    }
}
