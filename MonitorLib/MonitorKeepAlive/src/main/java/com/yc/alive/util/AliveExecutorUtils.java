package com.yc.alive.util;

import android.os.AsyncTask;
import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 异步工具
 * <p>
 *  2018/7/9.
 */
public class AliveExecutorUtils {

    private static final String TAG = "KAExecutorUtils";

    private ExecutorService mExecutorService;

    private AliveExecutorUtils() {
    }

    private static final class InnerHolder {
        private static final AliveExecutorUtils INSTANCE = new AliveExecutorUtils();
    }

    public static AliveExecutorUtils getInstance() {
        return InnerHolder.INSTANCE;
    }

    public void runWorker(@NonNull KARunnable runnable) {
        ensureWorkerHandler();
        new KAAsyncTask().executeOnExecutor(mExecutorService, runnable);
    }

    private void ensureWorkerHandler() {
        if (mExecutorService == null) {
            mExecutorService = Executors.newCachedThreadPool();
        }
    }

    private final static class KAAsyncTask extends AsyncTask<KARunnable, Void, KARunnable> {

        @Override
        protected KARunnable doInBackground(KARunnable...params) {
            if (params == null || params.length == 0) {
                return null;
            }
            KARunnable runnable = params[0];
            runnable.runWorker();
            return runnable;
        }

        @Override
        protected void onPostExecute(KARunnable runnable) {
            if (runnable != null) {
                runnable.runUI();
            }
        }
    }

    public interface KARunnable {

        /**
         * 后台线程
         */
        void runWorker();

        /**
         * UI线程
         */
        void runUI();
    }

}
